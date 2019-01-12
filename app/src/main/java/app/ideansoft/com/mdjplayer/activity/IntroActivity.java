package app.ideansoft.com.mdjplayer.activity;

/**
 * Created by User on 2017-02-20.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.widget.Toast;

import app.ideansoft.com.mdjplayer.R;


public class IntroActivity extends Activity {

    Handler h;//핸들러 선언

    public static int selectedDirection = 1;
    // default value : DB exist

    public String dirPath = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //인트로화면이므로 타이틀바를 없앤다
        setContentView(R.layout.activity_intro);

        h = new Handler(); //딜래이를 주기 위해 핸들러 생성
        h.postDelayed(mrun, 3000); // 딜레이 ( 런어블 객체는 mrun, 시간 2초)


    }

    Runnable mrun = new Runnable() {
        @Override
        public void run() {

            int permissionCheck_1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int permissionCheck_2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
            int permissionCheck_3 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET);
            int permissionCheck_4 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WAKE_LOCK);
            int permissionCheck_5 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_NETWORK_STATE);

            // 권한 다 있으면
            if (permissionCheck_1 == PackageManager.PERMISSION_GRANTED
                    && permissionCheck_2 == PackageManager.PERMISSION_GRANTED
                    && permissionCheck_3 == PackageManager.PERMISSION_GRANTED
                    && permissionCheck_4 == PackageManager.PERMISSION_GRANTED
                    && permissionCheck_5 == PackageManager.PERMISSION_GRANTED
                    ) {

                Intent Existed = new Intent(IntroActivity.this, AlarmActivity.class);
                startActivity(Existed);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                finish();

            // 약관동의안하고 권한 없으면
            } else {

                // 권한을 요청합니다
                getPermission();
                finish();

            }

        }

        //overridePendingTransition 이란 함수를 이용하여 fade in,out 효과를줌. 순서가 중요
        // 새로설치하거나, 휴대폰을 바꿨거나

    };

    //인트로 중에 뒤로가기를 누를 경우 핸들러를 끊어버려 아무일 없게 만드는 부분
    //미 설정시 인트로 중 뒤로가기를 누르면 인트로 후에 홈화면이 나옴.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        h.removeCallbacks(mrun);
    }

    // 값 불러오기

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getPermission(){

        ActivityCompat.requestPermissions(IntroActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.WAKE_LOCK
                },
                1000);

        // 재실행
        Toast.makeText(IntroActivity.this,"권한요청 승인 후 앱을 재실행해주세요.",Toast.LENGTH_SHORT).show();

    }


}

