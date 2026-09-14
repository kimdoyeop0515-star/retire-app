package com.retireplanner.app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private var fileUploadCallback: ValueCallback<Array<Uri>>? = null

    private val filePicker = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val uris: Array<Uri>? = if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { arrayOf(it) }
        } else null
        fileUploadCallback?.onReceiveValue(uris)
        fileUploadCallback = null
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = WebView(this)
        setContentView(webView)

        // WebView 설정
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            allowFileAccess = true
            allowContentAccess = true
            setSupportZoom(false)
            loadWithOverviewMode = true
            useWideViewPort = true
            mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
            cacheMode = WebSettings.LOAD_DEFAULT
            mediaPlaybackRequiresUserGesture = false
        }

        // 다크모드 지원
        if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
            @Suppress("DEPRECATION")
            WebSettingsCompat.setForceDark(
                webView.settings,
                WebSettingsCompat.FORCE_DARK_AUTO
            )
        }

        // ChromeClient (파일 업로드, 알림 등)
        webView.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                view: WebView,
                callback: ValueCallback<Array<Uri>>,
                params: FileChooserParams
            ): Boolean {
                fileUploadCallback = callback
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "*/*"
                    addCategory(Intent.CATEGORY_OPENABLE)
                }
                filePicker.launch(intent)
                return true
            }

            override fun onConsoleMessage(msg: ConsoleMessage): Boolean {
                // 콘솔 로그는 무시 (release 빌드)
                return true
            }
        }

        // WebViewClient
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                val url = request.url.toString()
                return when {
                    // SAVE 앱 관련 URL은 외부 브라우저/앱으로
                    url.contains("saveticker.com") || url.contains("savenews.app") -> {
                        try {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                        } catch (e: Exception) {
                            Toast.makeText(this@MainActivity, "열 수 없는 링크입니다", Toast.LENGTH_SHORT).show()
                        }
                        true
                    }
                    // Play Store
                    url.startsWith("market://") -> {
                        try {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                        } catch (e: Exception) {
                            val webUrl = url.replace("market://", "https://play.google.com/store/apps/")
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(webUrl)))
                        }
                        true
                    }
                    // 내부 assets은 허용
                    url.startsWith("file:///android_asset/") -> false
                    // 외부 http/https는 WebView 내에서 로드
                    else -> false
                }
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                // 메인 페이지 오류 시 재시도
                if (request.isForMainFrame) {
                    view.loadUrl("file:///android_asset/app.html")
                }
            }
        }

        // JavaScript Bridge
        webView.addJavascriptInterface(AndroidBridge(), "AndroidBridge")

        // 앱 로드
        webView.loadUrl("file:///android_asset/app.html")
    }

    // JavaScript ↔ Android 브릿지
    inner class AndroidBridge {
        @JavascriptInterface
        fun getAppVersion(): String {
            return try {
                packageManager.getPackageInfo(packageName, 0).versionName ?: "1.0.0"
            } catch (e: Exception) { "1.0.0" }
        }

        @JavascriptInterface
        fun showToast(message: String) {
            runOnUiThread {
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
            }
        }

        @JavascriptInterface
        fun openSaveApp(ticker: String) {
            runOnUiThread {
                // SAVE 앱 설치 여부 확인
                val savePackage = "com.savenews.app"
                val intent = packageManager.getLaunchIntentForPackage(savePackage)
                if (intent != null) {
                    startActivity(intent)
                } else {
                    // 미설치 → Play Store
                    val playUrl = "https://play.google.com/store/apps/details?id=$savePackage"
                    try {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$savePackage")))
                    } catch (e: Exception) {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(playUrl)))
                    }
                }
            }
        }

        @JavascriptInterface
        fun openSaveNews(ticker: String) {
            runOnUiThread {
                val url = if (ticker.isNotBlank()) {
                    "https://www.saveticker.com/news?q=$ticker"
                } else {
                    "https://www.saveticker.com/news"
                }
                // WebView 내에서 뉴스 열기
                webView.loadUrl(url)
            }
        }

        @JavascriptInterface
        fun isSaveAppInstalled(): Boolean {
            return try {
                packageManager.getPackageInfo("com.savenews.app", 0)
                true
            } catch (e: Exception) { false }
        }

        @JavascriptInterface
        fun openExternalUrl(url: String) {
            runOnUiThread {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "열 수 없습니다", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }
}
