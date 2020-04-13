package com.she.music_play_library.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatSeekBar

class CustomSeekBar : AppCompatSeekBar {
    private var isCanDrag = true

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * 设置SeekBar是否可以拖动
     * @param isCanDrag
     */
    fun isCanDrag(isCanDrag: Boolean) {
        this.isCanDrag = isCanDrag
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (isCanDrag) {
            super.onTouchEvent(event)
        } else false
    }
}