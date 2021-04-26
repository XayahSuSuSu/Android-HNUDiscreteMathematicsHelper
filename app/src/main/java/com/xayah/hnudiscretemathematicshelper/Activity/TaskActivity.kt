package com.xayah.hnudiscretemathematicshelper.Activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.xayah.hnudiscretemathematicshelper.Adapter.CertainTaskAdapter
import com.xayah.hnudiscretemathematicshelper.R
import com.xayah.hnudiscretemathematicshelper.Util.NetUtil

class TaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        init()
    }

    private fun init() {
        val schoolno = intent.getStringExtra("schoolno")
        val zh = intent.getStringExtra("zh")
        val id = intent.getStringExtra("id")
        val papername = intent.getStringExtra("papername")
        val userAgent = intent.getStringExtra("userAgent")
        val cookie = intent.getStringExtra("cookie")
        val main_textView_taskName: TextView = findViewById(R.id.main_textView_taskName)
        val cond = "schoolno='$schoolno' AND zh='$zh' AND paperplanId=$id"
        Log.d("mTAG", "init: " + cond)
        main_textView_taskName.setText(papername)
        val recyclerView_certainTasks: RecyclerView = findViewById(R.id.recyclerView_certainTasks)
        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView_certainTasks.setLayoutManager(layoutManager)
        recyclerView_certainTasks.overScrollMode = View.OVER_SCROLL_NEVER
        recyclerView_certainTasks.itemAnimator = DefaultItemAnimator()
        Thread {
            val certainTaskList = NetUtil.getCertainTask(
                "studscoredetail",
                "knowpoint,tkno,studans,scorestudnum,teachauditmsg,teachauditmsgqa,studanstext,studreply,datebegin,dateend,testtopic,id",
                cond,
                "tkno",
                userAgent!!,
                cookie!!,
            )
            for (i in certainTaskList) {
                Log.d("mTAG", "遍历答案数组: " + i.certainTaskQuestionClass.qOption)
            }
            runOnUiThread {
                val mCertainTaskAdapter = CertainTaskAdapter(this, certainTaskList)
                recyclerView_certainTasks.adapter = mCertainTaskAdapter
            }
        }.start()


    }
}