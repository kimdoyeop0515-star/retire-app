module.exports = {
  name: "은퇴투자 시뮬레이터 Pro",
  slug: "retire-app",
  version: "1.0.1",
  orientation: "portrait",
  userInterfaceStyle: "dark",
  owner: "kpopkop55",
  android: {
    package: "com.app.retirementsimulatorpro",
    versionCode: 2,
    adaptiveIcon: { backgroundColor: "#07091a" },
    permissions: ["INTERNET", "ACCESS_NETWORK_STATE"]
  },
  plugins: [
    ["expo-build-properties", {
      "android": {
        "minSdkVersion": 24,
        "compileSdkVersion": 34,
        "targetSdkVersion": 34
      }
    }]
  ]
};
