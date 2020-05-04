package com.silver.fox.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.animation.doOnEnd
import com.silver.fox.math.distance
import com.silver.fox.viewext.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * @Author fox
 * @Date 2020/5/1 23:21
 */
class MessageBubbleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var fixationPoint: PointF = PointF(0f, 0f)
    private var dragPoint: PointF = PointF(0f, 0f)


    var dragBitmap: Bitmap? = null

    private val paint: Paint by lazy {
        Paint().apply {
            color = Color.RED
            //抗锯齿
            isAntiAlias = true
            //抗抖动
            isDither = true
        }
    }

    private val dragRadius: Int = dp2px(10f)
    private var fixRadius: Int = dp2px(8f)
    private val FIX_MAX_RADIUS: Float = dp2px(8f).toFloat()
    private val Fix_MIN_RADIUS: Float = dp2px(2f).toFloat()

    var messageBubbleListener: MessageBubbleListener? = null

    companion object {
        @JvmStatic
        fun attach(
            target: View,
            disappearListener: BubbleMessageTouchListener.BubbleDisappearListener
        ) {
            target.setOnTouchListener(BubbleMessageTouchListener(target, disappearListener))
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                initPointF(event.x, event.y)
            }

            MotionEvent.ACTION_MOVE -> {
                updateDrag(event.x, event.y)
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        if (dragPoint.isAvailable()&& fixationPoint.isAvailable()) {
            canvas?.run {
                dragPoint.run {
                    drawCircle(x, y, dragRadius.toFloat(), paint)
                }

                getBezerPath()?.let {
                    fixationPoint.run {
                        drawCircle(x, y, fixRadius.toFloat(), paint)
                        //画贝塞尔曲线
                        drawPath(it, paint)
                    }
                }
                dragBitmap?.run {
                    drawBitmap(this, dragPoint.x - width / 2, dragPoint.y - height / 2, null)
                }
            }
        }
    }

    private fun getBezerPath(): Path? {
        fixRadius = (FIX_MAX_RADIUS - distance(
            fixationPoint.x.toDouble(),
            fixationPoint.y.toDouble(),
            dragPoint.x.toDouble(),
            dragPoint.y.toDouble()
        ) / 14).toInt()
        if (fixRadius < Fix_MIN_RADIUS) {
            return null
        } else {
            //求斜率
            val path = Path()
            val dy = dragPoint.y - fixationPoint.y
            val dx = dragPoint.x - fixationPoint.x
            val tanA = dy / dx

            val arcTanA = Math.atan(tanA.toDouble())

            val p0x = (fixationPoint.x + fixRadius * sin(arcTanA)).toFloat()
            val p0y = (fixationPoint.y - fixRadius * cos(arcTanA)).toFloat()


            val p1x = (dragPoint.x + dragRadius * sin(arcTanA)).toFloat()
            val p1y = (dragPoint.y - dragRadius * cos(arcTanA)).toFloat()
            val p2x = (dragPoint.x - dragRadius * sin(arcTanA)).toFloat()
            val p2y = (dragPoint.y + dragRadius * cos(arcTanA)).toFloat()
            val p3x = (fixationPoint.x - fixRadius * sin(arcTanA)).toFloat()
            val p3y = (fixationPoint.y + fixRadius * cos(arcTanA)).toFloat()

            path.moveTo(p0x, p0y)

            val controlPoint = getControlPoint()
            path.quadTo(controlPoint.x, controlPoint.y, p1x, p1y)
            path.lineTo(p2x, p2y)
            path.quadTo(controlPoint.x, controlPoint.y, p3x, p3y)
            path.close()
            return path
        }
    }

    private fun getControlPoint(): PointF {
        return PointF(
            (dragPoint.x + fixationPoint.x) / 2,
            (dragPoint.y + fixationPoint.y) / 2
        )
    }

    fun initPointF(x: Float, y: Float) {
        fixationPoint = PointF(x, y)
        dragPoint = PointF(x, y)
        invalidate()
    }


    fun updateDrag(x: Float, y: Float) {
        dragPoint.x = x
        dragPoint.y = y
        invalidate()
    }

    fun handleUp() {
        if (fixRadius > Fix_MIN_RADIUS) {
            //回弹
            ObjectAnimator.ofFloat(1f).apply {
                duration = 250
                addUpdateListener {
                    val percent = it.animatedValue as Float
                    val start = PointF(dragPoint.x, dragPoint.y)
                    val end = PointF(fixationPoint.x, fixationPoint.y)
                    val point = start.getPointByPercent(end, percent)
                    updateDrag(point.x, point.y)
                }
                doOnEnd {
                    messageBubbleListener?.restore()
                }
            }.start()
        } else {
            //爆炸消失
            messageBubbleListener?.dismiss(dragPoint)
        }
    }

}

class BubbleMessageTouchListener(val target: View, val listener: BubbleDisappearListener) :
    View.OnTouchListener, MessageBubbleListener {

    private val bubbleView by lazy {
        MessageBubbleView(target.context).apply {
            messageBubbleListener = this@BubbleMessageTouchListener
        }
    }

    private val context by lazy {
        target.context
    }

    private val windowManager by lazy {
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    private val params = WindowManager.LayoutParams().apply { format = PixelFormat.TRANSPARENT }
    private val bombFrame = FrameLayout(context)
    private val bombImage = ImageView(context).apply {
        layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        bombFrame.addView(this)
    }

    interface BubbleDisappearListener {
        fun onDismiss()
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                windowManager.addView(bubbleView, params)
                //添加到windowmanager中 同时将target隐藏
                //要保证固定中心在target的中心 并且减去状态栏高度
                val location = IntArray(2)
                target.getLocationOnScreen(location)
                val bitmap = target.bitmap
                bubbleView.initPointF(
                    (location[0] + target.width / 2).toFloat(),
                    (location[1] + target.height / 2).toFloat() - context.getStatusBarHeight()
                )
                bubbleView.dragBitmap = bitmap
                target.visibility = View.INVISIBLE
            }

            MotionEvent.ACTION_MOVE -> {
                bubbleView.updateDrag(event.rawX, event.rawY - context.getStatusBarHeight())

            }

            MotionEvent.ACTION_UP -> {
                bubbleView.handleUp()
            }
        }
        return true
    }

    override fun restore() {
        windowManager.removeView(bubbleView)
        target.visibility = View.VISIBLE
    }

    override fun dismiss(pointF: PointF?) {
        windowManager.removeView(bubbleView)
        //在window上添加一个爆炸动画
        windowManager.addView(bombFrame, params)
        bombImage.setBackgroundResource(R.drawable.view_anim_bubble_pop)
        val drawable = bombImage.background as AnimationDrawable
        bombImage.x = (pointF!!.x - drawable.intrinsicWidth / 2)
        bombImage.y = (pointF.y - drawable.intrinsicHeight / 2)
        drawable.start()

        bombImage.postDelayed(Runnable {
            windowManager.removeView(bombFrame)
            listener.onDismiss()
        }, getAnimationDrawableTime(drawable))

    }

    private fun getAnimationDrawableTime(drawable: AnimationDrawable): Long {
        val numberOfFrames = drawable.numberOfFrames
        var total = 0
        for (i in 0..numberOfFrames) {
            total += drawable.getDuration(i)
        }
        return total.toLong()
    }
}

interface MessageBubbleListener {
    fun restore()

    fun dismiss(pointF: PointF?)
}

