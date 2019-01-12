package app.ideansoft.com.mdjplayer.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import app.ideansoft.com.mdjplayer.R;
import app.ideansoft.com.mdjplayer.receiver.Alarm_Receiver;

public class AlarmActivity extends AppCompatActivity {

    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    Context context;
    PendingIntent pendingIntent;

    Button alarm_on;
    Button alarm_off;

    public static int getHourTimePicker = 0;
    public static int getMinuteTimePicker = 0;
    public static final long ONE_DAY_MILLIS = 1000 * 24 * 60 * 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        this.context = this;

        // 알람매니저 설정
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // 타임피커 설정
        alarm_timepicker = findViewById(R.id.time_picker);

        // Calendar 객체 생성
        final Calendar calendar = Calendar.getInstance();

        // 알람리시버 intent 생성
        final Intent my_intent = new Intent(this.context, Alarm_Receiver.class);

        // 알람 시작 버튼
        alarm_on = (Button) findViewById(R.id.btn_start);


        alarm_on.setOnClickListener(new View.OnClickListener() {
            //            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                // 23 이하, Old
                if (Build.VERSION.SDK_INT < 23) {

                    calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getCurrentHour());
                    calendar.set(Calendar.MINUTE, alarm_timepicker.getCurrentMinute());
                    calendar.set(Calendar.SECOND, 0);

                    // 시간 가져옴
                    getHourTimePicker = alarm_timepicker.getCurrentHour();
                    getMinuteTimePicker = alarm_timepicker.getCurrentMinute();
                }
                // 23 이상, New
                else {
                    // calendar에 시간 셋팅
                    calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
                    calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());
                    calendar.set(Calendar.SECOND, 0);

                    // 시간 가져옴
                    getHourTimePicker = alarm_timepicker.getHour();
                    getMinuteTimePicker = alarm_timepicker.getMinute();
                }

                // 한국 시간 TimeZone time
                Date date = new Date();
                DateFormat df = new SimpleDateFormat(
                        "HH:mm");
                TimeZone time = TimeZone.getTimeZone("Asia/Seoul");
                df.setTimeZone(time);

                // 현재 시간 받아오기
                String str = df.format(date);

                // 먼저 @ 의 인덱스를 찾는다 - 인덱스 값: 5
                int idx = str.indexOf(":");

                // : 앞부분을 추출
                // substring은 첫번째 지정한 인덱스는 포함하지 않는다.
                // 아래의 경우는 첫번째 문자열인 a 부터 추출된다.
                String strCurrentHour = str.substring(0, idx);
                int numCurrentHour = Integer.valueOf(strCurrentHour);

                // 뒷부분을 추출
                // 아래 substring은 @ 바로 뒷부분인 n부터 추출된다.
                String strCurrentMin = str.substring(idx + 1);
                int numCurrentMin = Integer.valueOf(strCurrentMin);


                // reveiver에 string 값 넘겨주기
                my_intent.putExtra("state", "alarm on");

                pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 0, my_intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                // 현재시간이 설정한 시간보다 크다면 ( 다음날 울려야함, 24 시간 더해줌 )
                if (numCurrentHour >= getHourTimePicker && numCurrentMin >= getMinuteTimePicker) {

                    Toast.makeText(AlarmActivity.this, "다음날 알람예정 " + getHourTimePicker + "시 " + getMinuteTimePicker + "분", Toast.LENGTH_SHORT).show();

                    // ONE_DAY_MILLIS = 1000 * 24 * 60 * 60
                    alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + ONE_DAY_MILLIS, pendingIntent);

                }
                // 현재시간이 설정한 시간보다 작다면 ( 울리게되있음 패스)
                else {

                    Toast.makeText(AlarmActivity.this, "금일 알람예정 " + getHourTimePicker + "시 " + getMinuteTimePicker + "분", Toast.LENGTH_SHORT).show();

                    // pass
                    // 알람셋팅
                    alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                }

                // 액티비티 종료
                finish();

            }
        });

        // 취소 버튼 클릭시
        alarm_off = (Button) findViewById(R.id.btn_finish);

        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 0, my_intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                // 알람 해제시에 cancel 호출, 이미 설정된 알람 취소
                // if (pendingIntent != null) {
                //     alarm_manager.cancel(pendingIntent);
                //     //pendingIntent.cancel();
                //     Toast.makeText(AlarmActivity.this, "기존 알람을 취소합니다.", Toast.LENGTH_SHORT).show();
                // }

                Toast.makeText(AlarmActivity.this, "알람없이 진행합니다.", Toast.LENGTH_SHORT).show();

                // 유튜브 실행
                Intent Existed = new Intent(AlarmActivity.this, DownloadActivity.class);
                startActivity(Existed);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });
    }
}
