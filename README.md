# Music
#前言，最近项目需要，写了一个基础的音频播放功能。包括简单的拖动SeekBar快进，暂停播放等
如图所示：
![AA.gif](https://upload-images.jianshu.io/upload_images/21550626-7d4b86c8d6ad6be4.gif?imageMogr2/auto-orient/strip)

使用方法：

Add it in your root build.gradle at the end of repositories:
```
allprojects {
	repositories {
		...
		maven { url 'https://www.jitpack.io' }
	}
}
```
Step 2. Add the dependency
```
dependencies {
	implementation 'com.github.xinyitiandi:Music:V1.0.0'
}

```
Step 3：在代码中使用
这里以写的一个布局文件为例，当然也可以自定义布局：
```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/color_46413D"
    tools:context=".MainActivity">

    <ImageView
        android:id="@+id/iv_pause_or_play"
        android:layout_width="50dp"
        android:layout_height="50dp"
        android:layout_gravity="center_vertical"
        android:src="@drawable/icon_audio_pause"
        android:layout_alignParentBottom="true"
        android:layout_centerHorizontal="true"
        android:layout_marginBottom="30dp"/>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="45dp"
        android:layout_marginLeft="15dp"
        android:layout_marginRight="15dp"
        android:layout_marginBottom="10dp"
        android:orientation="horizontal"
        android:layout_above="@id/iv_pause_or_play">

        <TextView
            android:id="@+id/tv_current_time"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center_vertical"
            android:layout_marginLeft="20dp"
            android:maxLines="1"
            android:text="00:00"
            android:textColor="@color/color_white"
            android:textSize="15sp" />

        <com.she.music_play_library.custom.CustomSeekBar
            android:id="@+id/seekBar_audio"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_gravity="center_vertical"
            android:layout_marginLeft="5dp"
            android:layout_weight="1"
            android:maxHeight="1dp"
            android:progressDrawable="@drawable/seekbar_bg_color"
            android:thumb="@drawable/seek_bar_thumb" />

        <TextView
            android:id="@+id/tv_all_time"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center_vertical"
            android:layout_marginLeft="5dp"
            android:maxLines="1"
            android:text="00:00"
            android:textColor="@color/color_white"
            android:textSize="15sp" />

    </LinearLayout>

</RelativeLayout>
```
#设置播放源
```
    private var audioHelper:AudioHelper?=null
    private var mAudioUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAudioUrl="http://www.xxx.mp3"

        audioHelper=AudioHelper.instance
        audioHelper?.init(tv_current_time,tv_all_time,seekBar_audio,iv_pause_or_play,this)//初始化界面的View
        audioHelper?.setAudioUrl(mAudioUrl!!)//设置音乐播放源
    }
```

#在onPause中调用AudioHelper中的 pauseAudio方法
```
    override fun onPause() {
        super.onPause()
        audioHelper?.pauseAudio()//暂停
    }
```
    
#在onDestory中调用AudioHelper中的 release方法
```
    override fun onDestroy() {
        super.onDestroy()
        audioHelper?.release()//退出时候释放资源
    }
```
大功告成，到这里就可以播放音频文件了。写得比较简陋。还有好多待完善的地方，欢迎各位大神多多指正。
