package cn.yzl.ruleview.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import java.util.List;

import cn.yzl.ruleview.library.R;
import cn.yzl.ruleview.library.utils.DensityUtils;


/**
 * Created by 伊 on 2016/12/17.
 */
public class RulerView extends View {

    /**
     * 是否滑动过,用来过滤 finish事件
     */
    protected boolean isScrolled;

    private RulerListener listener;
    /**
     * 线的颜色
     */
    protected int maxLineColor;
    protected int minLineColor;
    protected int midLineColor;


    protected int lineGravity;

    /**
     * 线的粗细
     */
    protected int lineWidth;

    /**
     * 线的高度
     */
    protected int maxLineHeight;
    protected int midLineHeight;
    protected int minLineHeight;

    /**
     * 间距
     */
    protected int lineSpace;


    protected int lineCount;
    protected int maxLineLengh;

    //划线
    protected Paint maxPaint;
    protected Paint midPaint;
    protected Paint minPaint;
    protected Paint bottomLinePaint;

    //画文字
    protected Paint textPaint;

    protected Paint indicatorPain;

    /**
     * 第一个scroller用于放开手的时候惯性处理,第二个用于修复滑动,使得指示器指向一个刻度,而不是刻度中间的位置
     */
    protected Scroller scroller, fixScroller;

    /**
     * 字体大小,按高度来算
     */
    protected int textSize;

    protected int textColor;

    protected int textGravity;


    /**
     * 指示器
     */
    protected int indicatorSize;

    protected int indicatorColor;

    /**
     * 基线
     */
    protected int baseLineColor;

    protected int baseLineHeight;

    protected int baseLineGravity;

    protected boolean showBaseLine;

    /**
     * 0的时候的线X坐标
     */
    protected int baseX;

    protected int maxLineCount;

    protected int midLineCount;

    /**
     * 当前偏移X坐标
     */
    protected int offsetX;

    protected int selPosition;

    protected List<String> data;


    /**
     * 速率计算器
     */
    protected VelocityTracker velocityTracker;

    public RulerView(Context context) {
        super(context);

    }

