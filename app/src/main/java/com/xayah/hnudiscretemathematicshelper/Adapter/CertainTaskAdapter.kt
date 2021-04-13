package com.xayah.hnudiscretemathematicshelper.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import com.xayah.hnudiscretemathematicshelper.Class.CertainTaskClass
import com.xayah.hnudiscretemathematicshelper.R


class CertainTaskAdapter(
    mContext: Context,
    mCertainTaskList: MutableList<CertainTaskClass>,
) :
    RecyclerView.Adapter<CertainTaskAdapter.ViewHolderTask>() {
    var mContext: Context
    var mCertainTaskList: MutableList<CertainTaskClass>

    init {
        this.mContext = mContext
        this.mCertainTaskList = mCertainTaskList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTask {
        val itemView: View =
            LayoutInflater.from(mContext).inflate(
                R.layout.recyclerview_certaintasks_item,
                parent,
                false
            )
        return ViewHolderTask(itemView)
    }

    override fun getItemCount(): Int {
        return mCertainTaskList.size
    }

    class ViewHolderTask(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var linearLayout_options: LinearLayout
        var textView_subject: TextView
        var cardView: CardView

        init {
            linearLayout_options = itemView.findViewById(R.id.linearLayout_options)
            textView_subject = itemView.findViewById(R.id.textView_subject)
            cardView = itemView.findViewById(R.id.cardView_item)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolderTask, position: Int) {
        val certainTaskClass: CertainTaskClass = mCertainTaskList.get(position)
        if (holder.textView_subject.text == "qTitle"){
            for (i in certainTaskClass.certainTaskQuestionClass.qOption) {
                if (i.isNotEmpty()) {
                    val checkBox = CheckBox(mContext)
                    checkBox.setText(i)
                    val mParam = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    mParam.setMargins(
                        mContext.getResources().getDimension(R.dimen.mStart).toInt(),
                        mContext.getResources().getDimension(R.dimen.mTop).toInt(),
                        mContext.getResources().getDimension(R.dimen.mEnd).toInt(),
                        0
                    )
                    checkBox.layoutParams = mParam
                    holder.linearLayout_options.addView(checkBox)
                }
            }
        }
        holder.textView_subject.text = certainTaskClass.certainTaskQuestionClass.qTitle
//        holder.textView_subject.setEllipsize(TextUtils.TruncateAt.MARQUEE)
//        holder.textView_subject.setSingleLine(true)
//        holder.textView_subject.setSelected(true)
//        holder.textView_subject.setFocusable(true)
//        holder.textView_subject.setFocusableInTouchMode(true)
    }

    fun setItem(l_isEdited: Boolean, l_position: Int) {
        val mCertainTaskClass: CertainTaskClass = mCertainTaskList.get(l_position)
//        mCertainTaskClass.isEdited = l_isEdited
        mCertainTaskList.set(l_position, mCertainTaskClass) //在集合中修改这条数据
        notifyItemChanged(l_position)
    }


    fun addItem(l_CertainTaskClass: CertainTaskClass) {
//        mCertainTaskList.add(0, l_CertainTaskClass) //在集合中修改这条数据
        notifyItemInserted(0)
        notifyDataSetChanged()
    }

}