package com.silver.fox.viewgroup

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.RelativeLayout
import androidx.core.view.ViewCompat
import com.silver.fox.view.R
import com.silver.fox.viewext.dp2px
import com.silver.fox.viewext.getScreenWidth

/**
 * 仿照酷狗的首页
 * @Author fox
 * @Date 2020/3/14 13:22
 */
class QQSlidingMenu @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr) {

    companion object {
        const val TAG = "SlidingMenu"
    }

    private var menuRightMargin = 0f;
    private var menuWidth = 0f
    private lateinit var contentView: View
    private lateinit var menuView: View
    private lateinit var shadowView: View

    private var menuIsOpen = false
    private var isIntercept = false

    private val gestureListener: GestureDetector.OnGestureListener =
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent?,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                //打开的时候往右边快速滑动切换（关闭），关闭的时候往左边快速切换滑动(打开)
                //快速向左滑动时velocityX是一个负数 像右边滑动时是正数
                if (menuIsOpen) {
                    if (velocityX > 0) {
                        closeMenu()
                        return true
                    }
                } else {
                    if (velocityX < 0) {
                        openMenu()
                        return true
                    }
                }
                return super.onFling(e1, e2, velocityX, velocityY)
            }
        }

    private val gestureDetector = GestureDetector(context, gestureListener)

    init {
        val array =
            context.obtainStyledAttributes(attrs,
                R.styleable.view_QQSlidingMenu
            )
        array.apply {
            menuRightMargin = getDimension(
                R.styleable.view_QQSlidingMenu_view_rightQQSlideMargin,
                dp2px(context, 50f).toFloat()
            )
            menuWidth = getScreenWidth(context) - menuRightMargin
            recycle()
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let {
            isIntercept = false
            if (menuIsOpen) {
                val currentX = it.x
                if (currentX > menuWidth) {
                    closeMenu()
                    isIntercept = true
                    return true
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    //问题
    //1 宽高不对 需要指定宽高
    //2 初始化进来是关闭的
    //3 手指抬起是二选一 要么关闭要么打开
    //4 处理左边和右边的缩放和透明度
    //5 快速滑动
    //6 当菜单打开时 手指触摸右边时需要关闭菜单 拦截事件
    override fun onFinishInflate() {
        super.onFinishInflate()
        //内容页宽度是屏幕宽度 菜单页宽度是屏幕宽度减去一小部分宽度
        //只能放置两个子view
        val container = getChildAt(0) as ViewGroup
        menuView = container.getChildAt(0)
        //通过layoutParams设置menu宽高
        val menuParams = menuView.layoutParams
        menuParams.width = menuWidth.toInt()
        menuView.layoutParams = menuParams

        //设置content宽高
        contentView = container.getChildAt(1)
        val contentParams = contentView.layoutParams
        container.removeView(contentView)

        val shadowContainer = RelativeLayout(context)
        shadowContainer.addView(contentView)
        shadowView = View(context)
        shadowView.setBackgroundColor(Color.parseColor("#55000000"))
        shadowContainer.addView(shadowView)


        contentParams.width = getScreenWidth(context)
        shadowContainer.layoutParams = contentParams
        container.addView(shadowContainer)

        //初始化菜单是关闭的 此时调用scrollto是无效的 因为这个适合onLayout未执行
        //scrollTo(menuWidth,0)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        //滚动menuWidth的距离
        scrollTo(menuWidth.toInt(), 0)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        //onIntercept中处理的事情不再进入此方法
        if (isIntercept) {
            return true
        }
        //这个是代表快速滑动的动作被执行了 就不会继续往下执行了
        if (gestureDetector.onTouchEvent(ev)) {
            return true
        }
        if (ev?.action == MotionEvent.ACTION_UP) {
            //只要手指抬起 根据当前滚动的距离来判断
            val currentScrollX = scrollX
            if (currentScrollX > menuWidth / 2) {
                //关闭
                closeMenu()
            } else {
                //打开
                openMenu()
            }
            //注意 这里的true是放到up事件中来的
            //确保up事件不会调用super的onTouchEvent事件
            return true
        }
        return super.onTouchEvent(ev)
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        //计算梯度值
        val scale = 1f * l / menuWidth
// 阴影变色是反过来的
        shadowView.alpha = 1 - scale
        ViewCompat.setTranslationX(menuView, 0.7f * l)
    }

    //打开菜单 滚动到0的位置
    private fun openMenu() {
        menuIsOpen = true
        smoothScrollTo(0, 0)
    }

    //关闭菜单  滚动到menuwidth的位置
    private fun closeMenu() {
        menuIsOpen = false
        smoothScrollTo(menuWidth.toInt(), 0)
    }
}