    public RulerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RulerView);

        maxLineColor = typedArray.getColor(R.styleable.RulerView_max_line_color, Color.parseColor("#0078D7"));
        midLineColor = typedArray.getColor(R.styleable.RulerView_mid_line_color, Color.parseColor("#36B24A"));
        minLineColor = typedArray.getColor(R.styleable.RulerView_min_line_color, Color.parseColor("#CCCCCC"));

        lineWidth = (int) typedArray.getDimension(R.styleable.RulerView_line_width, DensityUtils.dp2px(context, 2));

        lineSpace = (int) typedArray.getDimension(R.styleable.RulerView_line_space, DensityUtils.dp2px(context, 10));

        lineGravity = typedArray.getInt(R.styleable.RulerView_line_gravity, 2);

        textSize = (int) typedArray.getDimension(R.styleable.RulerView_text_size, DensityUtils.dp2px(context, 18));

        textColor = typedArray.getColor(R.styleable.RulerView_text_color, Color.parseColor("#111111"));

        textGravity = typedArray.getInt(R.styleable.RulerView_text_gravity, 0);

        indicatorColor = typedArray.getColor(R.styleable.RulerView_indicator_color, Color.parseColor("#D62B20"));
        indicatorSize = (int) typedArray.getDimension(R.styleable.RulerView_indicator_size, DensityUtils.dp2px(context, 2));

        baseLineColor = typedArray.getColor(R.styleable.RulerView_base_line_color, Color.parseColor("#9B9B9B"));

        baseLineHeight = (int) typedArray.getDimension(R.styleable.RulerView_base_line_height, DensityUtils.dp2px(context, 2));

        baseLineGravity = typedArray.getInt(R.styleable.RulerView_base_line_gravity, 2);

        showBaseLine = typedArray.getBoolean(R.styleable.RulerView_show_base_line, true);

        maxLineCount = typedArray.getInt(R.styleable.RulerView_max_line_count, 10);
        midLineCount = typedArray.getInt(R.styleable.RulerView_mid_line_count, 5);

        typedArray.recycle();
    }


    /**
     * 初始化 画图相关的数据
     */
    protected void initDraw() {

        velocityTracker = VelocityTracker.obtain();

        scroller = new Scroller(getContext(), new DecelerateInterpolator(0.5f));
        fixScroller = new Scroller(getContext(), new LinearInterpolator());

        maxPaint = new Paint();

        maxPaint.setColor(maxLineColor);
        maxPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        maxPaint.setStrokeWidth(lineWidth);
        maxPaint.setAntiAlias(true);

        midPaint = new Paint();
        midPaint.setColor(midLineColor);
        midPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        midPaint.setStrokeWidth(lineWidth);
        midPaint.setAntiAlias(true);

        minPaint = new Paint();
        minPaint.setColor(minLineColor);
        minPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        minPaint.setStrokeWidth(lineWidth);
        minPaint.setAntiAlias(true);

        indicatorPain = new Paint();

        indicatorPain.setStyle(Paint.Style.FILL_AND_STROKE);
        indicatorPain.setStrokeWidth(indicatorSize);
        indicatorPain.setAntiAlias(true);
        indicatorPain.setColor(indicatorColor);


        bottomLinePaint = new Paint();

        bottomLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        bottomLinePaint.setStrokeWidth(baseLineHeight);
        bottomLinePaint.setAntiAlias(true);
        bottomLinePaint.setColor(baseLineColor);

        textPaint = new Paint();

        textPaint.setColor(textColor);
//        minPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        baseX = getMeasuredWidth() / 2 - lineWidth / 2;
        offsetX = 0;
        selPosition = 0;
    }

    /**
     * 初始化与数据相关的东西
     */
    protected void initData() {
        maxLineLengh = (lineSpace + lineWidth) * lineCount;
        lineCount = 100;
    }


    public void setData(List<String> data) {
        this.data = data;
        lineCount = data.size();
        postInvalidate();
    }

    public String getSelData() {
        return data.get(selPosition);
    }

    public int getSelPosition() {
        return selPosition;
    }

    public void setSelPosition(int selPosition) {
        this.selPosition = selPosition;
        offsetX = -selPosition * (lineSpace + lineWidth);
        scroller.forceFinished(true);
        fixScroller.forceFinished(true);
        postInvalidate();
    }


    protected int baseLineStartY;
    protected int minLineStartY;
    protected int midLineStartY;
    protected int maxLineStartY;
    protected int textStartY;

    /**
     * 根据属性计算 文字基线位置,各个刻度位置,绘制的 Y 坐标位置,计算后直接请求重绘
     */
    protected void computeYPostion() {
        if (textGravity == 0) {//文字在上面
            textStartY = textSize;
            maxLineStartY = getMeasuredHeight() - maxLineHeight;

            switch (lineGravity) {
                case 0:
                    midLineStartY = getMeasuredHeight() - maxLineHeight;
                    minLineStartY = getMeasuredHeight() - maxLineHeight;
                    break;
                case 1:
                    midLineStartY = getMeasuredHeight() - maxLineHeight / 2 - midLineHeight / 2;
                    minLineStartY = getMeasuredHeight() - maxLineHeight / 2 - minLineHeight / 2;
                    break;
                case 2:
                    midLineStartY = getMeasuredHeight() - midLineHeight;
                    minLineStartY = getMeasuredHeight() - minLineHeight;
                    break;
            }

            switch (baseLineGravity) {
                case 0:
                    baseLineStartY = getMeasuredHeight() - maxLineHeight;
                    break;
                case 1:
                    baseLineStartY = getMeasuredHeight() - maxLineHeight / 2;
                    break;
                case 2:
                    baseLineStartY = getMeasuredHeight();
                    break;
            }

        } else {//文字在下面
            textStartY = getMeasuredHeight();

            maxLineStartY = 0;

            switch (lineGravity) {
                case 0:
                    midLineStartY = 0;
                    minLineStartY = 0;
                    break;
                case 1:
                    midLineStartY = maxLineHeight / 2 - midLineHeight / 2;
                    minLineStartY = maxLineHeight / 2 - minLineHeight / 2;
                    break;
                case 2:
                    midLineStartY = maxLineHeight;
                    minLineStartY = maxLineHeight;
                    break;
            }

            switch (baseLineGravity) {
                case 0:
                    baseLineStartY = 0;
                    break;
                case 1:
                    baseLineStartY = maxLineHeight / 2;
                    break;
                case 2:
                    baseLineStartY = maxLineHeight;
                    break;
            }
        }
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Float textBaseY = ((float) textSize);
        if (offsetX > 0) {
            offsetX = 0;
        }

        if (offsetX < -maxLineLengh) {
            offsetX = -maxLineLengh;
        }

        for (int i = 0; i <= lineCount; i++) {

            float startX = baseX + i * (lineSpace + lineWidth) + offsetX;

            if (startX < 0) {
                continue;
            }

            if (startX > getMeasuredWidth()) {
                break;
            }

            if (i % maxLineCount == 0) {
                canvas.drawLine(startX, maxLineStartY,
                        startX, maxLineStartY + maxLineHeight, maxPaint);
                if (data != null && i < data.size()) {
                    float width = textPaint.measureText(data.get(i));
                    canvas.drawText(data.get(i), startX - width / 2, textStartY, textPaint);
                }

            } else if (i % midLineCount == 0) {
                canvas.drawLine(startX, midLineStartY,
                        startX, midLineStartY + midLineHeight, midPaint);
            } else {
                canvas.drawLine(startX, minLineStartY,
                        startX, minLineStartY + minLineHeight, minPaint);
            }

        }

        if (showBaseLine) {
            {//绘制基线
                canvas.drawLine(0, baseLineStartY - baseLineHeight / 2,
                        getMeasuredWidth(), baseLineStartY + baseLineHeight / 2, bottomLinePaint);
            }
        }


        {//绘制指示器
            canvas.drawLine(getMeasuredWidth() / 2 - lineWidth / 2, 0,
                    getMeasuredWidth() / 2 - lineWidth / 2, getMeasuredHeight(), indicatorPain);
        }

        int index = Math.abs(offsetX / (lineSpace + lineWidth));
        if (listener != null) {
            if (data != null) {
                if (index <= data.size() - 1) {
                    if (listener != null) {
                        listener.scroll(index, data.get(index));
                    }
                }
            }
        }

        if (scroller.isFinished() && fixScroller.isFinished()) {
            if (data != null) {
                if (index <= data.size() - 1) {
                    if (listener != null && isScrolled) {
                        listener.finish(index, data.get(index));
                        selPosition = index;
                        isScrolled = false;
                    }
                }
            }
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        // 在wrap_content的情况下默认长度为200dp
        int minWidthSize = DensityUtils.dp2px(getContext(), 200);
        int minHeightSize = DensityUtils.dp2px(getContext(), 50);
        // wrap_content的specMode是AT_MOST模式，这种情况下宽/高等同于specSize
        // 查表得这种情况下specSize等同于parentSize，也就是父容器当前剩余的大小
        // 在wrap_content的情况下如果特殊处理，效果等同martch_parent
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(minWidthSize, minHeightSize);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(minWidthSize, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, minHeightSize);
        }
        initDraw();
        initData();
        initLineHeight(DensityUtils.dp2px(getContext(), 50));
    }

    protected void initLineHeight(int i) {
        textSize = DensityUtils.dp2px(getContext(), 16);
        textPaint.setTextSize(textSize);
        maxLineHeight = (i - textSize) / 6 * 5;
        midLineHeight = (i - textSize) / 6 * 4;
        minLineHeight = (i - textSize) / 6 * 3;
        computeYPostion();
    }

    protected float lastX = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                scroller.forceFinished(true);
                fixScroller.forceFinished(true);
                velocityTracker.clear();
                velocityTracker.addMovement(event);
                lastX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                velocityTracker.addMovement(event);
                velocityTracker.computeCurrentVelocity(1000);
                float detX = event.getX() - lastX;
                if (Math.abs(detX) < 5) {
                    break;
                }
                lastX = event.getX();
                offsetX += detX;
