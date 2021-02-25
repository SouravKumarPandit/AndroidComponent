package com.laalsa.yumzy.androidcomponent.weekrangeselector;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.laalsa.yumzy.androidcomponent.R;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class CalendarItemRangeView extends View /*implements View.OnClickListener*/ {
    private static final String TAG = "CalenderItemView";

    public CalendarItemRangeView(Context context) {
        this(context, null);
    }

    public CalendarItemRangeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarItemRangeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private Paint ringPaint;
    private Calendar todaysDate;
    private int dayOfWeek;
    private int year, month, day;
    private int itemWidth;
    private int itemHeight;
    private int rows = 3;
    private int preSelectDate = -1;
    //    private int selectDate = -1;
    public static int SELECTED_DATE = -1;

    //    private int[][] dates = new int[cols][rows];
//    private int[] s7dates = new int[rows];

    // head font color
    private int headerTextColor;
    // head font size
    private float headerTextSize;
    // head brush
    private Paint headerPaint;
    private Paint weekendPaint;
    private Paint currnetDatePaint;

    // Date font color
    private int dateTextColor;
    private int weekenddateTextColor;
    // Date font size
    private float dateTextSize;
    // date brush
    private Paint datePaint;

    // background color
    private int backColor;
    // check the background color
    private int selectBackColor;
    // Select the font color
    private int selectTextColor;
    // select the font size
    private float selectTextSize;
    // selected brush
    private Paint selectItemPaint;

    private Paint backgroundPaint;
    private TextPaint monthsPaint;
    private RectF backgroundRect;
    private Calendar[] weekDates;
    private int iCurrentDay;
    private int currentTextColor;
    private static int iSundayOn;

    private void init(Context context) {
//        setOnClickListener(this);
        initAttrs();
        initTools(context);
    }

    private void initAttrs() {
        headerTextColor = Color.BLACK;
        headerTextSize = 12;
//normal color
        dateTextColor = Color.BLACK;
        dateTextSize = 15;

//        for week day color
        weekenddateTextColor = Color.RED;
        //for currnet day color
        currentTextColor = getResources().getColor(R.color.design_default_color_primary);

        backColor = getDrawingCacheBackgroundColor();

//        selected day color
        selectBackColor = Color.BLUE;
        selectTextColor = Color.WHITE;
        selectTextSize = dateTextSize;
        Calendar cal = Calendar.getInstance();
//cal.setTime(new Date());//Set specific Date if you want to
        todaysDate = cal;
        dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        iCurrentDay = cal.get(Calendar.DATE);
        setSelectDate(cal.get(Calendar.DATE));


        CalendarWeekHolder calenderBean = CalendarWeekUtil.getWeekUtilCalender(cal.getTime(), CalendarWeekUtil.getWeek(cal));
        this.year = calenderBean.getYear();
        this.month = calenderBean.getMonth();
        this.day = calenderBean.getDay();
        setDate(calenderBean.getYear(), calenderBean.getMonth(), calenderBean.getDay());
    }

    private void initTools(Context context) {
        //top text
        ringPaint = new Paint();
        ringPaint.setColor(getResources().getColor(R.color.design_default_color_primary));
//        ringPaint.setColor(Color.LTGRAY);
        ringPaint.setStrokeWidth(3);
        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setAntiAlias(true);

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(getResources().getColor(android.R.color.white));
//        backgroundPaint.setColor( Color.parseColor("#FF963DB9"));
        monthsPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        monthsPaint.setTextSize(sp2px(dateTextSize));//dateTextSize * 1.2f
        monthsPaint.setColor(ContextCompat.getColor(context, android.R.color.darker_gray));

        backgroundRect = new RectF();

        // initialize the head brush
        headerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        headerPaint.setColor(headerTextColor);
        headerPaint.setTextSize(sp2px(headerTextSize));

        // Initialize the specific date brush
        datePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        datePaint.setColor(dateTextColor);
        datePaint.setTextSize(sp2px(dateTextSize));

        weekendPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        weekendPaint.setColor(weekenddateTextColor);
        weekendPaint.setTextSize(sp2px(dateTextSize));


        currnetDatePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        currnetDatePaint.setColor(currentTextColor);
        currnetDatePaint.setTextSize(sp2px(selectTextSize));
        // Initialize the selected brush
        selectItemPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectItemPaint.setColor(selectBackColor);
        selectItemPaint.setStrokeWidth(selectTextColor);
        selectItemPaint.setTextSize(sp2px(selectTextSize));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.EXACTLY));
