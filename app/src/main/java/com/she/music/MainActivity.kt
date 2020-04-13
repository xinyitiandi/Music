package com.she.music

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.she.music_play_library.manger.AudioHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var audioHelper:AudioHelper?=null
    private var mAudioUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAudioUrl="http://www.170mv.com/kw/antiserver.kuwo.cn/anti.s?rid=MUSIC_82944321&response=res&format=mp3|aac&type=convert_url&br=128kmp3&agent=iPhone&callback=getlink&jpcallback=getlink.mp3"

        audioHelper=AudioHelper.instance
        audioHelper?.init(tv_current_time,tv_all_time,seekBar_audio,iv_pause_or_play,this)//初始化界面的View
        audioHelper?.setAudioUrl(mAudioUrl!!)//设置音乐播放源
    }

    override fun onPause() {
        super.onPause()
        audioHelper?.pauseAudio()//暂停
    }

    override fun onDestroy() {
        super.onDestroy()
        audioHelper?.release()//退出时候释放资源
    }
}
