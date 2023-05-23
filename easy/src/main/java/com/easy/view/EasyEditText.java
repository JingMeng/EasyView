package com.easy.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.easy.view.listener.NumberKeyboardListener;
import com.easy.view.utils.EasyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 简化输入框，可用于密码框、验证码框
 * <p>
 * <p>
 * 绘制的很简单----代码量少，相比telegram的缺少动画
 * <p>
 * 没用到  setShowSoftInputOnFocus
 * <p>
 * 完全自定义，键盘在 onTouchEvent 弹起，所以不支持系统键盘
 */
public class EasyEditText extends View implements NumberKeyboardListener {

    /**
     * Context
     */
    private Context mContext;
    /**
     * 方框大小，因为它是相同的宽度和高度，它只需要一个值
     */
    private int mBoxWidth;
    /**
     * 方框背景颜色
     */
    private int mBoxBackgroundColor;
    /**
     * 方框默认描边颜色
     */
    private int mBoxStrokeColor;
    /**
     * 方框获取焦点描点颜色
     */
    private int mBoxFocusStrokeColor;
    /**
     * 方框描边大小
     */
    private final int mBoxStrokeWidth;
    /**
     * 文字颜色
     */
    private int mTextColor;
    /**
     * 文字大小
     */
    private float mTextSize;
    /**
     * 方框数量，最少4个 - 最多6个
     */
    private int mBoxNum;
    /**
     * 方框之间的间距
     */
    private int mBoxMargin = 4;
    /**
     * 方框画笔
     */
    private Paint mBoxPaint;
    /**
     * 方框描边画笔
     */
    private Paint mBoxStrokePaint;
    /**
     * 文字画笔
     */
    private Paint mTextPaint;
    /**
     * 文字矩形
     */
    private final Rect mTextRect = new Rect();
    /**
     * 方框圆角
     */
    private float mBoxCornerRadius = 8f;
    /**
     * 描边圆角
     */
    private float strokeRadius;

    /**
     * Input length
     */
    private final int mInputLength;
    /**
     * Input array
     */
    private final String[] inputArray;
    /**
     * Current input position
     */
    private int currentInputPosition = 0;

    private final List<RectF> focusList = new ArrayList<>();

    private boolean isFocus = false;
    /**
     * 是否密文显示
     */
    private boolean ciphertext = false;
    /**
     * 密文显示 *
     */
    private String ciphertextContent = "*";

    public EasyEditText(Context context) {
        this(context, null);
    }

