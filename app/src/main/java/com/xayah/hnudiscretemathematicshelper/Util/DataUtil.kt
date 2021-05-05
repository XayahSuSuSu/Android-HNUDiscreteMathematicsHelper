package com.xayah.hnudiscretemathematicshelper.Util

import android.content.Context
import android.util.Log
import com.xayah.hnudiscretemathematicshelper.Class.CertainTaskClass
import com.xayah.hnudiscretemathematicshelper.Class.TaskClass
import org.json.JSONObject
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

class DataUtil {
    companion object {
        fun encodeURI(mStr: String): String {
            val mReturn = URLEncoder.encode(mStr, "UTF-8")
            return mReturn
        } // URI编码

        fun parseTime(mTimeStr: String): JSONObject {
            val mJSONObject = JSONObject()
            val year = mTimeStr.substring(IntRange(0, 3))
            val month = mTimeStr.substring(IntRange(4, 5))
            val day = mTimeStr.substring(IntRange(6, 7))
            val hour = mTimeStr.substring(IntRange(8, 9))
            val minute = mTimeStr.substring(IntRange(10, 11))
            val date = "$year.$month.$day"
            val time = "$hour:$minute"
            mJSONObject.put("year", year)
            mJSONObject.put("month", month)
            mJSONObject.put("day", day)
            mJSONObject.put("hour", hour)
            mJSONObject.put("minute", minute)
            mJSONObject.put("date", date)
            mJSONObject.put("time", time)
            return mJSONObject
        } // 解析时间

        fun getVerifyCode(verifycodeint: String): String {
            var mReturn = ""
            when (verifycodeint) {
                "1" -> {
                    mReturn = "336K"
                }
                "2" -> {
                    mReturn = "59ZP"
                }
                "3" -> {
                    mReturn = "6KKD"
                }
                "4" -> {
                    mReturn = "7E3V"
                }
                "5" -> {
                    mReturn = "APD2"
                }
                "6" -> {
                    mReturn = "C6DN"
                }
                "7" -> {
                    mReturn = "CVWT"
                }
                "8" -> {
                    mReturn = "ECRR"
                }
                "9" -> {
                    mReturn = "GJ5Y"
                }
                "10" -> {
                    mReturn = "GV2R"
                }
                "11" -> {
                    mReturn = "GZPW"
                }
                "12" -> {
                    mReturn = "H9VJ"
                }
                "13" -> {
                    mReturn = "HEM2"
                }
                "14" -> {
                    mReturn = "HJQ4"
                }
                "15" -> {
                    mReturn = "HS38"
                }
                "16" -> {
                    mReturn = "JHDT"
                }
                "17" -> {
                    mReturn = "MX5C"
                }
                "18" -> {
                    mReturn = "N6JR"
                }
                "19" -> {
                    mReturn = "PH7H"
                }
                "20" -> {
                    mReturn = "PRFP"
                }
                "21" -> {
                    mReturn = "Q4YV"
                }
                "22" -> {
                    mReturn = "Q5EP"
                }
                "23" -> {
                    mReturn = "RX8R"
                }
                "24" -> {
                    mReturn = "T3GK"
                }
                "25" -> {
                    mReturn = "T8TQ"
                }
                "26" -> {
                    mReturn = "TD6H"
                }
                "27" -> {
                    mReturn = "TGY3"
                }
                "28" -> {
                    mReturn = "UUKT"
                }
                "29" -> {
                    mReturn = "VF6H"
                }
                "30" -> {
                    mReturn = "VTF9"
                }
                "31" -> {
                    mReturn = "VX4N"
                }
                "32" -> {
                    mReturn = "WPVV"
                }
                "33" -> {
                    mReturn = "XAK6"
                }
                "34" -> {
                    mReturn = "XTJ6"
                }
                "35" -> {
                    mReturn = "XWNG"
                }
                "36" -> {
                    mReturn = "YU5H"
                }
                "37" -> {
                    mReturn = "YWUX"
                }
                "38" -> {
                    mReturn = "ZFTF"
                }
                "39" -> {
                    mReturn = "ZJGK"
                }
                "40" -> {
                    mReturn = "ZQ6P"
                }
            }
            return mReturn
        } // 服务器上固定40个验证码图片，没有必要接入OCR，直接判断就可以了

        fun int2Char(mInt: Int): Char {
            return (mInt + 65).toChar()
        } // 索引转选项标号

        fun char2Int(mChar: Char): Int {
            return mChar.toInt() - 65
        } // 选项标号转索引

        fun addAnswer(mChar: Char, mStr: String): String {
            if (!mStr.contains(mChar)) {
                var mReturn = mStr + mChar
                val mReturnArr = mutableListOf<Char>()
                for (i in mReturn)
                    mReturnArr.add(i)
                mReturnArr.sort()
                mReturn = ""
                for (i in mReturnArr)
                    mReturn += i
                Log.d("mTAG", "mSort: $mReturn")
                mReturn = mReturn.replace("-", "")
                return mReturn
            }
            return mStr
        } // 从答案字符串中按顺序添加答案

        fun deleteAnswer(mChar: Char, mStr: String): String {
            val mReturn: String
            if (mStr.contains(mChar)) {
                mReturn = mStr.replace(mChar.toString(), "").replace("-", "")
                return mReturn
            }
            return mStr
        } // 从答案字符串中按顺序删除答案

        fun getTime(): String {
            val simpleDateFormat = SimpleDateFormat("yyyyMMddHHmmss")
            val mTime =
                simpleDateFormat.format(Date(System.currentTimeMillis().toString().toLong()))
            Log.d("mTAG", "mTime: " + mTime)
            return mTime
        } // 获取当前时间

        fun modifyAnswer(mIP: String, mCertainTaskList: MutableList<CertainTaskClass>): String {
            var sqlState = ""
            Log.d("mTAG", "mIP: " + mIP)
            for ((index, i) in mCertainTaskList.withIndex()) {
                if (index == 0) {
                    sqlState =
                        "update`c`studscoredetail`studans`c`" + i.studans + "`studanstext`c`" + "-" + "`scorestudnum`n`(case  when  ( len(rtrim(cast(stanardans as varchar(max))))>0 AND cast(stanardans as varchar(max))='" + i.studans + "') then scorestandard when  ( len(rtrim(cast(stanardans as varchar(max))))>0 AND RTRIM(cast(stanardans as varchar(max)))<>'" + i.studans + "') then 0 else scorestudnum  end)" + "`datestudsubmit`c`" + getTime() + "`ipstud`c`" + mIP + "`where`1`id='" + i.id + "'"
                } else {
                    sqlState += "~yshsxzyqy~update`c`studscoredetail`studans`c`" + i.studans + "`studanstext`c`" + "-" + "`scorestudnum`n`(case  when  ( len(rtrim(cast(stanardans as varchar(max))))>0 AND cast(stanardans as varchar(max))='" + i.studans + "') then scorestandard when  ( len(rtrim(cast(stanardans as varchar(max))))>0 AND RTRIM(cast(stanardans as varchar(max)))<>'" + i.studans + "') then 0 else scorestudnum  end)" + "`datestudsubmit`c`" + getTime() + "`ipstud`c`" + mIP + "`where`1`id='" + i.id + "'"
                }
            }
            return sqlState
        } // 格式化答案提交表单

        fun isProperTime(mStartTime: String, mEndTime: String): String {
            if (mStartTime.trim().toLong() > getTime().trim().toLong()) {
                return "aheadDate"
            } else if (getTime().trim()
                    .toLong() > mEndTime.trim().toLong()
            ) {
                return "outDate"
            } else {
                return "properDate"
            }
        } // 判断时间

        fun getImageUrl(mQTitle: String): MutableList<String> {
            // 当传进来的Url太奇怪时，使用StringIndexOutOfBoundsException处理异常
            val mUrlList = mutableListOf<String>()
            try {
                val mQTitleTmp = mQTitle.replace("</img>", "")
                Log.d("mTAG", "mQTitleTmp: " + mQTitleTmp)
                val mUrls =
                    mQTitleTmp.substring(
                        mQTitleTmp.indexOf("<img src="),
                        mQTitleTmp.lastIndexOf(">") + 1
                    ).trim()
                Log.d("mTAG", "mUrls: " + mUrls)
                if (mUrls.trim() == mQTitleTmp) {
                    mUrlList.add("")
                    val mReturn =
                        mUrls.replace("‘", "\"").replace("\" border=\"0\">", "")
                            .split("<img src=\"")
                    Log.d("mTAG", "mReturn: " + mReturn)
                    for (i in mReturn) {
                        if (!i.isEmpty())
                            mUrlList.add(i)
                    }
                    mUrlList.add("")
                } else {
                    val mTextTitleLeft =
                        mQTitleTmp.substring(0, mQTitleTmp.indexOf("<img src=")).trim()
                    var mTextTitleRight = ""
                    if (mQTitleTmp.lastIndexOf(">") != mQTitleTmp.lastIndex) {
                        mTextTitleRight =
                            mQTitleTmp.substring(
                                mQTitleTmp.lastIndexOf(">") + 1,
                                mQTitleTmp.lastIndex
                            )
                                .trim()
                    }

                    mUrlList.add(mTextTitleLeft)
                    Log.d("mTAG", "mTextTitleLeft: $mTextTitleLeft")
                    Log.d("mTAG", "mTextTitleRight: $mTextTitleRight")
                    val mReturn =
                        mUrls.replace("‘", "\"").replace("\" border=\"0\">", "")
                            .split("<img src=\"")
                    Log.d("mTAG", "mReturn: $mReturn")
                    for (i in mReturn) {
                        if (!i.isEmpty())
                            mUrlList.add(i)
                    }
                    mUrlList.add(mTextTitleRight)
                }
            } catch (e: StringIndexOutOfBoundsException) {
                e.printStackTrace()
            }
            return mUrlList
        } // 解析图片链接

        fun getVersion(mContext: Context): String? {
            return try {
                val manager = mContext.packageManager
                val info = manager.getPackageInfo(mContext.packageName, 0)
                val version = info.versionName
                version
            } catch (e: Exception) {
                e.printStackTrace()
                "无法获取到版本号"
            }
        } // 获取当前版本号

        fun getSurplusTime(mTime: String): String {
            // Format - 15:00
            val time = mTime.split(":").toMutableList()
            if (time[1].toInt() != 0)
                time[1] = (time[1].toInt() - 1).toString()
            else if (time[1].toInt() == 0 && time[0].toInt() == 0) {
                return "0:0"
            } else {
                time[0] = (time[0].toInt() - 1).toString()
                time[1] = "59"
            }
            return time[0] + ":" + time[1]
        } // 计算剩余时间

        fun getScore(certainTaskList: MutableList<CertainTaskClass>): String {
            var mScore = 0
            // 当试卷到期前没有提交过时，scorestudnum值为-,用NumberFormatException处理异常
            try {
                Log.d("mTAG", "getScore()")
                for (i in certainTaskList) {
                    Log.d("mTAG", "i.scorestudnum: " + i.scorestudnum)
                    mScore += i.scorestudnum.toInt()
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }

            return mScore.toString()
        } // 计算分数

        fun sortTasks(taskClass: MutableList<TaskClass>): MutableList<TaskClass> {
            val aheadDateTaskClass = mutableListOf<TaskClass>()
            val properDateTaskClass = mutableListOf<TaskClass>()
            val outDateTaskClass = mutableListOf<TaskClass>()
            for (i in taskClass) {
                if (i.groupType == "aheadDate") {
                    aheadDateTaskClass.add(i)
                } else if (i.groupType == "properDate") {
                    properDateTaskClass.add(i)
                } else {
                    outDateTaskClass.add(i)
                }
            }
            val mTaskClass = mutableListOf<TaskClass>()
            for (i in aheadDateTaskClass) {
                mTaskClass.add(i)
            }
            for (i in properDateTaskClass) {
                mTaskClass.add(i)
            }
            for (i in outDateTaskClass) {
                mTaskClass.add(i)
            }
            return mTaskClass
        } // 按试卷状态重新排序

        fun isTheFirstTask(index: Int, taskClassList: MutableList<TaskClass>): Boolean {
            if (index == 0) {
                return true
            } else {
                if (taskClassList[index].groupType != taskClassList[index - 1].groupType)
                    return true
            }
            return false
        } // 判断是否为该状态试卷的第一个任务，以添加分类标签
    }
}