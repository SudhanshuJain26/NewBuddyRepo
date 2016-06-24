package indwin.c3.shareapp.utils;

/**
 * Created by rock on 6/24/16.
 */


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import indwin.c3.shareapp.R;

public class CustomProgressBar extends View {
    private int layout_height = 0;
    private int layout_width = 0;
    private int fullRadius = 100;
    private int circleRadius = 80;
    private int barLength = 60;
    private int barWidth = 20;
    private int rimWidth = 20;
    private int textSize = 20;
    private float contourSize = 0.0F;
    private int paddingTop = 5;
    private int paddingBottom = 5;
    private int paddingLeft = 5;
    private int paddingRight = 5;
    private int barColor = 1442840576;
    private int contourColor = 1442840576;
    private int rimColor = 1428300323;
    private int textColor = 16777216;
    private int circleColor = 0;
    private Paint barPaint = new Paint();
    private Paint circlePaint = new Paint();
    private Paint rimPaint = new Paint();
    private Paint textPaint = new Paint();
    private Paint contourPaint = new Paint();
    private RectF rectBounds = new RectF();
    private RectF circleBounds = new RectF();
    private RectF circleOuterContour = new RectF();
    private RectF circleInnerContour = new RectF();
    private int spinSpeed = 2;
    private int delayMillis = 0;
    int progress = 0;
    boolean isSpinning = false;
    private String text = "";
    private String[] splitText = new String[0];

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.ProgressWheel));
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        boolean size = false;
        int width = this.getMeasuredWidth();
        int height = this.getMeasuredHeight();
        int widthWithoutPadding = width - this.getPaddingLeft() - this.getPaddingRight();
        int heigthWithoutPadding = height - this.getPaddingTop() - this.getPaddingBottom();
        int size1;
        if (widthWithoutPadding > heigthWithoutPadding) {
            size1 = heigthWithoutPadding;
        } else {
            size1 = widthWithoutPadding;
        }

        this.setMeasuredDimension(size1 + this.getPaddingLeft() + this.getPaddingRight(), size1 + this.getPaddingTop() + this.getPaddingBottom());
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.layout_width = w;
        this.layout_height = h;
        this.setupBounds();
        this.setupPaints();
        this.invalidate();
    }

    private void setupPaints() {
        this.barPaint.setColor(this.barColor);
        this.barPaint.setAntiAlias(true);
        this.barPaint.setStyle(Paint.Style.STROKE);
        this.barPaint.setStrokeWidth((float) this.barWidth);
        this.rimPaint.setColor(this.rimColor);
        this.rimPaint.setAntiAlias(true);
        this.rimPaint.setStyle(Paint.Style.STROKE);
        this.rimPaint.setStrokeWidth((float) this.rimWidth);
        this.circlePaint.setColor(this.circleColor);
        this.circlePaint.setAntiAlias(true);
        this.circlePaint.setStyle(Paint.Style.FILL);
        this.textPaint.setColor(this.textColor);
        this.textPaint.setStyle(Paint.Style.FILL);
        this.textPaint.setAntiAlias(true);
        this.textPaint.setTextSize((float) this.textSize);
        this.contourPaint.setColor(this.contourColor);
        this.contourPaint.setAntiAlias(true);
        this.contourPaint.setStyle(Paint.Style.STROKE);
        this.contourPaint.setStrokeWidth(this.contourSize);
    }

    private void setupBounds() {
        int minValue = Math.min(this.layout_width, this.layout_height);
        int xOffset = this.layout_width - minValue;
        int yOffset = this.layout_height - minValue;
        this.paddingTop = this.getPaddingTop() + yOffset / 2;
        this.paddingBottom = this.getPaddingBottom() + yOffset / 2;
        this.paddingLeft = this.getPaddingLeft() + xOffset / 2;
        this.paddingRight = this.getPaddingRight() + xOffset / 2;
        int width = this.getWidth();
        int height = this.getHeight();
        this.rectBounds = new RectF((float) this.paddingLeft, (float) this.paddingTop, (float) (width - this.paddingRight), (float) (height - this.paddingBottom));
        this.circleBounds = new RectF((float) (this.paddingLeft + this.barWidth), (float) (this.paddingTop + this.barWidth), (float) (width - this.paddingRight - this.barWidth), (float) (height - this.paddingBottom - this.barWidth));
        this.circleInnerContour = new RectF(this.circleBounds.left + (float) this.rimWidth / 2.0F + this.contourSize / 2.0F, this.circleBounds.top + (float) this.rimWidth / 2.0F + this.contourSize / 2.0F, this.circleBounds.right - (float) this.rimWidth / 2.0F - this.contourSize / 2.0F, this.circleBounds.bottom - (float) this.rimWidth / 2.0F - this.contourSize / 2.0F);
        this.circleOuterContour = new RectF(this.circleBounds.left - (float) this.rimWidth / 2.0F - this.contourSize / 2.0F, this.circleBounds.top - (float) this.rimWidth / 2.0F - this.contourSize / 2.0F, this.circleBounds.right + (float) this.rimWidth / 2.0F + this.contourSize / 2.0F, this.circleBounds.bottom + (float) this.rimWidth / 2.0F + this.contourSize / 2.0F);
        this.fullRadius = (width - this.paddingRight - this.barWidth) / 2;
        this.circleRadius = this.fullRadius - this.barWidth + 1;
    }

    private void parseAttributes(TypedArray a) {
        this.barWidth = (int) a.getDimension(R.styleable.ProgressWheel_barWidth, (float) this.barWidth);
        this.rimWidth = (int) a.getDimension(R.styleable.ProgressWheel_rimWidth, (float) this.rimWidth);
        this.spinSpeed = (int) a.getDimension(R.styleable.ProgressWheel_spinSpeed, (float) this.spinSpeed);
        this.delayMillis = a.getInteger(R.styleable.ProgressWheel_delayMillis, this.delayMillis);
        if (this.delayMillis < 0) {
            this.delayMillis = 0;
        }

        this.barColor = a.getColor(R.styleable.ProgressWheel_barColor, this.barColor);
        this.barLength = (int) a.getDimension(R.styleable.ProgressWheel_bar_length, (float) this.barLength);
        this.textSize = (int) a.getDimension(R.styleable.ProgressWheel_textSize, (float) this.textSize);
        this.textColor = a.getColor(R.styleable.ProgressWheel_textColor, this.textColor);
        if (a.hasValue(R.styleable.ProgressWheel_text)) {
            this.setText(a.getString(R.styleable.ProgressWheel_text));
        }

        this.rimColor = a.getColor(R.styleable.ProgressWheel_rimColor, this.rimColor);
        this.circleColor = a.getColor(R.styleable.ProgressWheel_circleColor, this.circleColor);
        this.contourColor = a.getColor(R.styleable.ProgressWheel_contourColor, this.contourColor);
        this.contourSize = a.getDimension(R.styleable.ProgressWheel_contourSize, this.contourSize);
        a.recycle();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(this.circleBounds, 360.0F, 360.0F, false, this.circlePaint);
        canvas.drawArc(this.circleBounds, 360.0F, 360.0F, false, this.rimPaint);
        canvas.drawArc(this.circleOuterContour, 360.0F, 360.0F, false, this.contourPaint);
        canvas.drawArc(this.circleInnerContour, 360.0F, 360.0F, false, this.contourPaint);
        if (this.isSpinning) {
            canvas.drawArc(this.circleBounds, (float) (this.progress - 90), (float) this.barLength, false, this.barPaint);
        } else {
            canvas.drawArc(this.circleBounds, -90.0F, (float) this.progress, false, this.barPaint);
        }

        float textHeight = this.textPaint.descent() - this.textPaint.ascent();
        float verticalTextOffset = textHeight / 2.0F - this.textPaint.descent();
        String[] arr$ = this.splitText;
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            String s = arr$[i$];
            float horizontalTextOffset = this.textPaint.measureText(s) / 2.0F;
            canvas.drawText(s, (float) (this.getWidth() / 2) - horizontalTextOffset, (float) (this.getHeight() / 2) + verticalTextOffset, this.textPaint);
        }

        if (this.isSpinning) {
            this.scheduleRedraw();
        }

    }

    private void scheduleRedraw() {
        this.progress += this.spinSpeed;
        if (this.progress > 360) {
            this.progress = 0;
        }

        this.postInvalidateDelayed((long) this.delayMillis);
    }

    public boolean isSpinning() {
        return this.isSpinning;
    }

    public void resetCount() {
        this.progress = 0;
        this.setText("0%");
        this.invalidate();
    }

    public void stopSpinning() {
        this.isSpinning = false;
        this.progress = 0;
        this.postInvalidate();
    }

    public void spin() {
        this.isSpinning = true;
        this.postInvalidate();
    }

    public void incrementProgress() {
        this.isSpinning = false;
        ++this.progress;
        if (this.progress > 360) {
            this.progress = 0;
        }

        this.postInvalidate();
    }

    public void setProgress(int i) {
        this.isSpinning = false;
        this.progress = i;
        this.postInvalidate();
    }

    public void setText(String text) {
        this.text = text;
        this.splitText = this.text.split("\n");
    }

    public int getCircleRadius() {
        return this.circleRadius;
    }

    public void setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
    }

    public int getBarLength() {
        return this.barLength;
    }

    public void setBarLength(int barLength) {
        this.barLength = barLength;
    }

    public int getBarWidth() {
        return this.barWidth;
    }

    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
        if (this.barPaint != null) {
            this.barPaint.setStrokeWidth((float) this.barWidth);
        }

    }

    public int getTextSize() {
        return this.textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        if (this.textPaint != null) {
            this.textPaint.setTextSize((float) this.textSize);
        }

    }

    public int getPaddingTop() {
        return this.paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getPaddingBottom() {
        return this.paddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public int getPaddingLeft() {
        return this.paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public int getPaddingRight() {
        return this.paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public int getBarColor() {
        return this.barColor;
    }

    public void setBarColor(int barColor) {
        this.barColor = barColor;
        if (this.barPaint != null) {
            this.barPaint.setColor(this.barColor);
        }

    }

    public int getCircleColor() {
        return this.circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
        if (this.circlePaint != null) {
            this.circlePaint.setColor(this.circleColor);
        }

    }

    public int getRimColor() {
        return this.rimColor;
    }

    public void setRimColor(int rimColor) {
        this.rimColor = rimColor;
        if (this.rimPaint != null) {
            this.rimPaint.setColor(this.rimColor);
        }

    }

    public Shader getRimShader() {
        return this.rimPaint.getShader();
    }

    public void setRimShader(Shader shader) {
        this.rimPaint.setShader(shader);
    }

    public int getTextColor() {
        return this.textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        if (this.textPaint != null) {
            this.textPaint.setColor(this.textColor);
        }

    }

    public int getSpinSpeed() {
        return this.spinSpeed;
    }

    public void setSpinSpeed(int spinSpeed) {
        this.spinSpeed = spinSpeed;
    }

    public int getRimWidth() {
        return this.rimWidth;
    }

    public void setRimWidth(int rimWidth) {
        this.rimWidth = rimWidth;
        if (this.rimPaint != null) {
            this.rimPaint.setStrokeWidth((float) this.rimWidth);
        }

    }

    public int getDelayMillis() {
        return this.delayMillis;
    }

    public void setDelayMillis(int delayMillis) {
        this.delayMillis = delayMillis;
    }

    public int getContourColor() {
        return this.contourColor;
    }

    public void setContourColor(int contourColor) {
        this.contourColor = contourColor;
        if (this.contourPaint != null) {
            this.contourPaint.setColor(this.contourColor);
        }

    }

    public float getContourSize() {
        return this.contourSize;
    }

    public void setContourSize(float contourSize) {
        this.contourSize = contourSize;
        if (this.contourPaint != null) {
            this.contourPaint.setStrokeWidth(this.contourSize);
        }

    }
}
