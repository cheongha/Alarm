package cjdgk7.s.alarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.TimePicker
import java.util.*

class MainActivity : AppCompatActivity() {

    //var alarm_manager: AlarmManager? = null
    //private var alarm_timepicker: TimePicker? = null
    var context: Context? = null
    var pendingIntent: PendingIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.context = this

        // 알람매니저 설정
        val alarm_manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // 타임피커 설정
        val alarm_timepicker: TimePicker? = findViewById(R.id.time_picker)

        // Calendar 객체 생성
        val calendar = Calendar.getInstance()

        // 알람리시버 intent 생성
        val my_intent = Intent(this.context, AlarmReciver::class.java)

        // 알람 시작 버튼
        var alarm_on : Button = findViewById(R.id.btn_start)
        alarm_on.setOnClickListener(object : View.OnClickListener {
            @RequiresApi(api = Build.VERSION_CODES.M)
            override fun onClick(v: View) {

                // calendar에 시간 셋팅
                calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker!!.getHour())
                calendar.set(Calendar.MINUTE, alarm_timepicker!!.getMinute())

                // 시간 가져옴
                val hour = alarm_timepicker!!.getHour()
                val minute = alarm_timepicker!!.getMinute()
                Toast.makeText(
                    this@MainActivity,
                    "Alarm 예정 " + hour + "시 " + minute + "분",
                    Toast.LENGTH_SHORT
                ).show()

                // reveiver에 string 값 넘겨주기
                my_intent.putExtra("state", "alarm on")

                pendingIntent = PendingIntent.getBroadcast(
                    this@MainActivity, 0, my_intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                // 알람셋팅
                alarm_manager.set(
                    AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    pendingIntent
                )
            }
        })

        // 알람 정지 버튼
        val alarm_off: Button = findViewById(R.id.btn_finish)
        alarm_off.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (pendingIntent != null){
                    Toast.makeText(this@MainActivity, "Alarm 종료", Toast.LENGTH_SHORT).show()
                    // 알람매니저 취소
                    alarm_manager.cancel(pendingIntent)

                    my_intent.putExtra("state", "alarm off")

                    // 알람취소
                    sendBroadcast(my_intent)
                }
            }
        })
    }
}