//        int height=MeasureSpec.getSize(MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.EXACTLY));
        itemWidth = width / rows;
        itemHeight = itemWidth / rows;
        setMeasuredDimension(itemWidth * rows, itemHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDayItem(canvas);
    }

    private void drawTodaysDate(Canvas canvas, String text, int centerX, int centerY) {
        ringPaint.setStyle(Paint.Style.STROKE);
//        canvas.drawCircle(centerX, centerY, (float) (Math.min(itemWidth, itemHeight) / 4), ringPaint);
        canvas.drawRoundRect(0, 0, itemWidth, itemHeight, centerX, centerY, ringPaint);
//        canvas.drawCircle(centerX, centerY, (float) (Math.min(itemWidth, itemHeight) / 2.6), selectItemPaint);

    }

    private void drawOneText(Canvas canvas, String text, int centerX, int centerY, Paint paint) {
        float textWidth = paint.measureText(text);
        canvas.drawText(text, centerX - textWidth / 2, centerY - (paint.descent() + paint.ascent()) / 2, paint);
    }


    private void drawDayItem(Canvas canvas) {
        int dayItemY = itemHeight / 2;
        for (int j = 0; j < rows; j++) {

            if (weekDates[j].get(Calendar.DATE) == SELECTED_DATE) {
                drawSelectItem(canvas, getDateRange(weekDates[j]), itemWidth * j + itemWidth / 2, dayItemY, j);
            } else /*if (weekDates[j].get(Calendar.DATE)> 0)*/ {
                drawOneText(canvas, getDateRange(weekDates[j]), itemWidth * j + itemWidth / 2, dayItemY, datePaint);
            }

//            if (todaysDate.get(Calendar.YEAR) == year && todaysDate.get(Calendar.MONTH) == month && todaysDate.get(Calendar.DATE) == day && j == dayOfWeek)
//                drawTodaysDate(canvas, getDateRange(weekDates[j]), itemWidth * j + itemWidth / 2, dayItemY);


        }


    }

    @NotNull
    private String getDateRange(Calendar dates) {
        Calendar nextDay = Calendar.getInstance();
        nextDay.setTime(dates.getTime());
        nextDay.add(Calendar.DATE, 7);

        return dates.get(Calendar.DATE) + " " + YearMonth.values()[dates.get(Calendar.MONTH)] +
                " - " + nextDay.get(Calendar.DATE) + " " + YearMonth.values()[nextDay.get(Calendar.MONTH)];
    }

    private void drawSunText(Canvas canvas, String text, int centerX, int centerY, Paint datePaint) {
        float textWidth = datePaint.measureText(text);
        canvas.drawText(text, centerX - textWidth / 2, centerY - (datePaint.descent() + datePaint.ascent()) / 2, datePaint);

    }

    private void drawSelectItem(Canvas canvas, String text, int centerX, int centerY, int pos) {
//        selectItemPaint.setColor(isSelect ? selectBackColor : backColor);
        selectItemPaint.setColor(selectBackColor);
//        canvas.drawCircle(centerX, centerY, (float) (Math.min(itemWidth, itemHeight) / 4), selectItemPaint);
        canvas.drawRoundRect((int) (centerX - itemWidth / 2.0 - dateTextSize), 0, itemWidth*(pos+1), itemHeight, 0, 0, selectItemPaint);
        selectItemPaint.setColor(selectTextColor);
        drawOneText(canvas, text, centerX, centerY, selectItemPaint);
    }

    private PointF startPoint;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startPoint = new PointF(event.getX(), event.getY());
