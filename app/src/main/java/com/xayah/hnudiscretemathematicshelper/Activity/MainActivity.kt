package com.xayah.hnudiscretemathematicshelper.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.xayah.hnudiscretemathematicshelper.Adapter.TaskAdapter
import com.xayah.hnudiscretemathematicshelper.R
import com.xayah.hnudiscretemathematicshelper.Util.DataUtil
import com.xayah.hnudiscretemathematicshelper.Util.DialogUtil
import com.xayah.hnudiscretemathematicshelper.Util.NetUtil
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity() {
    private val dialogUtil = DialogUtil(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        checkVersion()
//        dialogUtil.createProgressDialog()
    }

    private fun init() {
        val nestedScrollView_main: NestedScrollView = findViewById(R.id.nestedScrollView_main)
        val floatingActionButton_refresh: ExtendedFloatingActionButton =
            findViewById(R.id.floatingActionButton_refresh)


        //---------------------------------------------------------------------------------------------------
        val main_navigationView: NavigationView = findViewById(R.id.main_navigationView)
        val main_drawer_layout: DrawerLayout = findViewById(R.id.main_drawer_layout)
        val navigationview_head = main_navigationView.getHeaderView(0)
        val navigationview_head_textView_version: TextView =
            navigationview_head.findViewById(R.id.navigationview_head_textView_version)
        navigationview_head_textView_version.text = "HNU离散数学助手 v" + DataUtil.getVersion(this)

        main_navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_website_system -> {
                    val uri = Uri.parse("http://server.wuyou.com.cn/hnuysh/")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
                R.id.item_website_AcmeZone -> {
                    val uri = Uri.parse("http://acmezone.tk/")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
                R.id.item_about_app -> {

                }
                R.id.item_about_author -> {
                    val intent =
                        Intent(this, AboutAuthorActivity::class.java)
                    startActivity(intent)
                }
                R.id.item_quit -> {
                    finish()
                }
                R.id.item_logout -> {
                    finish()
                    val intent =
                        Intent(this, LoginActivity::class.java)
                    intent.putExtra("isLogOut", true)
                    startActivity(intent)
                }
            }
            false
        }

        val main_imageView_menu: ImageButton = findViewById(R.id.main_imageView_menu)
        main_imageView_menu.setOnClickListener {
            main_drawer_layout.openDrawer(main_navigationView)
        }
//---------------------------------------------------------------------------------------------------

//        nestedScrollView_main.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//
////            val contentView = nestedScrollView_main.getChildAt(0)
////            if (scrollY + v.height == contentView.measuredHeight) {
////                floatingActionButton_refresh.visibility = View.GONE
////            } else {
////                floatingActionButton_refresh.visibility = View.VISIBLE
////            }
//            if (scrollY > oldScrollY) {
//                floatingActionButton_refresh.shrink()
//            }
//            Log.d("mTAG", "scrollY: " + scrollY)
//            Log.d("mTAG", "oldScrollY: " + oldScrollY)
//
//        }


        val zh = intent.getStringExtra("zh")
        val rolename = intent.getStringExtra("rolename")
        val username = intent.getStringExtra("username")
        val schoolno = intent.getStringExtra("schoolno")
        val schoolname = intent.getStringExtra("schoolname")
        val ip = intent.getStringExtra("ip")
        var cond = ""
        val paperplannoList = intent.getStringExtra("paperplannoList")
        if (paperplannoList!!.isEmpty()) {
            cond = "id<0"
//            你暂时没有考试任务
        } else {
            val paperplannoListArr = paperplannoList.split(",")
            var paperplanList = ""
            for (i in 0 until paperplannoListArr.size) {
                val tmpArr = paperplannoListArr[i].split("_")
                val tmpTrim = tmpArr[0].trim()
                if (tmpTrim.isNotEmpty())
                    paperplanList += "," + tmpArr[0]
            }
            if (paperplanList.isNotEmpty()) {
                paperplanList = paperplanList.substring(1)
                cond = " (studvisible=1)  AND  (id in ($paperplanList))"
            } else {
                cond = " (not (studvisible is null)) and studvisible=1"
            }
        }

        Log.d("mTAG", "init: " + DataUtil.encodeURI(DataUtil.encodeURI(cond)))
        val userAgent = intent.getStringExtra("userAgent")
        val cookie = intent.getStringExtra("cookie")
        val main_textView_name: TextView = findViewById(R.id.main_textView_name)
        val recyclerView_tasks: RecyclerView = findViewById(R.id.recyclerView_tasks)
        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView_tasks.setLayoutManager(layoutManager)
        recyclerView_tasks.overScrollMode = View.OVER_SCROLL_NEVER
        recyclerView_tasks.itemAnimator = DefaultItemAnimator()
        Thread {
            val taskList = NetUtil.getTasks(
                zh!!,
                "global_chnname",
                "paperplan",
                "0",
                "1",
                "papername,datecreate,schoolno,schoolname,zh,username,courseno,coursename,classname,studansflag,datebegin,dateend,examperoid,id",
                cond,
                "id desc",
                userAgent!!,
                cookie!!
            )
            runOnUiThread {
                val mTaskAdapter = TaskAdapter(this, taskList, zh, userAgent, cookie)
                recyclerView_tasks.adapter = mTaskAdapter
            }
        }.start()
        main_textView_name.setText(username)
    }

    //自动获取更新
    fun checkVersion() {
        Thread {
            val mVersion: String = DataUtil.getVersion(this)!!
            val getTbsUrl =
                "https://service-rw6vse30-1303879152.gz.apigw.tencentcs.com/release/HNUDMHCheckUpdateNodeJS"
            val okHttpClient = OkHttpClient()
            val mRequest: Request = Request.Builder()
                .url(getTbsUrl)
                .addHeader("Cookie", mVersion)
                .get()
                .build()
            val mCall: Call = okHttpClient.newCall(mRequest)
            val mResponse: Response = mCall.execute()
            val mResponseBody: String? = mResponse.body?.string()
            if (mResponseBody != null) {
                val ifTheNewest = Objects.requireNonNull(mResponseBody)
                if (ifTheNewest == "1") {
                    // 已经是最新版本
                } else {
                    try {
                        val jsonObject = JSONObject(ifTheNewest)
                        val newestVersion = jsonObject.getString("newestVersion")
                        val localVersion = jsonObject.getString("localVersion")
                        val downloadUrl = jsonObject.getString("downloadUrl")
                        val title = jsonObject.getString("title")
                        val confirmButton = jsonObject.getString("confirmButton")
                        val cancelButton = jsonObject.getString("cancelButton")
                        val contentSplit = jsonObject.getString("content").split("&").toTypedArray()
                        val content = StringBuilder()
                        for (s in contentSplit) {
                            content.append(s).append("\n")
                        }
                        var showContent = "最新版本：$newestVersion\n"
                        showContent += "当前版本：$localVersion\n\n"
                        showContent += "更新内容：\n$content\n"
                        showContent += "是否立即更新？"
                        Looper.prepare()
                        dialogUtil.createCustomButtonDialog(
                            title,
                            showContent,
                            confirmButton,
                            cancelButton,
                            {
                                val intent = Intent()
                                intent.action = "android.intent.action.VIEW"
                                val content_url = Uri.parse(downloadUrl)
                                intent.data = content_url
                                startActivity(intent)
                            },
                            {})
                        Looper.loop()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
        }.start()
    }
}