package com.laalsa.yumzy.androidcomponent.weekrangeselector;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.laalsa.yumzy.androidcomponent.R;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;


public class DateRangeCalendarView extends ViewPager {
    private CalendarWeekHolder[] calenderWeekBeans;
    private CalendarItemRangeView[] calenderItemViews;
    private int headerTextColor;
    private float headerTextSize;
    private int dateTextColor;
    private float dateTextSize;
    private int selectBackColor;
    private int selectTextColor;
    private float selectTextSize;
    public static Calendar selectedDate;

    public DateRangeCalendarView(Context context) {
        this(context, null);
    }

    public DateRangeCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        DateRangeCalendarView.selectedDate = Calendar.getInstance();
        init(attrs);
        setBackgroundColor(Color.LTGRAY);
    }

    private void init(AttributeSet attrs) {
        initAttrs(attrs);
        initCalenderView();
        setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return Integer.MAX_VALUE;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                position = position % 3;
                CalendarItemRangeView calenderItemView = calenderItemViews[position];
                calenderItemView.setDate(calenderWeekBeans[position].getYear(), calenderWeekBeans[position].getMonth(), calenderWeekBeans[position].getDay());
                calenderItemView.setSelectDate(Calendar.getInstance().get(Calendar.DATE));
                if (calenderItemView.getParent() != null) {
                    container.removeView(calenderItemView);
                }
                container.addView(calenderItemView);
                return calenderItemView;
            }

            @Override
            public void destroyItem(@NotNull ViewGroup container, int position, @NotNull Object object) {
//                super.destroyItem(container, position, object);
            }
        });
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setPositionCalender(calenderWeekBeans[position % 3], position);
                Objects.requireNonNull(getAdapter()).notifyDataSetChanged();
                if (onCalenderPageChangeListener != null) {
                    onCalenderPageChangeListener.onChange(getCurrentCalender());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        int currentPosition = Integer.MAX_VALUE / 2;
        int currentPosition = 0;
//        if (currentPosition % 3 == 2) {
//            currentPosition++;
//        } else if (currentPosition % 3 == 1) {
//            currentPosition--;
//        }
        setCurrentItem(currentPosition);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.DateRangeCalendarView);
        headerTextColor = typedArray.getColor(R.styleable.DateRangeCalendarView_headerTextColorCV, Color.GRAY);
        headerTextSize = typedArray.getFloat(R.styleable.DateRangeCalendarView_headerTextSizeCV, 12);
        dateTextColor = typedArray.getColor(R.styleable.DateRangeCalendarView_dateTextColorCV, Color.BLACK);
        dateTextSize = typedArray.getFloat(R.styleable.DateRangeCalendarView_dateTextSizeCV, 15);
        selectBackColor = typedArray.getColor(R.styleable.DateRangeCalendarView_selectBackColorCV, getResources().getColor(R.color.design_default_color_primary));
        selectTextColor = typedArray.getColor(R.styleable.DateRangeCalendarView_selectTextColorCV, Color.WHITE);
        selectTextSize = typedArray.getFloat(R.styleable.DateRangeCalendarView_dateTextSizeCV, 17);
        typedArray.recycle();
    }

    private void initCalenderView() {
        Calendar cal = Calendar.getInstance();
        CalendarWeekHolder calenderWeekBean = CalendarWeekUtil.getWeekUtilCalender(cal.getTime(), CalendarWeekUtil.getWeek(cal));
        calenderWeekBean = CalendarWeekUtil.getNewCalender(calenderWeekBean.getYear(), calenderWeekBean.getMonth(), calenderWeekBean.getDay());
        calenderWeekBeans = new CalendarWeekHolder[]{calenderWeekBean, CalendarWeekUtil.nextUnit(calenderWeekBean.getYear(), calenderWeekBean.getMonth(), calenderWeekBean.getMonth())
                , CalendarWeekUtil.previousUnit(calenderWeekBean.getYear(), calenderWeekBean.getMonth(), calenderWeekBean.getMonth())};
        calenderItemViews = new CalendarItemRangeView[calenderWeekBeans.length];
        for (int i = 0; i < calenderItemViews.length; i++) {
            CalendarItemRangeView calenderItemView = calenderItemViews[i] == null ? new CalendarItemRangeView(getContext()) : calenderItemViews[i];
            calenderItemView.setHeaderTextColor(headerTextColor);
            calenderItemView.setHeaderTextSize(headerTextSize);
            calenderItemView.setDateTextColor(dateTextColor);
            calenderItemView.setDateTextSize(dateTextSize);
            calenderItemView.setSelectBackColor(selectBackColor);
            calenderItemView.setSelectTextColor(selectTextColor);
            calenderItemView.setSelectTextSize(selectTextSize);
            calenderItemView.setOnItemSelectListener(new CalendarItemRangeView.OnItemSelectListener() {
                @Override
                public void onSelectDate(CalendarWeekHolder calenderWeekBean, Calendar date) {
                    if (onItemSelectListener != null) {
                        onItemSelectListener.onSelectDate(calenderWeekBean, date);
                    }
                }
            });
            calenderItemViews[i] = calenderItemView;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = 0;
        if (getAdapter() != null) {
            CalendarItemRangeView calenderItemView = (CalendarItemRangeView) getChildAt(0);
            if (calenderItemView != null) {
                height = calenderItemView.getMeasuredHeight();
            }
        }
        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    private void setPositionCalender(CalendarWeekHolder calenderWeekBean, int position) {
        position = position % 3;
        // the current month
        calenderWeekBeans[position] = calenderWeekBean;
        // position after the next month
        calenderWeekBeans[(position + 1) % 3] = CalendarWeekUtil.nextUnit(calenderWeekBean.getYear(), calenderWeekBean.getMonth(), calenderWeekBean.getDay());

        // the previous one is last month
        if (getCurrentItem() == 1||getCurrentItem() == 0) {
            Calendar cal = Calendar.getInstance();
            calenderWeekBeans[(position - 1 + 3) % 3] = CalendarWeekUtil.getNewCalender(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));;
        } else
            calenderWeekBeans[(position - 1 + 3) % 3] = CalendarWeekUtil.previousUnit(calenderWeekBean.getYear(), calenderWeekBean.getMonth(), calenderWeekBean.getDay());

        for (int i = 0; i < calenderWeekBeans.length; i++) {
            calenderItemViews[i].setDate(calenderWeekBeans[i].getYear(), calenderWeekBeans[i].getMonth(), calenderWeekBeans[i].getDay());

//            calenderItemViews[i].setSelectDate(-1);

            Calendar date = DateRangeCalendarView.selectedDate;
            int sldate = 0, slMonth = 0, slYear = 0;
            if (date != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date.getTime());
                sldate = calendar.get(Calendar.DATE);
                slMonth = calendar.get(Calendar.MONTH);
                slYear = calendar.get(Calendar.YEAR);
                Calendar[] dates = calenderWeekBean.getBeanDate();
                for (Calendar value : dates) {
                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTime(value.getTime());
                    int sldate2 = calendar2.get(Calendar.DATE);
                    int slMonth2 = calendar2.get(Calendar.MONTH);
                    int slYear2 = calendar2.get(Calendar.YEAR);
                    if (slYear == slYear2 && slMonth == slMonth2 && sldate == sldate2) {
                        calenderItemViews[i].setSelectDate(sldate);

                    }
                }
            }

        }
    }

    public CalendarWeekHolder getCurrentCalender() {
        return calenderWeekBeans[getCurrentItem() % 3];
    }

    public void setCurrentCalender(CalendarWeekHolder calenderBean) {
        calenderBean = CalendarWeekUtil.getNewCalender(calenderBean.getYear(), calenderBean.getMonth(), calenderBean.getDay());
        int result = calenderBean.compareTo(CalendarWeekUtil.getNewCalender(getCurrentCalender().getYear(), getCurrentCalender().getMonth(), getCurrentCalender().getDay()));
        if (result != 0) {
            calenderWeekBeans[(getCurrentItem() + result) % 3] = calenderBean;
            setPositionCalender(calenderBean, getCurrentItem() + result);
            setCurrentItem(getCurrentItem() + result);
        }
    }

    public CalendarWeekHolder getSelectDate() {
        CalendarWeekHolder CalenderWeekBean = calenderWeekBeans[getCurrentItem() % 3];
        CalenderWeekBean.setDay(calenderItemViews[getCurrentItem() % 3].getSelectDate());
        if (CalenderWeekBean.getDay() == -1) {
            return null;
        }
        return CalenderWeekBean;
    }

    public void setSelectDate(CalendarWeekHolder calender) {
        setCurrentCalender(calender);

        calenderItemViews[getCurrentItem() % 3].setSelectDate(calender.getDay());
    }

    private CalendarItemRangeView.OnItemSelectListener onItemSelectListener;

    public void setOnItemSelectListener(CalendarItemRangeView.OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    private OnCalenderPageChangeListener onCalenderPageChangeListener;

    public void setOnCalenderPageChangeListener(OnCalenderPageChangeListener onCalenderPageChangeListener) {
        this.onCalenderPageChangeListener = onCalenderPageChangeListener;
    }

    public interface OnCalenderPageChangeListener {
        void onChange(CalendarWeekHolder calenderWeekBean);
    }


}
