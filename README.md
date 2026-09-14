# 🏖 은퇴투자 시뮬레이터

Android 스마트폰용 은퇴 투자 시뮬레이터 앱입니다.

## 📲 APK 설치 방법 (컴퓨터 없이 폰만으로)

### 방법 1: GitHub Actions 자동 빌드 (권장)

1. **GitHub 계정 생성**: https://github.com 에서 무료 계정 만들기
2. **이 프로젝트 업로드**:
   - GitHub 앱(iOS/Android)에서 새 저장소(Repository) 만들기
   - 또는 github.com 웹사이트에서 "New repository" 클릭
   - 저장소 이름: `retire-app` (영어로)
   - **ZIP 파일의 모든 파일을 업로드**
3. **자동 빌드 시작**:
   - 파일 업로드 후 자동으로 APK 빌드 시작
   - 저장소 → **Actions** 탭 클릭
   - "Build Android APK" 워크플로우 실행 확인
4. **APK 다운로드**:
   - Actions → 완료된 워크플로우 클릭
   - 하단 **Artifacts** 섹션에서 `은퇴투자_시뮬레이터_APK` 다운로드
   - 또는 **Releases** 탭에서 최신 릴리스의 APK 다운로드

### APK 설치 (Android 폰)

1. 다운로드한 `app-debug.apk` 파일 열기
2. "출처를 알 수 없는 앱" 메시지 → **설정** 클릭
3. "이 소스의 앱 허용" 활성화
4. 뒤로가기 → **설치** 클릭
5. 설치 완료 → **열기** 클릭

---

## 🔧 직접 빌드 (Android Studio)

```bash
git clone <이 저장소>
cd retire-app
./gradlew assembleDebug
# APK: app/build/outputs/apk/debug/app-debug.apk
```

## ✨ 주요 기능

- 📊 **포트폴리오 관리**: GOOGL, NVDA, STRC, STRD 등
- 💰 **실제 투자 관리**: 보유수량, 평균매수가, 평가손익
- 🎲 **몬테카를로**: 목표 달성 확률 시뮬레이션
- 📈 **은퇴 시뮬레이션**: 자산 성장 추이 (9999년까지)
- 💸 **세금/건보료 추정**: 2024년 기준
- 🏖 **은퇴 후 인출**: 4% 룰, 자산 고갈 시뮬
- 🔄 **리밸런싱**: 목표 비중 vs 현재 비중
- 📰 **SAVE 연동**: SAVE 앱/웹 뉴스 연결
- 💾 **백업/복원**: JSON 형태로 데이터 보호

## ⚠️ 주의사항

- 세금/건보료는 추정치이며 실제와 다를 수 있습니다
- 투자 결정은 전문가와 상담하세요
- 증권사 로그인/계좌 연동 기능 없음
