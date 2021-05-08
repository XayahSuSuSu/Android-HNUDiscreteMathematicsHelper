package com.xayah.hnudiscretemathematicshelper.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.xayah.hnudiscretemathematicshelper.Activity.TaskActivity
import com.xayah.hnudiscretemathematicshelper.Class.TaskClass
import com.xayah.hnudiscretemathematicshelper.R
import com.xayah.hnudiscretemathematicshelper.Util.DataUtil
import com.xayah.hnudiscretemathematicshelper.Util.DialogUtil

class TaskAdapter(
    mContext: Context,
    mTaskList: MutableList<TaskClass>,
    mZh: String,
    mUserAgent: String,
    mCookie: String
) :
    RecyclerView.Adapter<TaskAdapter.ViewHolderTask>() {
    var mContext: Context
    var mTaskList: MutableList<TaskClass>
    var mZh: String
    var mUserAgent: String
    var mCookie: String

    init {
        this.mContext = mContext
        this.mTaskList = mTaskList
        this.mZh = mZh
        this.mUserAgent = mUserAgent
        this.mCookie = mCookie
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTask {
        val itemView: View =
            LayoutInflater.from(mContext).inflate(
                R.layout.recyclerview_tasks_item,
                parent,
                false
            )
        return ViewHolderTask(itemView)
    }

    override fun getItemCount(): Int {
        return mTaskList.size
    }

    class ViewHolderTask(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView_papername: TextView
        var textView_examperoid: TextView
        var textView_date: TextView
        var textView_classname: TextView
        var textView_time: TextView
        var cardView: CardView
        var linearLayout_tasks: LinearLayout

        init {
            textView_papername = itemView.findViewById(R.id.textView_papername)
            textView_examperoid = itemView.findViewById(R.id.textView_examperoid)
            textView_date = itemView.findViewById(R.id.textView_date)
            textView_classname = itemView.findViewById(R.id.textView_classname)
            textView_time = itemView.findViewById(R.id.textView_time)
            cardView = itemView.findViewById(R.id.cardView_item)
            linearLayout_tasks = itemView.findViewById(R.id.linearLayout_tasks)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolderTask, position: Int) {
        val taskClass: TaskClass = mTaskList.get(position)
        holder.textView_papername.text = taskClass.papername
        holder.textView_examperoid.text = taskClass.examperoid + " 分钟"
        val datebeginJSONObject = DataUtil.parseTime(taskClass.datebegin)
        val dateendJSONObject = DataUtil.parseTime(taskClass.dateend)
        holder.textView_date.text =
            datebeginJSONObject.getString("date") + " - " + dateendJSONObject.getString("date")
        holder.textView_classname.text = taskClass.classname
        holder.textView_time.text =
            datebeginJSONObject.getString("time") + " - " + dateendJSONObject.getString("time")

        when {
            DataUtil.isProperTime(taskClass.datebegin, taskClass.dateend) == "aheadDate" -> {
                if (DataUtil.isTheFirstTask(position, mTaskList)) {
                    val textView = TextView(mContext)
                    textView.text = "即将开放"
                    textView.setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        mContext.resources.getDimension(R.dimen.mTextViewTextSizeTaskGroup),
                    )
                    textView.setTextColor(
                        ContextCompat.getColor(
                            mContext,
                            R.color.mBlue
                        )
                    )
                    textView.setTypeface(
                        textView.typeface,
                        Typeface.BOLD
                    )
                    val mParam = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    mParam.gravity = Gravity.CENTER
                    mParam.setMargins(
                        0,
                        0,
                        0,
                        mContext.resources.getDimension(R.dimen.mTaskGroupBottom).toInt()
                    )
                    textView.layoutParams = mParam
                    holder.linearLayout_tasks.addView(textView, 0)
                }
                holder.textView_examperoid.setTextColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.mBlue
                    )
                )
            }
            DataUtil.isProperTime(taskClass.datebegin, taskClass.dateend) == "properDate" -> {
                if (DataUtil.isTheFirstTask(position, mTaskList)) {
                    val textView = TextView(mContext)
                    textView.text = "进行中"
                    textView.setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        mContext.resources.getDimension(R.dimen.mTextViewTextSizeTaskGroup),
                    )
                    textView.setTextColor(
                        ContextCompat.getColor(
                            mContext,
                            R.color.mGreen
                        )
                    )
                    textView.setTypeface(
                        textView.typeface,
                        Typeface.BOLD
                    )
                    val mParam = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    mParam.gravity = Gravity.CENTER
                    mParam.setMargins(
                        0,
                        0,
                        0,
                        mContext.resources.getDimension(R.dimen.mTaskGroupBottom).toInt()
                    )
                    textView.layoutParams = mParam
                    holder.linearLayout_tasks.addView(textView, 0)
                }
                holder.textView_examperoid.setTextColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.mGreen
                    )
                )
            }
            else -> {
                if (DataUtil.isTheFirstTask(position, mTaskList)) {
                    val textView = TextView(mContext)
                    textView.text = "已过期"
                    textView.setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        mContext.resources.getDimension(R.dimen.mTextViewTextSizeTaskGroup),
                    )
                    textView.setTextColor(
                        ContextCompat.getColor(
                            mContext,
                            R.color.mRed
                        )
                    )
                    textView.setTypeface(
                        textView.typeface,
                        Typeface.BOLD
                    )
                    val mParam = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    mParam.gravity = Gravity.CENTER
                    mParam.setMargins(
                        0,
                        0,
                        0,
                        mContext.resources.getDimension(R.dimen.mTaskGroupBottom).toInt()
                    )
                    textView.layoutParams = mParam
                    holder.linearLayout_tasks.addView(textView, 0)
                }
                holder.textView_examperoid.setTextColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.mRed
                    )
                )
            }
        }

        holder.textView_papername.setEllipsize(TextUtils.TruncateAt.MARQUEE)
        holder.textView_papername.setSingleLine(true)
        holder.textView_papername.setSelected(true)
        holder.textView_papername.setFocusable(true)
        holder.textView_papername.setFocusableInTouchMode(true)
        holder.textView_examperoid.setEllipsize(TextUtils.TruncateAt.MARQUEE)
        holder.textView_examperoid.setSingleLine(true)
        holder.textView_examperoid.setSelected(true)
        holder.textView_examperoid.setFocusable(true)
        holder.textView_examperoid.setFocusableInTouchMode(true)
        holder.textView_date.setEllipsize(TextUtils.TruncateAt.MARQUEE)
        holder.textView_date.setSingleLine(true)
        holder.textView_date.setSelected(true)
        holder.textView_date.setFocusable(true)
        holder.textView_date.setFocusableInTouchMode(true)
        holder.textView_classname.setEllipsize(TextUtils.TruncateAt.MARQUEE)
        holder.textView_classname.setSingleLine(true)
        holder.textView_classname.setSelected(true)
        holder.textView_classname.setFocusable(true)
        holder.textView_classname.setFocusableInTouchMode(true)
        holder.textView_time.setEllipsize(TextUtils.TruncateAt.MARQUEE)
        holder.textView_time.setSingleLine(true)
        holder.textView_time.setSelected(true)
        holder.textView_time.setFocusable(true)
        holder.textView_time.setFocusableInTouchMode(true)

        holder.cardView.setOnClickListener {
            if (DataUtil.isProperTime(taskClass.datebegin, taskClass.dateend) == "aheadDate") {
                val dialogUtil = DialogUtil(mContext)
                dialogUtil.createPositiveButtonDialog("试卷还未到开始时间!", "好的") {
                }

            } else if (DataUtil.isProperTime(taskClass.datebegin, taskClass.dateend) == "outDate") {
                val dialogUtil = DialogUtil(mContext)
                dialogUtil.createPositiveButtonDialog("试卷已经截止,仅可查看试卷!", "好的") {
                    val intent =
                        Intent(mContext, TaskActivity::class.java)
                    intent.putExtra("schoolno", taskClass.schoolno)
                    intent.putExtra("zh", mZh)
                    intent.putExtra("id", taskClass.id)
                    intent.putExtra("papername", taskClass.papername)
                    intent.putExtra("examperoid", taskClass.examperoid)
                    intent.putExtra("userAgent", mUserAgent)
                    intent.putExtra("cookie", mCookie)
                    intent.putExtra("isOutDate", "outDate")
                    mContext.startActivity(intent)
                }
            } else {
                val intent =
                    Intent(mContext, TaskActivity::class.java)
                intent.putExtra("schoolno", taskClass.schoolno)
                intent.putExtra("zh", mZh)
                intent.putExtra("id", taskClass.id)
                intent.putExtra("papername", taskClass.papername)
                intent.putExtra("examperoid", taskClass.examperoid)
                intent.putExtra("userAgent", mUserAgent)
                intent.putExtra("cookie", mCookie)
                intent.putExtra("isOutDate", "properDate")
                mContext.startActivity(intent)
            }
        }
    }

    fun setItem(l_isEdited: Boolean, l_position: Int) {
        val mTaskClass: TaskClass = mTaskList.get(l_position)
//        mTaskClass.isEdited = l_isEdited
        mTaskList.set(l_position, mTaskClass) //在集合中修改这条数据
        notifyItemChanged(l_position)
    }


    fun addItem(l_TaskClass: TaskClass) {
//        mTaskList.add(0, l_TaskClass) //在集合中修改这条数据
        notifyItemInserted(0)
        notifyDataSetChanged()
    }

}