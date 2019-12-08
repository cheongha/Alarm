package cjdgk7.s.alarm

import android.media.MediaPlayer
import android.content.Intent
import androidx.core.app.NotificationCompat
import android.app.NotificationManager
import android.app.NotificationChannel
import android.app.Service
import android.content.Context
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.Nullable


class RingtonePlayingService : Service() {

    var mediaPlayer: MediaPlayer? = null
    var startId: Int = 0
    var isRunning: Boolean = false

    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        Log.e("Service", "진입")

        if (Build.VERSION.SDK_INT >= 26) {
            val CHANNEL_ID = "default"
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                channel
            )

            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("알람시작")
                .setContentText("알람음이 재생됩니다.")
                .setSmallIcon(R.mipmap.ic_launcher)

                .build()

            startForeground(1, notification)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        var startId = startId

        val getState = intent.extras!!.getString("state")!!

        when (getState) {
            "alarm on" -> startId = 1
            "alarm off" -> startId = 0
            else -> startId = 0
        }

        // 알람음 재생 X , 알람음 시작 클릭
        if (!this.isRunning && startId == 1) {
            Log.e("Service", "노래 나오는중")

            mediaPlayer = MediaPlayer.create(this, R.raw.ouu)
            mediaPlayer!!.start()

            this.isRunning = true
            this.startId = 0
        } else if (this.isRunning && startId == 0) {

            mediaPlayer!!.stop()
            mediaPlayer!!.reset()
            mediaPlayer!!.release()

            this.isRunning = false
            this.startId = 0
        } else if (!this.isRunning && startId == 0) {

            this.isRunning = false
            this.startId = 0

        } else if (this.isRunning && startId == 1) {

            this.isRunning = true
            this.startId = 1
        } else {
        }// 알람음 재생 O , 알람음 시작 버튼 클릭
        // 알람음 재생 X , 알람음 종료 버튼 클릭
        // 알람음 재생 O , 알람음 종료 버튼 클릭
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("onDestory() 실행", "서비스 파괴")

    }
}
