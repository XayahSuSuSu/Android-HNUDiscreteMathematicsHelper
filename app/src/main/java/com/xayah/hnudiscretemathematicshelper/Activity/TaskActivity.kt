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
    // -------------------Component-------------------
    lateinit var task_textView_timeNum: TextView
    lateinit var task_textView_scoreNum: TextView
    lateinit var task_textView_taskName: TextView
    lateinit var recyclerView_certainTasks: RecyclerView
    lateinit var floatingActionButton_upload: ExtendedFloatingActionButton

    // -------------------Var-------------------
    lateinit var userAgent: String
    lateinit var cookie: String
    // -------------------Utils-------------------

    val dialogUtil = DialogUtil(this)
    lateinit var mContext: Context
    lateinit var certainTaskList: MutableList<CertainTaskClass>
    lateinit var examperoid: String
    lateinit var isOutDate: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        // ???????????????Context
        mContext = this
        bindView() // ????????????
        init() // ?????????
        updateSurplusTime() // ??????????????????
        setListener() // ???????????????
    }

    private fun bindView() {
        task_textView_timeNum = findViewById(R.id.task_textView_timeNum)
        task_textView_scoreNum = findViewById(R.id.task_textView_scoreNum)
        task_textView_taskName = findViewById(R.id.task_textView_taskName)
        recyclerView_certainTasks = findViewById(R.id.recyclerView_certainTasks)
        floatingActionButton_upload = findViewById(R.id.floatingActionButton_upload)
    } // ????????????

    private fun setListener() {
        // ????????????
        floatingActionButton_upload.setOnClickListener {
            floatingActionButton_uploadOnClickEvent()
        }
    } // ???????????????

    private fun floatingActionButton_uploadOnClickEvent() {
        when {
            task_textView_timeNum.text.toString() == "?????????" -> {
                dialogUtil.createPositiveButtonDialog("??????????????????,??????????????????!", "??????") {}
            }
            certainTaskList[0].certainTaskQuestionClass.qAnswer.toInt() >= 10 -> {
                dialogUtil.createPositiveButtonDialog("??????????????????????????????!", "??????") {}
            }
            else -> {
                dialogUtil.createProgressDialog {
                    Thread {
                        val mIP = getIP()
                        val sqlState = modifyAnswer(mIP, certainTaskList)
                        Log.d("mTAG", "modifyAnswer: $sqlState")
                        val mReturn = commitAnswer(sqlState, userAgent, cookie)
                        runOnUiThread {
                            it.dismiss()
                            dialogUtil.createPositiveButtonDialog(
                                mReturn,
                                "??????"
                            ) { finish() }
                        }
                    }.start()
                }
            }
        }
    }

    private fun init() {
        // ?????????MainActivity???intent???????????????
        val schoolno = intent.getStringExtra("schoolno")
        val zh = intent.getStringExtra("zh")
        val id = intent.getStringExtra("id")
        val papername = intent.getStringExtra("papername")
        examperoid = intent.getStringExtra("examperoid").toString()
        userAgent = intent.getStringExtra("userAgent").toString()
        cookie = intent.getStringExtra("cookie").toString()
        isOutDate = intent.getStringExtra("isOutDate").toString()
        val cond = "schoolno='$schoolno' AND zh='$zh' AND paperplanId=$id"
        Log.d("mTAG", "init: $cond")
        // ??????????????????
        task_textView_taskName.text = papername
        // ?????????????????????
        task_textView_taskName.setEllipsize(TextUtils.TruncateAt.MARQUEE)
        task_textView_taskName.setSingleLine(true)
        task_textView_taskName.setSelected(true)
        task_textView_taskName.setFocusable(true)
        task_textView_taskName.setFocusableInTouchMode(true)
        // ?????????recyclerView
        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView_certainTasks.setLayoutManager(layoutManager)
        recyclerView_certainTasks.overScrollMode = View.OVER_SCROLL_NEVER
        recyclerView_certainTasks.itemAnimator = DefaultItemAnimator()
        recyclerView_certainTasks.isNestedScrollingEnabled = false
        certainTaskList = mutableListOf()
        // ????????????????????????
        dialogUtil.createProgressDialog {
            Thread {
                certainTaskList = NetUtil.getCertainTask(
                    "studscoredetail",
                    "knowpoint,tkno,studans,scorestudnum,teachauditmsg,teachauditmsgqa,studanstext,studreply,datebegin,dateend,testtopic,id",
                    cond,
                    "tkno",
                    userAgent,
                    cookie,
                )
                it.dismiss()
                task_textView_scoreNum.setText(DataUtil.getScore(certainTaskList))
                for (i in certainTaskList) {
                    Log.d("mTAG", "??????????????????: " + i.certainTaskQuestionClass.qOption)
                    if (i.certainTaskQuestionClass.qTitle.contains("<img src=")) {
                        DataUtil.getImageUrl(i.certainTaskQuestionClass.qTitle)
                    }
                }
                runOnUiThread {
                    val mCertainTaskAdapter = CertainTaskAdapter(this, certainTaskList)
                    recyclerView_certainTasks.adapter = mCertainTaskAdapter
                }
            }.start()
        }
    } // ?????????

    private fun updateSurplusTime() {
        val mHandler = Handler(Looper.getMainLooper())
        if (isOutDate == "outDate") {
            task_textView_timeNum.text = "?????????"
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
                override fun run() { // ??????
                    if (timeSurplus == "0:0") {
                        timeSurplus = "?????????"
                        floatingActionButton_uploadOnClickEvent()
                        task_textView_timeNum.setTextColor(
                            ContextCompat.getColor(
                                mContext,
                                R.color.mRed
                            )
                        )
                    } else if (timeSurplus == "?????????") {
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

    } // ??????????????????

}