package com.xayah.hnudiscretemathematicshelper.Util

import org.json.JSONObject
import java.net.URLEncoder

class DataUtil {
    companion object {
        fun encodeURI(mStr: String): String {
            val mReturn = URLEncoder.encode(mStr, "UTF-8")
            return mReturn
        }

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
        }

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
        }

        fun int2Char(mInt: Int): Char {
            return (mInt + 65).toChar()
        }

        fun char2Int(mChar: Char): Int {
            return mChar.toInt() - 65
        }
    }
}