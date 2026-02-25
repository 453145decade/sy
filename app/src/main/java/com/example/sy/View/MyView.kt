package com.example.sy.View

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.navigation.NavHostController
import com.example.lib_base.Const

class MyView : View {
    var paint : Paint
    var time:Int = 0
    var angle = 0f
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


//    init {
//        paint = Paint()
//        // 创建一个Paint对象
//        paint = Paint()
//        // 设置画笔颜色
//        paint.color = Color.BLUE
//        // 设置画笔样式为描边
//        paint.style = Paint.Style.STROKE
//        // 设置画笔宽度为5f
//        paint.strokeWidth = 5f
//        // 设置画笔抗锯齿
//        paint.isAntiAlias = true
//// 设置画笔的文字大小为35f（浮点数）
//        paint.textSize = 35f
//    }
//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//        // 在画布上绘制一个圆
//        canvas.drawCircle(110f, 110f, 100f, paint)
//        // 在画布上绘制一个文本
//        canvas.drawText("Kiva", 20f, 100f, paint)
//        // 在画布上绘制一个矩形
//        canvas.drawRect(10f, 250f, 210f, 350f, paint)
////        在画布上绘制一条线
//        canvas.drawArc(10f, 360f, 210f, 460f,90f,90f,false, paint)
////        在画布上绘制一条弧
//        canvas.drawLine(10f, 470f, 210f, 470f,  paint)
//    }








    //    初始化画笔
    init {
        // 创建一个Paint对象
        paint = Paint()
        // 设置画笔颜色
        paint.color = Color.RED
        // 设置画笔样式为描边
        paint.style = Paint.Style.STROKE
        // 设置画笔宽度为5f
        paint.strokeWidth = 2f
        // 设置画笔抗锯齿
        paint.isAntiAlias = true
        // 设置画笔的文字大小为35f（浮点数）
        paint.textSize = 35f

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val mode1 = MeasureSpec.getMode(widthMeasureSpec)
        val mode2 = MeasureSpec.getMode(heightMeasureSpec)
        var size1 = MeasureSpec.getSize(widthMeasureSpec)
        var size2 = MeasureSpec.getSize(heightMeasureSpec)
        if (mode1 == MeasureSpec.AT_MOST ) {
            size1 = 150
        }
        if (mode2 == MeasureSpec.AT_MOST) {
            size2 = 150
        }
        setMeasuredDimension(size1, size2)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawText(time.toString(),60f,80f,paint)
        val rectF = RectF(20f, 20f, 120f, 120f)
        canvas.drawArc(rectF, 0f, angle, false, paint)


//        摩尔上半边上色
        paint.setColor(Color.parseColor("#FF1FB2F0"))
        paint.style = Paint.Style.FILL
        canvas?.drawArc(100f, 400f, 800f, 1100f, 180f, 180f, false, paint)
//        设置小控件填充
        paint.setColor(Color.parseColor("#FF1FB2F0"))
        paint.style = Paint.Style.FILL
        canvas?.drawOval(100f, 600f, 225f, 900f, paint)
        paint.setColor(Color.parseColor("#FF1FB2F0"))
        paint.style = Paint.Style.FILL
        canvas?.drawOval(675f, 600f, 800f, 900f, paint)
//        正方形
        paint.setColor(Color.parseColor("#FF1FB2F0"))
        paint.style = Paint.Style.FILL
        canvas?.drawRect(350f, 650f, 550f, 850f, paint)
//        摩尔下半边上色
        paint.setColor(Color.parseColor("#FFFCE9BE"))
        paint.style = Paint.Style.FILL
        canvas?.drawArc(102f, 470f, 798f, 1100f, 0f, 180f, false, paint)
//        脸上的弧度
        paint.setColor(Color.parseColor("#FFFCE9BE"))
        paint.style = Paint.Style.FILL
        canvas?.drawArc(105f, 715f, 430f, 860f, 180f, 180f, false, paint)
        paint.setColor(Color.parseColor("#FFFCE9BE"))
        paint.style = Paint.Style.FILL
        canvas?.drawArc(475f, 715f, 800f, 860f, 180f, 180f, false, paint)
//        绘制摩尔的鼻子
        paint.color = Color.RED
        paint.style = Paint.Style.FILL
        canvas?.drawCircle(450f, 750f, 85f, paint)
//        高光
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        canvas?.drawCircle(485f, 720f, 16f, paint)
//        摩尔的左眼
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
//        椭圆
        canvas?.drawOval(250f, 480f, 380f, 680f, paint)
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        canvas?.drawOval(315f, 525f, 365f, 615f, paint)
//        摩尔的右眼
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
//        椭圆
        canvas?.drawOval(500f, 480f, 630f, 680f, paint)
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        canvas?.drawOval(565f, 525f, 615f, 615f, paint)
//        摩尔的嘴巴
        // 设置下面嘴巴的弧度
        paint.color = Color.BLACK
        paint.strokeWidth = 3f
        paint.style = Paint.Style.FILL
        paint.setColor(Color.parseColor("#FFB51E26"))
        canvas?.drawArc(360f, 695f, 540f, 995f, 0f, 180f, false, paint)
//        摩尔的牙齿
        //带弧度的矩形
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 3f
        canvas?.drawRoundRect(420f, 835f, 480f, 885f, 15f, 15f, paint)
//       填充上面嘴巴
        paint.setColor(Color.parseColor("#FFFCE9BE"))
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 3f
        canvas?.drawArc(350f, 810f, 550f, 860f, 0f, 180f, false, paint)
//       设置上面嘴巴的弧度
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3f
        canvas?.drawArc(350f, 810f, 550f, 860f, 0f, 180f, false, paint)
//        舌头
        paint.setColor(Color.parseColor("#E7959A"))
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 3f
        canvas?.drawArc(385f, 930f, 515f, 970f, 180f, 180f, false, paint)
        canvas?.drawArc(385f, 895f, 515f, 995f, 0f, 180f, false, paint)
//       设置下面嘴巴的弧度的边
        paint.color = Color.BLACK
        paint.strokeWidth = 3f
        paint.style = Paint.Style.STROKE
        canvas?.drawArc(360f, 695f, 540f, 995f, 0f, 180f, false, paint)


        // 在画布上绘制一个圆形
        paint.color = Color.BLUE
        paint.strokeWidth = 5f
        paint.style = Paint.Style.STROKE
        canvas?.drawCircle(450f, 750f, 350f, paint)
    }


    fun start(i:Int,navHostController: NavHostController){
        time = i
        val animator1 = ValueAnimator.ofInt(time, 0)
        animator1.duration = (i*1000).toLong()
        animator1.addUpdateListener {
            time = it.animatedValue as Int
            invalidate()

            if(time == 0){
                navHostController.navigate(Const.MainPage)
            }
        }
        animator1.start()

//        弧的变化
        val animator2 = ValueAnimator.ofFloat(angle, 360f)
        animator2.duration = (i*1000).toLong()
        animator2.addUpdateListener {
            angle = it.animatedValue as Float
            invalidate()
        }
        animator2.start()

    }


}