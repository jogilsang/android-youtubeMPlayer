package app.ideansoft.com.mdjplayer.activity;


import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import app.ideansoft.com.mdjplayer.R;
import app.ideansoft.com.mdjplayer.util.DownloadManager;

public class DownloadActivity extends AppCompatActivity {

    // 액셀을 읽는다
    // 액셀은 참조 또는 다운을 받아서한다
    // 출력내용을 리스트뷰나 리사이클러뷰로 출력한다
    // 해당 아이템 클릭시 영상을 재생한다.
    // framelayout 형식으로 하고, 재생이 끝낮는대 재생안된 리스트에 목록이있다면
    // 그거를 시작함
    // 리스트는 총 3개임
    // 다운받은 리스트
    // 재생한 리스트
    // 재생안한 리스트
    // 재생하면 재생한 리스트에 들어가고, 재생안한 리스트에서 해당 리스트를 없앤다
    // 영상이 끝나는 이벤트를 받으면, 다음 리스트에서 찾아서 재생하는걸로한다

    List<String> listPermissionsNeeded = new ArrayList<>();

//    int permissionWriteExternal = ContextCompat.checkSelfPermission(MainActivity.this,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//    int permissionReadExternal = ContextCompat.checkSelfPermission(MainActivity.this,
//            Manifest.permission.READ_EXTERNAL_STORAGE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 잠금화면 위에서도 사용할수있게
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String strDir = file.getAbsolutePath();

        new DownloadManager(this.getApplicationContext());
        DownloadManager.getInstance().setSavePath(strDir); // 저장하려는 경로 지정.
        DownloadManager.getInstance().setDownloadUrl(getString(R.string.downloadLink));

        finish();
//        String[] strUrl = new String[3];
//        strUrl[0] = "http://..../file1.png";
//        strUrl[1] = "http://..../file2.png";
//        strUrl[2] = "http://..../file3.png";
//
//        DownloadManager.getInstance().setDownloadUrl(strUrl);


    }



}
