package com.she.music_play_library.util

class AppUtil {
    companion object {
        var lastClickTime:Long=0
        const val SPACE_TIME=500

        /**
         * 将秒数转换为日时分秒，
         * @param second
         * @return
         */
        fun secondToMyTime(second: Int): String? {
            var second = second
            val hours = second / 3600 //转换小时
            second = second % 3600 //剩余秒数
            val minutes = second / 60 //转换分钟
            second = second % 60 //剩余秒数
            var hourStr = ""
            hourStr = if (hours >= 10) {
                hours.toString() + ""
            } else {
                "0$hours"
            }
            var minuteStr = ""
            minuteStr = if (minutes >= 10) {
                minutes.toString() + ""
            } else {
                "0$minutes"
            }
            var secondStr = ""
            secondStr = if (second >= 10) {
                second.toString() + ""
            } else {
                "0$second"
            }
            return if (hours == 0) {
                "$minuteStr:$secondStr"
            } else {
                "$hourStr:$minuteStr:$secondStr"
            }
        }

        /**
         * 判断是否可以二次点击的方法
         *
         * @return
         */
        @Synchronized
        fun isDoubleClick(): Boolean {
            val currentTime = System.currentTimeMillis()
            val isClickAble: Boolean
            isClickAble = if (currentTime - lastClickTime > AppUtil.SPACE_TIME) {
                false
            } else {
                true
            }
            lastClickTime = currentTime
            return isClickAble
        }
    }
}