    public EasyEditText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EasyEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        @SuppressLint("CustomViewStyleable")
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EasyEditText);
        mBoxWidth = (int) typedArray.getDimensionPixelSize(R.styleable.EasyEditText_boxWidth, 48);
        mBoxBackgroundColor = typedArray.getColor(R.styleable.EasyEditText_boxBackgroundColor, ContextCompat.getColor(context, R.color.white));
        mBoxStrokeColor = typedArray.getColor(R.styleable.EasyEditText_boxStrokeColor, ContextCompat.getColor(context, R.color.box_default_stroke_color));
        mBoxFocusStrokeColor = typedArray.getColor(R.styleable.EasyEditText_boxFocusStrokeColor, ContextCompat.getColor(context, R.color.box_default_stroke_color));
        mBoxStrokeWidth = (int) typedArray.getDimensionPixelSize(R.styleable.EasyEditText_boxStrokeWidth, 2);
        mTextColor = typedArray.getColor(R.styleable.EasyEditText_textColor, ContextCompat.getColor(context, R.color.tx_default_color));
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.EasyEditText_textSize, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
        int number = typedArray.getInteger(R.styleable.EasyEditText_boxNum, 4);
        ciphertext = typedArray.getBoolean(R.styleable.EasyEditText_ciphertext, false);
        mBoxNum = (number > 6 || number < 4) ? 4 : number;
        mInputLength = mBoxNum;
        inputArray = new String[mInputLength];
        typedArray.recycle();
        //初始化画笔
        initPaint();
    }

    /**
     * Initializing brush
     */
    private void initPaint() {
        //设置边框画笔
        mBoxPaint = new Paint();
        // anti-aliasing
        mBoxPaint.setAntiAlias(true);
        //Set color
        mBoxPaint.setColor(mBoxBackgroundColor);
        //Style filling
        mBoxPaint.setStyle(Paint.Style.FILL);
        //设置描边画笔
        mBoxStrokePaint = new Paint();
        mBoxStrokePaint.setAntiAlias(true);
        mBoxStrokePaint.setColor(mBoxStrokeColor);
        //Style stroke
        mBoxStrokePaint.setStyle(Paint.Style.STROKE);
        //Stroke width
        mBoxStrokePaint.setStrokeWidth(mBoxStrokeWidth);
        //设置文字画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(mTextColor);
        //Text size
        mTextPaint.setTextSize(mTextSize);
        //Center the text
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * 测量
     *
     * @param widthMeasureSpec  Width measurement
     * @param heightMeasureSpec Height measurement
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = 0;
        int margin = EasyUtils.dp2px(mContext, mBoxMargin);
        switch (MeasureSpec.getMode(widthMeasureSpec)) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:   //wrap_content
                width = mBoxWidth * mBoxNum + margin * (mBoxNum - 1);
                break;
            case MeasureSpec.EXACTLY:   //match_parent
                width = MeasureSpec.getSize(widthMeasureSpec);
                mBoxWidth = (width - margin * (mBoxNum - 1)) / mBoxNum;
                break;
        }
        //设置测量后的值
        setMeasuredDimension(width, mBoxWidth);
    }

    /**
     * 绘制
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //Draw box
        drawBox(canvas);
        //Draw Mac address
        drawMacAddress(canvas);
    }

    /**
     * 绘制方框
     */
    private void drawBox(Canvas canvas) {
        //The spacing of each box
        int margin = EasyUtils.dp2px(mContext, mBoxMargin);
        int radius = EasyUtils.dp2px(mContext, mBoxCornerRadius);
        //Draw a rounded rectangle border
        float strokeWidth = mBoxStrokeWidth / 2;
        for (int i = 0; i < mBoxNum; i++) {
            //To draw a rectangular box, you need the positions of the left, top, right and bottom points
            float left = i * mBoxWidth + i * margin;
            float top = 0f;
            float right = (i + 1) * mBoxWidth + i * margin;
            float bottom = mBoxWidth;
            RectF rectF = new RectF(left, top, right, bottom);
            //画一个圆角矩形框
            canvas.drawRoundRect(rectF, radius, radius, mBoxPaint);
            RectF strokeRectF = new RectF(left + strokeWidth, top + strokeWidth, right - strokeWidth, bottom - strokeWidth);
            //添加到列表
            focusList.add(strokeRectF);
        }

        for (int i = 0; i < mBoxNum; i++) {
            strokeRadius = radius - strokeWidth;
            if (i <= currentInputPosition && isFocus) {
                mBoxStrokePaint.setColor(mBoxFocusStrokeColor);
            } else {
                mBoxStrokePaint.setColor(mBoxStrokeColor);
            }
            canvas.drawRoundRect(focusList.get(i), strokeRadius, strokeRadius, mBoxStrokePaint);
        }
    }

    /**
     * Draw Mac address
     */
    private void drawMacAddress(Canvas canvas) {
        int boxMargin = EasyUtils.dp2px(mContext, mBoxMargin);
        for (int i = 0; i < inputArray.length; i++) {
            if (inputArray[i] != null) {
                //Drawn text
                String content = ciphertext ? ciphertextContent : inputArray[i];
                //Gets the drawn text boundary
                mTextPaint.getTextBounds(content, 0, content.length(), mTextRect);
                //Drawn position
                int offset = (mTextRect.top + mTextRect.bottom) / 2;
                //Draw text, need to determine the starting point of X, Y coordinate points
                //todo 没考虑文字大小，但是影响不大
                Log.i("EasyEditText", mTextRect.top + "========drawMacAddress===========" + mTextRect.bottom);
                //mTextRect 得到的是文字的贴边占用矩形
                //https://github.com/rengwuxian/HenCoderPlus/blob/master/06-drawing/src/main/java/com/hencoder/plus/view/SportsView.java
                float x = (float) (getPaddingLeft() + mBoxWidth * i + boxMargin * i + mBoxWidth / 2);
                float y = (float) (getPaddingTop() + mBoxWidth / 2) - offset;
                //Draw text
                canvas.drawText(content, x, y, mTextPaint);
            }
        }
    }

    /**
     * Touch event
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event != null) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                //显示数字键盘------ 不支持系统键盘
                EasyUtils.showNumKeyboardDialog(mContext, this);
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onNum(String num) {
        if (currentInputPosition == mInputLength) {
            return;
        }
        inputArray[currentInputPosition] = num;
        currentInputPosition++;
        //Refresh View
        postInvalidate();
    }

    @Override
    public void onDelete() {
        if (currentInputPosition == 0) {
            return;
        }
        currentInputPosition--;
        inputArray[currentInputPosition] = null;
        //Refresh View
        postInvalidate();
    }

    @Override
    public void onComplete() {
        Log.d("TAG", "onComplete: " + getText());
    }

    @Override
    public void onDialogShow() {
        isFocus = true;
        postInvalidate();
    }

    @Override
    public void onDialogDismiss() {
        isFocus = false;
        postInvalidate();
    }

    /**
     * 设置输入框个数
     */
    public void setBoxNum(int num) {
        if (num < 4 || num > 6) {
            throw new IllegalArgumentException("The number of input boxes ranges from 4 to 6");
        }
        mBoxNum = num;
    }

    /**
     * 获取输入总长度
     */
    public int getBoxNum() {
        return mBoxNum;
    }

    /**
     * 设置是否密文
     *
     * @param flag true 密文、false 明文
     */
    public void setCiphertext(boolean flag) {
        ciphertext = flag;
        postInvalidate();
    }

    /**
     * 设置密文时显示的内容
     *
     * @param content 密文内容，默认是 *
     */
    public void setCiphertextContent(String content) {
        if (content == null) {
            return;
        }
        if (content.isEmpty()) {
            return;
        }
        if (content.length() > 1) {
            return;
        }
        ciphertextContent = content;
    }

    /**
     * 获取输入内容
     */
    public String getText() {
        StringBuilder builder = new StringBuilder();
        for (String number : inputArray) {
            if (number == null) {
                continue;
            }
            if (number.isEmpty()) {
                continue;
            }
            builder.append(number);
        }
        return builder.toString();
    }
}
