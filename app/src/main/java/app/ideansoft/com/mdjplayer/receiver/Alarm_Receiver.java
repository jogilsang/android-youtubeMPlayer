package app.ideansoft.com.mdjplayer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.PowerManager;

import app.ideansoft.com.mdjplayer.activity.DownloadActivity;

import static android.content.Context.WIFI_SERVICE;

public class Alarm_Receiver extends BroadcastReceiver {

    Context context;

    //PowerManager.WakeLock 빈객체 선언한다.
    private static PowerManager.WakeLock sCpuWakeLock;
    private static WifiManager.WifiLock sWifiLock;
    private static ConnectivityManager manager;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (sCpuWakeLock != null) {
            return;
        }

        if (sWifiLock != null) {
            return;
        }

        // 절전모드로 와이파이 꺼지는것을 방지
        WifiManager wifiManager = (WifiManager)context.getSystemService(WIFI_SERVICE);
        sWifiLock = wifiManager.createWifiLock("wifilock");
        sWifiLock.setReferenceCounted(true);
        sWifiLock.acquire();

        // 시스템에서 powermanager 받아옴
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        // 객체의 제어레벨 설정
        sCpuWakeLock = pm.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE, "app:mdjplayer");

        //acquire 함수를 실행하여 앱을 깨운다. cpu 를 획득한다
        sCpuWakeLock.acquire();

        //와이파이 연결 체크
        manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);//와이파이 연결 체크

        this.context = context;

        // intent로부터 전달받은 string
        String get_yout_string = intent.getExtras().getString("state");

        // 작동할 액티비티를 설정한다
        Intent alarmIntent = new Intent("android.intent.action.sec");
        alarmIntent.setClass(context, DownloadActivity.class);
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // 액티비티를 띄운다
        this.context.startActivity(alarmIntent);

        // 와이파이가 연결되어있다면
//        if (wifi.isConnected()) {
//
//
//
//        }
        // 와이파이가 연결되어있지않다면
//        else {
//            //Toast.makeText(this.context,"WIFI 활성화 시 이용하실 수 있습니다.", Toast.LENGTH_LONG).show();
//
//            // 와이파이 설정화면으로 이동
//
//            Intent intentConfirm = new Intent();
//            intentConfirm.setAction("android.settings.WIFI_SETTINGS");
//            this.context.startActivity(intentConfirm);
//
//        }

        // acquire 함수를 사용하였으면 꼭 release 를 해주어야 한다. cpu를 점유하게 되어 배터리 소모나 메모리 소모에 영향을 미칠 수 있다
        if(sWifiLock != null) {
            sWifiLock.release();
            sWifiLock = null;
        }

        if (sCpuWakeLock != null) {
            sCpuWakeLock.release();
            sCpuWakeLock = null;
        }

    }



}