//            Log.i(TAG,startPoint.toString());
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();
//            Log.i(TAG,x+","+y);
            if (Math.abs(startPoint.x - x) < 20 && Math.abs(startPoint.y - y) < 20) {
//                for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
                    if (x > itemWidth * j && x < itemWidth * (j + 1)) {
                        preSelectDate = SELECTED_DATE;
                        SELECTED_DATE = weekDates[j].get(Calendar.DATE);
                        DateRangeCalendarView.selectedDate = weekDates[j];
                        if (onItemSelectListener != null) {
                            onItemSelectListener.onSelectDate(CalendarWeekUtil.getNewCalender(year, month, SELECTED_DATE), weekDates[j]);
                        }
                        invalidate();
                        break;
                    }
//                    }
                }
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    public void setDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        initDates(year, month, day);
        invalidate();
    }

    private void initDates(int year, int month, int day) {
        CalendarWeekHolder calenderBean = CalendarWeekUtil.getNewCalender(year, month, day);
        weekDates = calenderBean.getBeanDate();
        for (int i = 0; i < rows; i++) {

            Calendar cal = Calendar.getInstance();
            cal.setTime(weekDates[i].getTime());
            int tempDay = cal.get(Calendar.DAY_OF_MONTH);
//            s7dates[i] = tempDay;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(weekDates[0].getTime());
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            iSundayOn = 6;
        }
        setSelectDate(-1);

    }

    public CalendarWeekHolder getCalenderBean() {
        return CalendarWeekUtil.getNewCalender(year, month, day);
    }

    public int getSelectDate() {
        return SELECTED_DATE;
    }


    public void setSelectDate(int selectDate) {
        SELECTED_DATE = selectDate;
        invalidate();
    }

    public int sp2px(float sp) {
        Resources r = Resources.getSystem();
        final float scale = r.getDisplayMetrics().density;
        return (int) (sp * scale + 0.5f);
    }

    public int getHeaderTextColor() {
        return headerTextColor;
    }

    public void setHeaderTextColor(int headerTextColor) {
        this.headerTextColor = headerTextColor;
        initTools(getContext());
        invalidate();
    }

    public float getHeaderTextSize() {
        return headerTextSize;
    }

    public void setHeaderTextSize(float headerTextSize) {
        this.headerTextSize = headerTextSize;
        initTools(getContext());
        invalidate();
    }

    public int getDateTextColor() {
        return dateTextColor;
    }

    public void setDateTextColor(int dateTextColor) {
        this.dateTextColor = dateTextColor;
        initTools(getContext());
        invalidate();
    }

    public float getDateTextSize() {
        return dateTextSize;
    }

    public void setDateTextSize(float dateTextSize) {
        this.dateTextSize = dateTextSize;
        initTools(getContext());
        invalidate();
    }

    public int getSelectBackColor() {
        return selectBackColor;
    }

    public void setSelectBackColor(int selectBackColor) {
        this.selectBackColor = selectBackColor;
        initTools(getContext());
        invalidate();
    }

    public int getSelectTextColor() {
        return selectTextColor;
    }

    public void setSelectTextColor(int selectTextColor) {
        this.selectTextColor = selectTextColor;
        initTools(getContext());
        invalidate();
    }

    public float getSelectTextSize() {
        return selectTextSize;
    }

    public void setSelectTextSize(float selectTextSize) {
        this.selectTextSize = selectTextSize;
        initTools(getContext());
        invalidate();
    }

    private OnItemSelectListener onItemSelectListener;

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }


    public interface OnItemSelectListener {
        void onSelectDate(CalendarWeekHolder calenderBean, Calendar date);

    }

    public enum YearMonth {
        JAN, FEB, MAR, APR, MAY, JUN,
        JUL, AUG, SEP, OCT, NOV, DEC
    }
}
