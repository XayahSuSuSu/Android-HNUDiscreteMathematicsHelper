package com.xayah.hnudiscretemathematicshelper.Util

import android.util.Log
import com.xayah.hnudiscretemathematicshelper.Class.CertainTaskClass
import com.xayah.hnudiscretemathematicshelper.Class.CertainTaskQuestionClass
import com.xayah.hnudiscretemathematicshelper.Class.TaskClass
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.io.IOException

class NetUtil {

    companion object {

        private val getSchoolnodeptnoApi = "http://120.27.17.78/hnuysh/"

        private val getImageApi = "http://120.27.17.78/hnuysh/getImage.aspx"

        private val loginApi = "http://120.27.17.78/hnuysh/yshverify.aspx"

        private val getTasksApi =
            "http://120.27.17.78/hnuysh/getDataTop1WithFieldListCalField.aspx"

        private val getCertainTaskApi =
            "http://120.27.17.78/hnuysh/getDataTop1WithFieldListNoShow.aspx"

        private val commitAnswerApi =
            "http://120.27.17.78/hnuysh/exeNonQueryUpDateInsertWithPara.aspx"

        private val getIPApi = "http://ip.json-json.com/"


        fun getSchoolnodeptno(userAgent: String): String {
            var returnBody = "none"
            try {
                val okHttpClient = OkHttpClient()
                val mRequest: Request = Request.Builder()
                    .url(getSchoolnodeptnoApi)
                    .addHeader(
                        "Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9"
                    )
                    .addHeader("Connection", "keep-alive")
                    .addHeader("User-Agent", userAgent)
                    .get()
                    .build()
                val mCall: Call = okHttpClient.newCall(mRequest)
                val mResponse: Response = mCall.execute()
                val mResponseBody: String? = mResponse.body?.string()
                if (mResponseBody != null) {
                    val document: Document = Jsoup.parse(mResponseBody)
                    val schoolnodeptnoElement: Element = document.getElementById("schoolnodeptno")
                    val schoolnodeptno = schoolnodeptnoElement.attr("value")
                    returnBody = DataUtil.encodeURI(schoolnodeptno)
                    Log.d("mTAG", "getSchoolnodeptno: " + schoolnodeptnoElement.attr("value"))
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return returnBody
        } // get获取schoolnodeptno

        fun getImage(userAgent: String): JSONObject {
            val returnJSON = JSONObject()

            val returnBody: String
            try {
                val okHttpClient = OkHttpClient()
                val mRequestBody: RequestBody = FormBody.Builder()
                    .add("oldImgId", "1")
                    .build()
                val mRequest: Request = Request.Builder()
                    .url(getImageApi)
                    .addHeader("Accept", "*/*")
                    .addHeader("Referer", "http://120.27.17.78/hnuysh/")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("User-Agent", userAgent)
                    .post(mRequestBody)
                    .build()
                val mCall: Call = okHttpClient.newCall(mRequest)
                val mResponse: Response = mCall.execute()
                val SessionId =
                    mResponse.headers("Set-Cookie").toString().replace("[", "").replace("]", "")
                        .split(";")[0]
                Log.d("mTAG", "getImage: " + SessionId)
                val mResponseBody: String? = mResponse.body?.string()
                if (mResponseBody != null) {
                    returnBody = mResponseBody
                    Log.d("mTAG", "getImage: " + returnBody)
                    try {
                        val jsonObject = JSONObject(returnBody)
                        val message = jsonObject.getString("message")
                        val imageUrl = "http://120.27.17.78/hnuysh/images/$message"
                        returnJSON.put("url", imageUrl)
                        returnJSON.put("SessionId", SessionId)
                        returnJSON.put("verifycodeint", message.replace(".png", ""))
                        Log.d("mTAG", "getImage: " + imageUrl)
                        Log.d("mTAG", "getImage: " + SessionId)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return returnJSON
        } // post获取验证码地址

        fun login(
            account: String,
            password: String,
            verifycode: String,
            verifycodeint: String,
            schoolnodeptno: String,
            userAgent: String,
            cookie: String
        ): String {
            var returnBody = "none"
            try {
                val okHttpClient = OkHttpClient()
                val mRequestBody: RequestBody = FormBody.Builder()
                    .add("account", account)
                    .add("password", password)
                    .add("verifycode", verifycode)
                    .add("verifycodeint", verifycodeint)
                    .add("schoolnodeptno", schoolnodeptno)
                    .build()
                val mRequest: Request = Request.Builder()
                    .url(loginApi)
                    .addHeader("Accept", "*/*")
                    .addHeader("Referer", "http://120.27.17.78/hnuysh/")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("User-Agent", userAgent)
                    .addHeader("Cookie", cookie)
                    .post(mRequestBody)
                    .build()
                val mCall: Call = okHttpClient.newCall(mRequest)
                val mResponse: Response = mCall.execute()
                val mResponseBody: String? = mResponse.body?.string()
                if (mResponseBody != null) {
                    returnBody = mResponseBody
                    Log.d("mTAG", "getImage: " + returnBody)
                    try {
                        val jsonObject = JSONObject(returnBody)
                        val message = jsonObject.getString("message")
                        returnBody = "http://120.27.17.78/hnuysh/images/$message"
                        Log.d("mTAG", "getImage: " + returnBody)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return returnBody
        } // post登录

        fun getTasks(
            zh: String,
            username: String,
            tableName: String,
            totalShowNum: String,
            pageIndex: String,
            fieldList: String,
            cond: String,
            orderByField: String,
            userAgent: String,
            cookie: String
        ): MutableList<TaskClass> {
            val taskList = mutableListOf<TaskClass>()
            try {
                val okHttpClient = OkHttpClient()
                val mRequestBody: RequestBody = FormBody.Builder()
                    .add("zh", zh)
                    .add("username", username)
                    .add("tableName", tableName)
                    .add("totalShowNum", totalShowNum)
                    .add("pageIndex", pageIndex)
                    .add("fieldList", fieldList)
                    .add("cond", cond)
                    .add("orderByField", orderByField)
                    .build()
                val mRequest: Request = Request.Builder()
                    .url(getTasksApi)
                    .addHeader("Accept", "*/*")
                    .addHeader("Referer", "http://120.27.17.78/hnuysh/homestudent.htm")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("User-Agent", userAgent)
                    .addHeader("Cookie", cookie)
                    .post(mRequestBody)
                    .build()
                val mCall: Call = okHttpClient.newCall(mRequest)
                val mResponse: Response = mCall.execute()
                val mResponseBody: String? = mResponse.body?.string()
                if (mResponseBody != null) {
                    val returnBody = mResponseBody
                    Log.d("mTAG", "getTasks: " + returnBody)

                    val dataArr = returnBody.split("`")
                    val nCol = dataArr[0] //字段数
                    val nRecordCount = dataArr[1] //总行数
                    for (i in 0 until ((dataArr.size - 2) / nCol.toInt())) {
                        val taskClass = TaskClass()
                        taskClass.papername = dataArr[i * nCol.toInt() + 2]
                        taskClass.datecreate = dataArr[i * nCol.toInt() + 3]
                        taskClass.schoolno = dataArr[i * nCol.toInt() + 4]
                        taskClass.schoolname = dataArr[i * nCol.toInt() + 5]
                        taskClass.zh = dataArr[i * nCol.toInt() + 6]
                        taskClass.username = dataArr[i * nCol.toInt() + 7]
                        taskClass.courseno = dataArr[i * nCol.toInt() + 8]
                        taskClass.coursename = dataArr[i * nCol.toInt() + 9]
                        taskClass.classname = dataArr[i * nCol.toInt() + 10]
                        taskClass.studansflag = dataArr[i * nCol.toInt() + 11]
                        taskClass.datebegin = dataArr[i * nCol.toInt() + 12]
                        taskClass.dateend = dataArr[i * nCol.toInt() + 13]
                        taskClass.examperoid = dataArr[i * nCol.toInt() + 14]
                        taskClass.id = dataArr[i * nCol.toInt() + 15]
                        taskClass.groupType =
                            DataUtil.isProperTime(taskClass.datebegin, taskClass.dateend)
                        taskList.add(taskClass)
                        Log.d("mTAG", "getTasks: " + taskClass.papername)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return taskList
        } // post获取任务

        fun getCertainTask(
            tableName: String,
            fieldList: String,
            cond: String,
            orderByField: String,
            userAgent: String,
            cookie: String
        ): MutableList<CertainTaskClass> {
            val certainTaskList = mutableListOf<CertainTaskClass>()
            try {
                val okHttpClient = OkHttpClient()
                val mRequestBody: RequestBody = FormBody.Builder()
                    .add("tableName", tableName)
                    .add("fieldList", fieldList)
                    .add("cond", cond)
                    .add("orderByField", orderByField)
                    .build()
                val mRequest: Request = Request.Builder()
                    .url(getCertainTaskApi)
                    .addHeader("Accept", "*/*")
                    .addHeader("Referer", "http://120.27.17.78/hnuysh/homestudent.htm")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("User-Agent", userAgent)
                    .addHeader("Cookie", cookie)
                    .post(mRequestBody)
                    .build()
                val mCall: Call = okHttpClient.newCall(mRequest)
                val mResponse: Response = mCall.execute()
                val mResponseBody: String? = mResponse.body?.string()
                if (mResponseBody != null) {
                    Log.d("mTAG", "getCertainTask: $mResponseBody")
                    val dataArr = mResponseBody.split("`")
                    Log.d("mTAG", "getCertainTask: " + dataArr.size.toString())

                    val nCol = dataArr[0] //未知数据
                    for (i in 0 until ((dataArr.size - 1) / 12)) {
                        val certainTaskClass = CertainTaskClass()
                        certainTaskClass.knowpoint = dataArr[i * 12 + 1]
                        certainTaskClass.tkno = dataArr[i * 12 + 2]
                        certainTaskClass.studans = dataArr[i * 12 + 3]
                        certainTaskClass.scorestudnum = dataArr[i * 12 + 4]
                        certainTaskClass.teachauditmsg = dataArr[i * 12 + 5]
                        certainTaskClass.teachauditmsgqa = dataArr[i * 12 + 6]
                        certainTaskClass.studanstext = dataArr[i * 12 + 7]
                        certainTaskClass.studreply = dataArr[i * 12 + 8]
                        certainTaskClass.datebegin = dataArr[i * 12 + 9]
                        certainTaskClass.dateend = dataArr[i * 12 + 10]
                        certainTaskClass.testtopic = dataArr[i * 12 + 11]
                        certainTaskClass.id = dataArr[i * 12 + 12]
                        Log.d("mTAG", "getCertainTask: " + certainTaskClass.testtopic)
                        val testtopicArr = certainTaskClass.testtopic.split(",")
                        certainTaskClass.certainTaskQuestionClass = CertainTaskQuestionClass()
                        certainTaskClass.certainTaskQuestionClass.qId = testtopicArr[0]
                        certainTaskClass.certainTaskQuestionClass.qPoint = testtopicArr[1]
                        certainTaskClass.certainTaskQuestionClass.qTitle = testtopicArr[2]
                        certainTaskClass.certainTaskQuestionClass.qOption = mutableListOf()
                        certainTaskClass.certainTaskQuestionClass.qOption.add(testtopicArr[3])
                        certainTaskClass.certainTaskQuestionClass.qOption.add(testtopicArr[4])
                        certainTaskClass.certainTaskQuestionClass.qOption.add(testtopicArr[5])
                        certainTaskClass.certainTaskQuestionClass.qOption.add(testtopicArr[6])
                        certainTaskClass.certainTaskQuestionClass.qOption.add(testtopicArr[7])
                        certainTaskClass.certainTaskQuestionClass.qOption.add(testtopicArr[8])
                        certainTaskClass.certainTaskQuestionClass.qAnswer = testtopicArr[9]
                        certainTaskClass.certainTaskQuestionClass.qExplain = testtopicArr[10]
                        certainTaskClass.certainTaskQuestionClass.qDegree = testtopicArr[11]
                        certainTaskClass.isFirst = true
                        certainTaskList.add(certainTaskClass)
                        Log.d("mTAG", "getCertainTask: " + certainTaskClass.studans)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return certainTaskList
        } // post获取特定任务

        fun getIP(): String {
            var returnBody = "none"
            try {
                val okHttpClient = OkHttpClient()
                val mRequest: Request = Request.Builder()
                    .url(getIPApi)
                    .get()
                    .build()
                val mCall: Call = okHttpClient.newCall(mRequest)
                val mResponse: Response = mCall.execute()
                val mResponseBody: String? = mResponse.body?.string()
                if (mResponseBody != null) {
                    Log.d("mTAG", "getIP: $mResponseBody")
                    returnBody = mResponseBody
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return returnBody
        } // get获取IP地址


        fun commitAnswer(
            sqlState: String,
            userAgent: String,
            cookie: String
        ): String {
            var mResponseBody = ""
            try {
                val okHttpClient = OkHttpClient()
                val mRequestBody: RequestBody = FormBody.Builder()
                    .add("sqlState", sqlState)
                    .build()
                val mRequest: Request = Request.Builder()
                    .url(commitAnswerApi)
                    .addHeader("Accept", "*/*")
                    .addHeader("Referer", "http://120.27.17.78/hnuysh/homestudent.htm")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("User-Agent", userAgent)
                    .addHeader("Cookie", cookie)
                    .post(mRequestBody)
                    .build()
                val mCall: Call = okHttpClient.newCall(mRequest)
                val mResponse: Response = mCall.execute()
                mResponseBody = mResponse.body?.string().toString()
                Log.d("mTAG", "commitAnswer: $mResponseBody")
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return mResponseBody
        } // post提交答案
    }
}