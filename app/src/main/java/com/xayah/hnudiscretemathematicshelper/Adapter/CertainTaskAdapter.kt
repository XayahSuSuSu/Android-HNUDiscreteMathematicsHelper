package com.xayah.hnudiscretemathematicshelper.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.xayah.hnudiscretemathematicshelper.Class.CertainTaskClass
import com.xayah.hnudiscretemathematicshelper.Component.NetImageView
import com.xayah.hnudiscretemathematicshelper.R
import com.xayah.hnudiscretemathematicshelper.Util.DataUtil


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
        // 绑定recyclerview_certaintasks_item布局
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
        var linearLayout_options: LinearLayout = itemView.findViewById(R.id.linearLayout_options)
        var textView_subject: TextView = itemView.findViewById(R.id.textView_subject)
        var cardView: CardView = itemView.findViewById(R.id.cardView_item)

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolderTask, position: Int) {
        holder.setIsRecyclable(false) // 复用问题导致CheckBox数据错乱,搞了我好久...
        val certainTaskClass: CertainTaskClass = mCertainTaskList.get(position)
        Log.d("mTAG", "答案数量: " + certainTaskClass.certainTaskQuestionClass.qAnswer)
        if (certainTaskClass.certainTaskQuestionClass.qTitle.contains("<img src=")) {
            val mImageUrlList =
                DataUtil.getImageUrl(certainTaskClass.certainTaskQuestionClass.qTitle)
            holder.textView_subject.visibility = View.GONE
            for ((index, imgUrl) in mImageUrlList.withIndex()) {
                if (index == 0 && imgUrl.isNotEmpty()) {
                    val textView = TextView(mContext)
                    textView.text = (position + 1).toString() + "." + imgUrl
                    val mParam = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    if (certainTaskClass.scorestudnum != "0" && !certainTaskClass.scorestudnum.contains(
                            "-"
                        ) && certainTaskClass.scorestudnum.isNotEmpty()
                    ) {
                        textView.setTextColor(
                            ContextCompat.getColor(
                                mContext,
                                R.color.white
                            )
                        )
                    } else {
                        textView.setTextColor(
                            ContextCompat.getColor(
                                mContext,
                                R.color.black
                            )
                        )
                    }
                    textView.setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        mContext.resources.getDimension(R.dimen.mTextViewTextSize),
                    )
                    textView.setTypeface(
                        textView.typeface,
                        Typeface.BOLD
                    )
                    mParam.setMargins(
                        mContext.resources.getDimension(R.dimen.mStart).toInt(),
                        mContext.resources.getDimension(R.dimen.mTopTextView).toInt(),
                        mContext.resources.getDimension(R.dimen.mEnd).toInt(),
                        0
                    )
                    textView.layoutParams = mParam
                    holder.linearLayout_options.addView(textView)
                } else
                    if (index == mImageUrlList.size - 1 && imgUrl.isNotEmpty()) {
                        val textView = TextView(mContext)
                        textView.text = imgUrl
                        val mParam = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        mParam.setMargins(
                            mContext.resources.getDimension(R.dimen.mStart).toInt(),
                            0,
                            mContext.resources.getDimension(R.dimen.mEnd).toInt(),
                            0
                        )
                        if (certainTaskClass.scorestudnum != "0" && !certainTaskClass.scorestudnum.contains(
                                "-"
                            ) && certainTaskClass.scorestudnum.isNotEmpty()
                        ) {
                            textView.setTextColor(
                                ContextCompat.getColor(
                                    mContext,
                                    R.color.white
                                )
                            )
                        } else {
                            textView.setTextColor(
                                ContextCompat.getColor(
                                    mContext,
                                    R.color.black
                                )
                            )
                        }
                        textView.setTextSize(
                            TypedValue.COMPLEX_UNIT_PX,
                            mContext.resources.getDimension(R.dimen.mTextViewTextSize),
                        )
                        textView.setTypeface(
                            textView.typeface,
                            Typeface.BOLD
                        )
                        textView.layoutParams = mParam
                        holder.linearLayout_options.addView(textView)
                    } else {
                        if (imgUrl.isNotEmpty()) {
                            val netImageView = NetImageView(mContext)
                            netImageView.setImageURL(imgUrl)
                            val mParam = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            mParam.setMargins(
                                mContext.resources.getDimension(R.dimen.mStart).toInt(),
                                0,
                                mContext.resources.getDimension(R.dimen.mEnd).toInt(),
                                0
                            )
                            netImageView.layoutParams = mParam
                            holder.linearLayout_options.addView(netImageView)
                        }
                    }
            }
        } else {
            holder.textView_subject.text =
                (position + 1).toString() + "." + certainTaskClass.certainTaskQuestionClass.qTitle
        }

        if (certainTaskClass.certainTaskQuestionClass.qAnswer == "1") {
            Log.d("mTAG", "是单选题: " + certainTaskClass.certainTaskQuestionClass.qAnswer)
            val radioGroup = RadioGroup(mContext)
            radioGroup.setTag("mRadioGroup")
            val mRadioGroupParam = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            mRadioGroupParam.setMargins(
                0,
                0,
                0,
                0
            )
            radioGroup.layoutParams = mRadioGroupParam
            holder.linearLayout_options.addView(radioGroup)
        }

        if (certainTaskClass.scorestudnum != "0" && !certainTaskClass.scorestudnum.contains("-") && certainTaskClass.scorestudnum.isNotEmpty()) {
            holder.cardView.setCardBackgroundColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.mGreen
                )
            )
            holder.textView_subject.setTextColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.white
                )
            )
        }