//                scroller.startScroll(0, 0, (int) detX, 0);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                lastX = 0;
                startScroller();
                break;
            case MotionEvent.ACTION_CANCEL:
                releaseVelocityTracker();
        }
        return true;
    }

    protected void releaseVelocityTracker() {
        try {
            velocityTracker.recycle();
        } catch (IllegalStateException e) {

        }
    }

    protected int lastDef;
    protected int lastFixDef;

    /**
     * 开始平滑滚动
     */
    protected void startScroller() {
        isScrolled = true;
        lastDef = 0;
        scroller.fling(0, velocityTracker.getXVelocity() > 0 ? 1 : 0,
                Math.abs((int) velocityTracker.getXVelocity()), 0,
                0, Integer.MAX_VALUE,
                0, 0);

        releaseVelocityTracker();
        invalidate();
    }


    @Override
    public void computeScroll() {
        /**
         * 处理手指松开的平滑滚动
         */
        if (scroller.computeScrollOffset()) {
            int defX = scroller.getCurrX() - lastDef;
            lastDef = scroller.getCurrX();

            if (scroller.getCurrX() == scroller.getFinalX()) {

                int temp = lineWidth + lineSpace - Math.abs(offsetX % (lineSpace + lineWidth));
                lastFixDef = 0;
                // if (Math.abs(offsetX % (lineSpace + lineWidth)) > lineWidth + lineSpace / 2) {
                if (scroller.getStartY() < 1) {
//                    offsetX = ((offsetX / (lineSpace + lineWidth)) - 1) * (lineSpace + lineWidth);
                    fixScroller.startScroll(0, 0, defX + temp, 0, 500);
                } else {
//                    offsetX = ((offsetX / (lineSpace + lineWidth))) * (lineSpace + lineWidth);
                    fixScroller.startScroll(0, 1, defX + Math.abs(offsetX % (lineSpace + lineWidth)), 0, 500);
                }
                postInvalidate();
                scroller.forceFinished(true);
            } else {
                if (scroller.getStartY() > 0) {
                    offsetX += defX;
                } else {
                    offsetX -= defX;
                }
                postInvalidate();
            }
            return;
        }

        /**
         * 处理 缓慢移动到相关刻度,当 指示线在两个刻度中间的
         */
        if ((!scroller.computeScrollOffset()) && fixScroller.computeScrollOffset()) {
            int defX = fixScroller.getCurrX() - lastFixDef;
            lastFixDef = fixScroller.getCurrX();


            if (scroller.getStartY() > 0) {
                offsetX = offsetX + defX;
            } else {
                offsetX = offsetX - defX;
            }
            lastDef = 0;
            postInvalidate();
        }
        super.computeScroll();
    }

    public void setListener(RulerListener listener) {
        this.listener = listener;
    }

    //----------------------------Get/Set method------------还没有处理-----------------------

    public int getLineGravity() {
        return lineGravity;
    }

    public void setLineGravity(int lineGravity) {
        this.lineGravity = lineGravity;
        computeYPostion();
    }

    public int getTextGravity() {
        return textGravity;
    }

    public void setTextGravity(int textGravity) {
        this.textGravity = textGravity;
        computeYPostion();
    }

    public int getBaseLineColor() {
        return baseLineColor;
    }

    public void setBaseLineColor(int baseLineColor) {
        this.baseLineColor = baseLineColor;
        computeYPostion();
    }

    public int getBaseLineHeight() {
        return baseLineHeight;
    }

    public void setBaseLineHeight(int baseLineHeight) {
        this.baseLineHeight = baseLineHeight;
        computeYPostion();
    }

    public int getBaseLineGravity() {
        return baseLineGravity;
    }

    public void setBaseLineGravity(int baseLineGravity) {
        this.baseLineGravity = baseLineGravity;
        computeYPostion();
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        computeYPostion();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        computeYPostion();
    }

    public int getIndicatorSize() {
        return indicatorSize;
    }

    public void setIndicatorSize(int indicatorSize) {
        this.indicatorSize = indicatorSize;
        computeYPostion();
    }

    public int getIndicatorColor() {
        return indicatorColor;
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        computeYPostion();
    }

    public boolean isShowBaseLine() {
        return showBaseLine;
    }

    public void setShowBaseLine(boolean showBaseLine) {
        this.showBaseLine = showBaseLine;
        computeYPostion();
    }

    public int getLineCount() {
        return lineCount;
    }

    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;
        computeYPostion();
    }

    public int getLineSpace() {
        return lineSpace;
    }

    public void setLineSpace(int lineSpace) {
        this.lineSpace = lineSpace;
        computeYPostion();
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        computeYPostion();
    }

    public int getMaxLineColor() {
        return maxLineColor;
    }

    public void setMaxLineColor(int maxLineColor) {
        this.maxLineColor = maxLineColor;
        computeYPostion();
    }

    public int getMinLineColor() {
        return minLineColor;
    }

    public void setMinLineColor(int minLineColor) {
        this.minLineColor = minLineColor;
        computeYPostion();
    }

    public int getMidLineColor() {
        return midLineColor;
    }

    public void setMidLineColor(int midLineColor) {
        this.midLineColor = midLineColor;
        computeYPostion();
    }
}
