package com.she.music_play_library.manger;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

public class MediaManager {

    private  MediaPlayer mMediaPlayer;
    private  boolean isPause;


    /**
     * 播放视频文件
     * @param filePath
     * @param onCompletionListener
     */
    public  void playSound(String filePath, MediaPlayer.OnCompletionListener onCompletionListener){
        if (mMediaPlayer==null){
            mMediaPlayer=new MediaPlayer();
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                   // Log.e("=======","======onError===="+what);
                    mMediaPlayer.reset();
                    return false;
                }
            });
        }else{
            mMediaPlayer.reset();
        }

        try {
            // 使用系统的媒体音量控制
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setFlags(AudioTrack.PERFORMANCE_MODE_LOW_LATENCY)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                    .build();
            mMediaPlayer.setAudioAttributes(attributes);
            mMediaPlayer.setLooping(false);
            //mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnCompletionListener(onCompletionListener);
            mMediaPlayer.setDataSource(filePath);
            mMediaPlayer.prepareAsync();

            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });

            mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    if (mOnBufferingUpdateListener!=null){
                        mOnBufferingUpdateListener.onBufferingUpdate(mp,percent);
                    }

                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void play(){
        mMediaPlayer.start();
    }

    /**
     * 暂停播放
     */
    public  void pause(){
        if (mMediaPlayer!=null&&mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
           // mMediaPlayer.reset();
            isPause=true;
        }
    }

    /**
     * 恢复播放
     */
    public  void resume(){
        if (mMediaPlayer!=null&&isPause){
            mMediaPlayer.start();
            isPause=false;
        }
    }

    /**
     * 判断当前是否是暂停状态
     * @return
     */
    public boolean isPause(){
        if (mMediaPlayer!=null&&isPause){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 判断当前是否是播放状态
     * @return
     */
    public boolean isPlaying(){
        if (mMediaPlayer!=null&&mMediaPlayer.isPlaying()){
            return true;
        }else{
            return false;
        }
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }

    public MediaPlayer getMediaPlayer(){
        return mMediaPlayer;
    }

    public  void release(){
        if (mMediaPlayer!=null){
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer=null;
        }
    }



    public void setOnBufferingUpdateListener(OnBufferingUpdateListener listener) {
        mOnBufferingUpdateListener = listener;
    }

    private OnBufferingUpdateListener mOnBufferingUpdateListener;

    public interface OnBufferingUpdateListener {
        void onBufferingUpdate(MediaPlayer mp, int percent);
    }
}
