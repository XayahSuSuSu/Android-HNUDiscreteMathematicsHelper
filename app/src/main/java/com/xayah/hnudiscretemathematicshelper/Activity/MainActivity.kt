package com.xayah.hnudiscretemathematicshelper.Activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.xayah.hnudiscretemathematicshelper.Adapter.TaskAdapter
import com.xayah.hnudiscretemathematicshelper.R
import com.xayah.hnudiscretemathematicshelper.Util.DataUtil
import com.xayah.hnudiscretemathematicshelper.Util.DialogUtil
import com.xayah.hnudiscretemathematicshelper.Util.NetUtil

class MainActivity : AppCompatActivity() {
    private val dialogUtil = DialogUtil(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
//        dialogUtil.createProgressDialog()
    }

    private fun init() {
        val nestedScrollView_main: NestedScrollView = findViewById(R.id.nestedScrollView_main)
        val floatingActionButton_refresh: ExtendedFloatingActionButton =
            findViewById(R.id.floatingActionButton_refresh)
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
}