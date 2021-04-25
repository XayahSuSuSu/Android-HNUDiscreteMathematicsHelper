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
        holder.setIsRecyclable(false) // 复用问题导致CheckBox数据错乱,搞了我好久...
        val certainTaskClass: CertainTaskClass = mCertainTaskList.get(position)
        if (holder.linearLayout_options.childCount == 1) {
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
    }
}