//        if (holder.linearLayout_options.childCount == 1) {
        Log.d("mTAG", "是单选题")
        for ((index, i) in certainTaskClass.certainTaskQuestionClass.qOption.withIndex()) {
            if (i.isNotEmpty()) {
                if (certainTaskClass.certainTaskQuestionClass.qAnswer == "1") {
                    Log.d("mTAG", "是单选题")
                    val radioButton = RadioButton(mContext)
                    radioButton.id = View.generateViewId()
                    radioButton.text = "{见下图}"
                    if (certainTaskClass.scorestudnum != "0" && !certainTaskClass.scorestudnum.contains(
                            "-"
                        ) && certainTaskClass.scorestudnum.isNotEmpty()
                    )
                        radioButton.buttonTintList = ColorStateList.valueOf(Color.WHITE)
                    if (certainTaskClass.studans.contains(DataUtil.int2Char(index)))
                        radioButton.isChecked = true
                    if (certainTaskClass.scorestudnum != "0" && !certainTaskClass.scorestudnum.contains(
                            "-"
                        ) && certainTaskClass.scorestudnum.isNotEmpty()
                    ) {
                        radioButton.setTextColor(
                            ContextCompat.getColor(
                                mContext,
                                R.color.white
                            )
                        )
                    }
                    radioButton.setOnCheckedChangeListener { buttonView, isChecked ->
                        if (isChecked) {
                            certainTaskClass.studans = DataUtil.int2Char(index).toString()
                            mCertainTaskList[position] = certainTaskClass
                            Log.d("mTAG", "修改后: " + certainTaskClass.studans)
                        }
                    }

                    val mParam = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    mParam.setMargins(
                        mContext.getResources().getDimension(R.dimen.mStart).toInt(),
                        mContext.getResources().getDimension(R.dimen.mTop).toInt(),
                        mContext.getResources().getDimension(R.dimen.mEnd).toInt(),
                        0
                    )
                    radioButton.layoutParams = mParam
                    val mRadioGroup: RadioGroup =
                        holder.linearLayout_options.findViewWithTag("mRadioGroup")
                    mRadioGroup.addView(radioButton)

                    if (i.contains("<img src=")) {
                        val mImageUrlList =
                            DataUtil.getImageUrl(i)
                        for ((index, imgUrl) in mImageUrlList.withIndex()) {
                            if (index == 0 && imgUrl.isNotEmpty()) {
                                radioButton.text = "$imgUrl{见下图}"
                            } else
                                if (index == mImageUrlList.size - 1 && imgUrl.isNotEmpty()) {
                                    radioButton.text = radioButton.text.toString() + imgUrl
                                } else {
                                    if (imgUrl.isNotEmpty()) {
                                        val netImageView = NetImageView(mContext)
                                        netImageView.setImageURL(imgUrl)
                                        val mParam = LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        )
                                        mParam.setMargins(
                                            mContext.resources.getDimension(R.dimen.mStart).toInt(),
                                            0,
                                            mContext.resources.getDimension(R.dimen.mEnd).toInt(),
                                            0
                                        )
                                        netImageView.layoutParams = mParam
                                        val mRadioGroup: RadioGroup =
                                            holder.linearLayout_options.findViewWithTag("mRadioGroup")
                                        mRadioGroup.addView(netImageView)
                                    }
                                }
                        }
                    } else {
                        radioButton.text = i
                    }
                } else {
                    val checkBox = CheckBox(mContext)
                    checkBox.text = "{见下图}"
                    if (certainTaskClass.scorestudnum != "0" && !certainTaskClass.scorestudnum.contains(
                            "-"
                        ) && certainTaskClass.scorestudnum.isNotEmpty()
                    )
                        checkBox.buttonTintList = ColorStateList.valueOf(Color.WHITE)
                    if (certainTaskClass.studans.contains(DataUtil.int2Char(index)))
                        checkBox.isChecked = true
                    if (certainTaskClass.scorestudnum != "0" && !certainTaskClass.scorestudnum.contains(
                            "-"
                        ) && certainTaskClass.scorestudnum.isNotEmpty()
                    ) {
                        checkBox.setTextColor(
                            ContextCompat.getColor(
                                mContext,
                                R.color.white
                            )
                        )
                    }
                    checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                        if (isChecked) {
                            val newStudans = DataUtil.addAnswer(
                                DataUtil.int2Char(index),
                                certainTaskClass.studans
                            )
                            Log.d("mTAG", "newStudans: " + newStudans)
                            certainTaskClass.studans = newStudans
                            mCertainTaskList[position] = certainTaskClass
                            Log.d("mTAG", "修改后: " + certainTaskClass.studans)
                        } else {
                            val newStudans = DataUtil.deleteAnswer(
                                DataUtil.int2Char(index),
                                certainTaskClass.studans
                            )
                            Log.d("mTAG", "newStudans: " + newStudans)
                            certainTaskClass.studans = newStudans
                            mCertainTaskList[position] = certainTaskClass
                            Log.d("mTAG", "修改后: " + certainTaskClass.studans)
                        }
                    }

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
                    if (i.contains("<img src=")) {
                        val mImageUrlList =
                            DataUtil.getImageUrl(i)
                        for ((mCheckBoxindex, imgUrl) in mImageUrlList.withIndex()) {
                            if (mCheckBoxindex == 0 && imgUrl.isNotEmpty()) {
                                checkBox.text = "$imgUrl{见下图}"
                            } else
                                if (mCheckBoxindex == mImageUrlList.size - 1 && imgUrl.isNotEmpty()) {
                                    checkBox.text = checkBox.text.toString() + imgUrl
                                } else {
                                    if (imgUrl.isNotEmpty()) {
                                        val netImageView = NetImageView(mContext)
                                        netImageView.setImageURL(imgUrl)
                                        val mCheckBoxParam = LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        )
                                        mCheckBoxParam.setMargins(
                                            mContext.resources.getDimension(R.dimen.mStart).toInt(),
                                            0,
                                            mContext.resources.getDimension(R.dimen.mEnd).toInt(),
                                            0
                                        )
                                        netImageView.layoutParams = mCheckBoxParam
                                        holder.linearLayout_options.addView(netImageView)
                                    }
                                }
                        }
                    } else {
                        checkBox.text = i
                    }
                }


            }
        }
    }
}