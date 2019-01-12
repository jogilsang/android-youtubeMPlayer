package app.ideansoft.com.mdjplayer.activity;


/**
 * Created by user on 12/14/2018.
 */

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import app.ideansoft.com.mdjplayer.R;


public class YoutubeActivity extends YouTubeBaseActivity {

    YouTubePlayerView youtubeView;
    YouTubePlayer mYouTubePlayer;
    Boolean isFullscreen = false;
    ImageView youtubeScreen;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    YouTubePlayer.PlayerStateChangeListener playerStateChangeListener;
    YouTubePlayer.PlaybackEventListener playbackEventListener;

    final static String videoId = "bOzk0OLz0Jc";

    // 테스트
    //    https://www.youtube.com/watch?v=Zr5K5phugA4
    //    https://www.youtube.com/watch?v=zLXPUuABY-o
    //    https://www.youtube.com/watch?v=IhM4dS1L2ZQ
    //    https://www.youtube.com/watch?v=BYUa3MjWbqg
    //    https://www.youtube.com/watch?v=9XQwA4iytk0

    static ArrayList<String> videoIdList = new ArrayList<String>();
    static int currentPosition = 0;

    public void overlayMode(){

        // 투명하게
        youtubeView.setAlpha(0);

        // 세로 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        // 잠금화면 위에서도 사용할수있게
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        youtubeView = (YouTubePlayerView) findViewById(R.id.youtubeView);
        youtubeScreen = (ImageView)findViewById(R.id.youtubeScreen);

        // 화면 덮어씌우는 모드
        overlayMode();

        onInitializedListener = new YouTubePlayer.OnInitializedListener() {

            @Override

            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean wasRestored) {

                //mYouTubePlayer = youTubePlayer;
                //youTubePlayer.setFullscreen(isFullscreen);

                // 시스템에서 자동조절
                //youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);

                // 커스텀 레이아웃 선에서 최대화
                youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);

                // 유튜브 영상 최대화 허용
                // 기기가 가로방향이 될때 자동전체화면
                //youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);

                youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {

                    @Override
                    public void onFullscreen(boolean fullscreen) {
                        isFullscreen = fullscreen;

                    }
                });

                if (!wasRestored) {

                    // 플레이할 리스트가 더이상없음
                    if(videoIdList.size() == 0) {
                        Toast.makeText(YoutubeActivity.this, "시청할 영상이 없습니다",Toast.LENGTH_SHORT).show();
                    }
                    // 플레이할 리스트가 있음
                    else {
                        youTubePlayer.loadVideo(videoIdList.get(currentPosition));
                        videoIdList.remove(currentPosition);
                    }
                }

                youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {

                    @Override
                    public void onLoading() {

                    }

                    @Override
                    public void onLoaded(String s) {

                    }

                    @Override
                    public void onAdStarted() {

                    }

                    // 비디오가 시작된다면
                    @Override
                    public void onVideoStarted() {

                    }

                    // 비디오가 끝난다면
                    @Override
                    public void onVideoEnded() {

                        // 리스트가 있다고 가정하면, 새로운 리스트의 주소로 영상을 시청한다
                        if(videoIdList.size() != 0 || !(videoIdList.isEmpty())) {
                            youTubePlayer.loadVideo(videoIdList.get(currentPosition));
                            videoIdList.remove(currentPosition);
                        }
                        // 더이상 볼 영상이 없다면 앱을 종료한다
                        else {
                            Toast.makeText(YoutubeActivity.this, "모든 영상 시청을 마칩니다.",Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }

                    @Override
                    public void onError(YouTubePlayer.ErrorReason errorReason) {

                    }
                });

                youTubePlayer.setPlaybackEventListener( playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
                    @Override
                    public void onBuffering(boolean arg0) {
                    }

                    @Override
                    public void onPaused() {

                    }

                    @Override
                    public void onPlaying() {
                    }

                    @Override
                    public void onSeekTo(int arg0) {
                    }

                    @Override
                    public void onStopped() {
                    }
                });

//                if (!wasRestored) {
//                    youTubePlayer.cueVideo(videoId);
//                }

            }


            @Override

            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                Toast.makeText(YoutubeActivity.this, "onInitializationFailure...",Toast.LENGTH_SHORT).show();

            }

        };


        // 파일경로
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        // 파일경로 + 파일이름
        String strDir = file.getPath()+ "/" + getString(R.string.downloadFileName);

        File downloadData = new File(strDir);
        // 액셀 읽기

        if(downloadData.exists()) {
            Toast.makeText(YoutubeActivity.this, "파일이 있습니다.\n영상을 재생합니다.",Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(YoutubeActivity.this, "파일이 없습니다.",Toast.LENGTH_SHORT).show();

        }

        try {
            // assets 경로에 있는 액셀 파일을 읽는다
            //InputStream is = new FileInputStream(strDir);

            // 테스트코드
            //InputStream is = getBaseContext().getResources().getAssets().open("URL_List.xls");
            //Workbook wb = Workbook.getWorkbook(is);

            // 코드완료
            Workbook wb = Workbook.getWorkbook(new java.io.File(strDir));

            if(wb != null) {
                Sheet sheet = wb.getSheet(0);   // 시트 불러오기
                if(sheet != null) {
                    int colTotal = sheet.getColumns();    // 전체 컬럼
                    int rowIndexStart = 0;                  // row 인덱스 시작
                    int rowTotal = sheet.getColumn(colTotal-1).length;

                    StringBuilder sb;
                    for(int row=rowIndexStart;row<rowTotal;row++) {
                        sb = new StringBuilder();
                        for(int col=0;col<colTotal;col++) {
                            String contents = sheet.getCell(col, row).getContents();
                            sb.append("col"+col+" : "+contents+" , ");

                            // 2번쨰 행이면
                            if(col ==1) {

                                // non-printable Unicode characters in Java
                                // 출력할수없는 글자들, 공백으로 변경
                                String temp = contents.replaceAll("\\P{Print}","").trim();

                                if(temp.equals("") || temp == null) {

                                }
                                else {

                                    int idx =0;

                                    // be/ 가 포함된 패턴이라면
                                    if(temp.contains("be/")) {

                                        // 먼저 "be/"의 인덱스를 찾는다 - 인덱스 값: 5
                                        idx = temp.indexOf("be/");

                                    }
                                    // ?v= 가 포함된 패턴이라면
                                    else if(temp.contains("?v=")) {

                                        // 먼저 "?v="의 인덱스를 찾는다 - 인덱스 값: 5
                                        idx = temp.indexOf("?v=");

                                    }
                                    else {
                                        idx = temp.indexOf("be/");
                                    }

                                    // 뒷부분을 추출
                                    // 아래 substring은 = 바로 뒷부분인 n부터 추출된다.
                                    String result = temp.substring(idx+3);

                                    // 12자리보다 크면 아이디값 뺴고 뒤에 삭제
                                    if(result.length() >= 12) {
                                        result = result.substring(0, 12).trim();
                                    }

                                    // 출력완료
                                    System.out.println(result);

                                    // 문자열을 넣는다
                                    videoIdList.add(result);

                                }

                            }

                        }
                        Log.i("test", sb.toString());
                    }

                    // api 키 값
                    youtubeView.initialize(getString(R.string.google_api_key), onInitializedListener);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }



        // 액셀 읽기
//
//        videoIdList.add("Zr5K5phugA4");
//        videoIdList.add("zLXPUuABY-o");
//        videoIdList.add("IhM4dS1L2ZQ");
//        videoIdList.add("BYUa3MjWbqg");
//        videoIdList.add("9XQwA4iytk0");

    }

}