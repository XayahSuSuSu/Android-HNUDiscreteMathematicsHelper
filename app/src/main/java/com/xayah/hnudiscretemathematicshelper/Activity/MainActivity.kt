package com.xayah.hnudiscretemathematicshelper.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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

class MainActivity : AppCompatActivity() {
    // -------------------Component-------------------
    lateinit var floatingActionButton_refresh: ExtendedFloatingActionButton
    lateinit var main_navigationView: NavigationView
    lateinit var main_drawer_layout: DrawerLayout
    lateinit var navigationview_head_textView_version: TextView
    lateinit var main_imageView_menu: ImageButton
    lateinit var main_textView_name: TextView
    lateinit var recyclerView_tasks: RecyclerView

    // -------------------Utils-------------------
    private val dialogUtil = DialogUtil(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindView() // 绑定组件
        init() // 初始化
        setListener() // 设置监听器
    }

    private fun bindView() {
        floatingActionButton_refresh =
            findViewById(R.id.floatingActionButton_refresh)
        main_navigationView = findViewById(R.id.main_navigationView)
        main_drawer_layout = findViewById(R.id.main_drawer_layout)
        val navigationview_head = main_navigationView.getHeaderView(0)
        navigationview_head_textView_version =
            navigationview_head.findViewById(R.id.navigationview_head_textView_version)
        main_imageView_menu = findViewById(R.id.main_imageView_menu)
        main_textView_name = findViewById(R.id.main_textView_name)
        recyclerView_tasks = findViewById(R.id.recyclerView_tasks)
    } // 绑定组件

    private fun setListener() {
        // 侧滑栏子项单击事件
        main_navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_website_system -> {
                    val uri = Uri.parse("http://120.27.17.78/hnuysh/")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
                R.id.item_website_AcmeZone -> {
                    val uri = Uri.parse("http://acmezone.top/")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
                R.id.item_about_app -> {
                    val uri =
                        Uri.parse("https://github.com/XayahSuSuSu/Android-HNUDiscreteMathematicsHelper")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
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
        // 左上角菜单按钮单击事件
        main_imageView_menu.setOnClickListener {
            main_drawer_layout.openDrawer(main_navigationView)
        }
    } // 设置监听器

    private fun init() {
        // 侧滑栏版本获取
        navigationview_head_textView_version.text = "HNU离散数学助手 v" + DataUtil.getVersion(this)
        // 获取从LoginActivity的intent传入的数据
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
        // 获取UserAgent和cookie
        val userAgent = intent.getStringExtra("userAgent")
        val cookie = intent.getStringExtra("cookie")
        // 初始化recyclerView
        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView_tasks.setLayoutManager(layoutManager)
        recyclerView_tasks.overScrollMode = View.OVER_SCROLL_NEVER
        recyclerView_tasks.itemAnimator = DefaultItemAnimator()
        recyclerView_tasks.isNestedScrollingEnabled = false
        // 获取任务信息
        dialogUtil.createProgressDialog{
            Thread {
                try {
                    var taskList = NetUtil.getTasks(
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
                    taskList = DataUtil.sortTasks(taskList)
                    runOnUiThread {
                        it.dismiss()
                        val mTaskAdapter = TaskAdapter(this, taskList, zh, userAgent, cookie)
                        recyclerView_tasks.adapter = mTaskAdapter
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    it.dismiss()
                }
            }.start()
        }
        // 展示账号信息
        main_textView_name.text = username
    } // 初始化
}