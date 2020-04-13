package com.she.music_play_library.manger

import android.app.Activity
import android.media.MediaPlayer
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import com.she.music_play_library.R
import com.she.music_play_library.custom.CustomSeekBar
import com.she.music_play_library.util.AppUtil

class AudioHelper private constructor() {

    private var tv_current_time: TextView? = null
    private var tv_all_time: TextView? = null
    private var seekBar_audio: CustomSeekBar? = null
    private var mediaManager: MediaManager? = null
    private var iv_pause_or_play: ImageView? = null
    private var mActivity: Activity? = null
    private var mAudioUrl: String? = null

    private val UPDATE_PROGRESS = 1
    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                UPDATE_PROGRESS -> {
                    if (mediaManager?.getMediaPlayer() != null && mediaManager!!.isPlaying()) {
                        val mMediaPlayer: MediaPlayer = mediaManager!!.getMediaPlayer()
                        tv_current_time?.setText(
                            AppUtil.secondToMyTime(mMediaPlayer.currentPosition / 1000)
                                .toString() + ""
                        )
                        tv_all_time?.setText(
                            AppUtil.secondToMyTime(mMediaPlayer.duration / 1000)
                                .toString() + ""
                        )
                        seekBar_audio?.setMax(100)
                        seekBar_audio?.setProgress(mMediaPlayer.currentPosition * 100 / mMediaPlayer.duration)
                    }
                    sendEmptyMessage(UPDATE_PROGRESS)
                }
            }
        }
    }

    companion object {
        val instance: AudioHelper by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AudioHelper()
        }
    }

    fun init(
        tv_current_time: TextView?,
        tv_all_time: TextView?,
        seekBar_audio: CustomSeekBar,
        iv_pause_or_play: ImageView,
        mActivity: Activity) {
        this.tv_current_time = tv_current_time
        this.tv_all_time = tv_all_time
        this.seekBar_audio = seekBar_audio
        this.iv_pause_or_play = iv_pause_or_play
        this.mActivity = mActivity

        tv_current_time?.setText("00:00")
        tv_all_time?.setText("00:00")
        seekBar_audio.isCanDrag(false)

        mediaManager = MediaManager()
        handler.sendEmptyMessage(UPDATE_PROGRESS)

        initEventListener()
    }

    /**
     * 设置音乐的播放地址
     */
    fun setAudioUrl(mAudioUrl:String){
        this.mAudioUrl = mAudioUrl
    }

    private fun initEventListener() {
        iv_pause_or_play?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (AppUtil.isDoubleClick()) {
                    return
                }
                //音频文件的暂停或播放
                if (TextUtils.isEmpty(mAudioUrl)) {
                    Toast.makeText(mActivity, "无效的音频文件", Toast.LENGTH_LONG).show()
                    return
                }

                if (mediaManager!!.isPause) { //如果当前是暂停状态，那么恢复播放
                    mediaManager!!.resume()
                    iv_pause_or_play!!.setImageResource(R.drawable.icon_audio_play)
                } else {
                    if (mediaManager!!.isPlaying) { //如果当前是正在播放状态
                        mediaManager!!.pause()
                        iv_pause_or_play!!.setImageResource(R.drawable.icon_audio_pause)
                    } else { //如果当前是未播状态，那么启动播放
                        //播放音频
                        iv_pause_or_play!!.setImageResource(R.drawable.icon_audio_play)
                        mediaManager!!.playSound(mAudioUrl + "") { mp ->
                            mp.seekTo(0)
                            seekBar_audio!!.progress = 0
                            tv_current_time!!.text = "00:00"
                            tv_all_time!!.text = AppUtil.secondToMyTime(mp.duration / 1000).toString() + ""
                            mp.pause()
                            mediaManager!!.isPause = true
                            iv_pause_or_play!!.setImageResource(R.drawable.icon_audio_pause)
                        }
                    }
                }
            }

        })
        mediaManager?.setOnBufferingUpdateListener(object : MediaManager.OnBufferingUpdateListener {
            override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
                seekBar_audio!!.secondaryProgress = percent
                if (percent > 0) {
                    seekBar_audio!!.isCanDrag(true)
                }
            }
        })

        seekBar_audio?.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                if (mediaManager!!.mediaPlayer != null && fromUser) {
                    mediaManager!!.mediaPlayer.seekTo(
                        seekBar.progress * mediaManager!!.mediaPlayer
                            .duration / seekBar.max
                    )
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    fun pauseAudio(){
        mediaManager!!.pause()
        iv_pause_or_play!!.setImageResource(R.drawable.icon_audio_pause)
    }

    fun release() {
        mediaManager!!.release()
        handler.removeCallbacksAndMessages(null)
    }


}