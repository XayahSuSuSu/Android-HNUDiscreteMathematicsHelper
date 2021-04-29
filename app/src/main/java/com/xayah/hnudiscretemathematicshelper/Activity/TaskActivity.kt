package com.xayah.hnudiscretemathematicshelper.Activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.xayah.hnudiscretemathematicshelper.Adapter.CertainTaskAdapter
import com.xayah.hnudiscretemathematicshelper.Class.CertainTaskClass
import com.xayah.hnudiscretemathematicshelper.R
import com.xayah.hnudiscretemathematicshelper.Util.DataUtil
import com.xayah.hnudiscretemathematicshelper.Util.DataUtil.Companion.modifyAnswer
import com.xayah.hnudiscretemathematicshelper.Util.DialogUtil
import com.xayah.hnudiscretemathematicshelper.Util.NetUtil
import com.xayah.hnudiscretemathematicshelper.Util.NetUtil.Companion.commitAnswer
import com.xayah.hnudiscretemathematicshelper.Util.NetUtil.Companion.getIP


class TaskActivity : AppCompatActivity() {
    val dialogUtil = DialogUtil(this)
    lateinit var task_textView_timeNum: TextView
    lateinit var mContext: Context
    lateinit var certainTaskList: MutableList<CertainTaskClass>
    lateinit var examperoid: String
    lateinit var isOutDate: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        mContext = this
        init()
        updateSurplusTime()
    }


    private fun init() {
        task_textView_timeNum = findViewById(R.id.task_textView_timeNum)
        val task_textView_scoreNum: TextView = findViewById(R.id.task_textView_scoreNum)
        val schoolno = intent.getStringExtra("schoolno")
        val zh = intent.getStringExtra("zh")
        val id = intent.getStringExtra("id")
        val papername = intent.getStringExtra("papername")
        examperoid = intent.getStringExtra("examperoid").toString()
        val userAgent = intent.getStringExtra("userAgent")
        val cookie = intent.getStringExtra("cookie")
        isOutDate = intent.getStringExtra("isOutDate").toString()
        val task_textView_taskName: TextView = findViewById(R.id.task_textView_taskName)
        val cond = "schoolno='$schoolno' AND zh='$zh' AND paperplanId=$id"
        Log.d("mTAG", "init: " + cond)
        task_textView_taskName.text = papername
        task_textView_taskName.setEllipsize(TextUtils.TruncateAt.MARQUEE)
        task_textView_taskName.setSingleLine(true)
        task_textView_taskName.setSelected(true)
        task_textView_taskName.setFocusable(true)
        task_textView_taskName.setFocusableInTouchMode(true)

        val recyclerView_certainTasks: RecyclerView = findViewById(R.id.recyclerView_certainTasks)
        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView_certainTasks.setLayoutManager(layoutManager)
        recyclerView_certainTasks.overScrollMode = View.OVER_SCROLL_NEVER
        recyclerView_certainTasks.itemAnimator = DefaultItemAnimator()
        certainTaskList = mutableListOf<CertainTaskClass>()
        Thread {
            certainTaskList = NetUtil.getCertainTask(
                "studscoredetail",
                "knowpoint,tkno,studans,scorestudnum,teachauditmsg,teachauditmsgqa,studanstext,studreply,datebegin,dateend,testtopic,id",
                cond,
                "tkno",
                userAgent!!,
                cookie!!,
            )
            task_textView_scoreNum.setText(DataUtil.getScore(certainTaskList))
            for (i in certainTaskList) {
                Log.d("mTAG", "遍历答案数组: " + i.certainTaskQuestionClass.qOption)
                if (i.certainTaskQuestionClass.qTitle.contains("<img src=")) {
                    DataUtil.getImageUrl(i.certainTaskQuestionClass.qTitle)
//                    val dialogUtil = DialogUtil(this)
//                    runOnUiThread {
//                        dialogUtil.createPositiveButtonDialog("暂不支持客观题!", "好的", {finish()})
//                    }
//                    break
                }
            }
            runOnUiThread {
                val mCertainTaskAdapter = CertainTaskAdapter(this, certainTaskList)
                recyclerView_certainTasks.adapter = mCertainTaskAdapter
            }
        }.start()

        val floatingActionButton_upload: ExtendedFloatingActionButton =
            findViewById(R.id.floatingActionButton_upload)
        floatingActionButton_upload.setOnClickListener {
            if (task_textView_timeNum.text.toString() == "已截止") {
                dialogUtil.createPositiveButtonDialog("试卷已经截止,仅可查看试卷!", "好的") {}
            } else if (certainTaskList[0].certainTaskQuestionClass.qAnswer.toInt() >= 10) {
                dialogUtil.createPositiveButtonDialog("暂不支持非客观题作答!", "好的") {}
            } else {
                Thread {
                    val mIP = getIP()
                    val sqlState = modifyAnswer(mIP, certainTaskList)
                    Log.d("mTAG", "modifyAnswer: $sqlState")
                    val mReturn = commitAnswer(sqlState, userAgent!!, cookie!!)
                    runOnUiThread {
                        dialogUtil.createPositiveButtonDialog(
                            mReturn,
                            "好的"
                        ) { finish() }
                    }

                }.start()

            }
        }

    }

    private fun updateSurplusTime() {
        val mHandler = Handler(Looper.getMainLooper())
        if (isOutDate == "outDate") {
            task_textView_timeNum.text = "已截止"
            task_textView_timeNum.setTextColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.mRed
                )
            )
        } else {
            task_textView_timeNum.text = examperoid + ":00"
            Log.d("mTAG", "examperoid: " + examperoid)

            var timeSurplus = ""
            val mTimeCounterRunnable: Runnable = object : Runnable {
                override fun run() { //轮询
                    if (timeSurplus == "0:0") {
                        timeSurplus = "已截止"
                        task_textView_timeNum.setTextColor(
                            ContextCompat.getColor(
                                mContext,
                                R.color.mRed
                            )
                        )
                    } else if (timeSurplus == "已截止") {
                        mHandler.removeCallbacks(this)
                    } else {
                        timeSurplus = DataUtil.getSurplusTime(task_textView_timeNum.text.toString())
                    }
                    runOnUiThread {
                        task_textView_timeNum.setText(timeSurplus)
                    }
                    mHandler.postDelayed(this, 1 * 1000)
                }
            }
            mHandler.postDelayed(mTimeCounterRunnable, 1 * 1000)
        }

    }

}