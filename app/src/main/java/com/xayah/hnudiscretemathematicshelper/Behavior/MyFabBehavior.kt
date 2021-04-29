package com.xayah.hnudiscretemathematicshelper.Util

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.Interpolator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator


class MyFabBehavior(context: Context?, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<View?>(context, attrs) {
    private var viewY //控件距离coordinatorLayout底部距离
            = 0f
    private var isHideAnimate //隐藏动画是否在进行
            = false
    private var isShowAnimate //显示动画是否在进行
            = false
    private var isChildTop = true //判断控件是否回到原始位置
    private var animator: ViewPropertyAnimator? = null

    /**
     * 滑动前指定Behavior关注的滑动方向
     */
    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int
    ): Boolean {
        if (child.getVisibility() === View.VISIBLE && viewY == 0f) {
            //获取控件距离父布局（coordinatorLayout）底部距离
            viewY = coordinatorLayout.height - child.getY()
        }
        return nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL != 0 //判断是否竖直滚动
    }

    /**
     * 用来监听滑动状态，对象消费滚动距离前回调
     */
    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray
    ) {
        //速度大于40，则执行动画（速度判断）
        if (Math.abs(dy) > 1) {
            //dy大于0是向上滚动 dy小于0是向下滚动
            if (dy >= 0 && !isHideAnimate && child.getVisibility() === View.VISIBLE) //如果向上滚动，隐藏动画已结束并且控件可见，则启动隐藏动画
            {
                hide(child)
            } else if (dy < 0 && !isShowAnimate && !isChildTop) //如果向下滚动，显示动画已结束并且控件未回到原始位置，则启动显示动画
            {
                show(child)
            } else if (dy >= 0 && isShowAnimate) //如果向上滚动，并且显示动画未结束，则先取消显示动画再启动隐藏动画
            {
                animator!!.cancel()
            } else if (dy < 0 && isHideAnimate) //如果向下滚动，并且显示隐藏未结束，则先取消隐藏动画再启动显示动画
            {
                animator!!.cancel()
            }
        }
    }

    /**
     * 隐藏时的动画
     */
    private fun hide(view: View) {
        animator = view.animate().translationY(viewY).setInterpolator(INTERPOLATOR).setDuration(300)
        animator!!.setListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {
                isHideAnimate = true
            }

            //每次cancel也会走onAnimationEnd方法
            override fun onAnimationEnd(animator: Animator) {
                //如果隐藏动画正常结束，而非取消后结束，则将布局隐藏
                if (isHideAnimate) {
//                    view.setVisibility(View.GONE);
                    isHideAnimate = false
                }
                isChildTop = false
            }

            override fun onAnimationCancel(animator: Animator) {
                isHideAnimate = false
            }

            override fun onAnimationRepeat(animator: Animator) {}
        })
        animator!!.start()
    }

    /**
     * 显示时的动画
     */
    private fun show(view: View) {
        animator = view.animate().translationY(0F).setInterpolator(INTERPOLATOR).setDuration(300)
        animator!!.setListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {
                view.visibility = View.VISIBLE
                isShowAnimate = true
            }

            //每次cancel也会走onAnimationEnd方法
            override fun onAnimationEnd(animator: Animator) {
                //如果显示动画正常结束，而非取消后结束，则isChildTop = true，表示控件回到原来位置
                if (isShowAnimate) {
                    isShowAnimate = false
                    isChildTop = true
                }
            }

            override fun onAnimationCancel(animator: Animator) {
                isShowAnimate = false
            }

            override fun onAnimationRepeat(animator: Animator) {}
        })
        animator!!.start()
    }

    companion object {
        private val INTERPOLATOR: Interpolator = FastOutSlowInInterpolator()
    }
}