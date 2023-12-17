# AIon-JBNU 캡스톤 프로젝트 데모 앱

## 사용 라이브러리
- Firebase-Messaging

## 설명
해당 앱은 Firebase를 통해 온 알림을 띄우는 앱입니다.

MainActivity의 onCreate() 함수에서 MyFirebaseMessaging 서비스를 실행시킵니다.
해당 서비스는 FirebaseMessagingService를 상속하며, onMessageReceived에서 Firebase에서 메시지가 왔을 때 알림을 띄우는 코드를 실행시킵니다.

데모 테스트를 위해 FirebaseMessagingService가 실행될 때, 해당 기기의 Firebase Cloud Messaging Token을 구해서 Log로 출력하게 해두었습니다.
````java
public MyFirebaseMessaging() {
    super();
    Task<String> token = FirebaseMessaging.getInstance().getToken();
    token.addOnCompleteListener((task) -> {
        if (task.isSuccessful()) {
            Log.d("FCM TOKEN", task.getResult());
            MyApplication.FCM_TOKEN = task.getResult();
            if (MyApplication.LISTENER != null) {
                MyApplication.LISTENER.onFCMTokenReload();
            }
        }
    });
}
````

데모 실행 시에는 해당 Token을 저장해두었다가 코드에 직접 넣어 실행하였습니다.
