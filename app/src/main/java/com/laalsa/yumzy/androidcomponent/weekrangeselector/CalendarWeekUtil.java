package com.laalsa.yumzy.androidcomponent.weekrangeselector;

import java.util.Calendar;
import java.util.Date;


public class CalendarWeekUtil
{
//    private static final Calendar todayDate=Calendar.getInstance();
    public static CalendarWeekHolder getWeekUtilCalender(Date date, Calendar[] sevenDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        CalendarWeekHolder calenderBean = new CalendarWeekHolder(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), sevenDays);
        return calenderBean;
    }

    public static CalendarWeekHolder getNewCalender(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
//        Date date = (Date) calendar.getTime().clone();
        return getWeekUtilCalender(calendar.getTime(), getWeek(calendar));
    }

    public static CalendarWeekHolder previousUnit(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date date = (Date) calendar.getTime().clone();
        Calendar prevCalendar = Calendar.getInstance();
        prevCalendar.setTime(date);
        prevCalendar.add(Calendar.DAY_OF_WEEK, -7*3);


        return getNewCalender(prevCalendar.get(Calendar.YEAR), prevCalendar.get(Calendar.MONTH), prevCalendar.get(Calendar.DAY_OF_MONTH));
    }

    public static CalendarWeekHolder nextUnit(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date date = (Date) calendar.getTime().clone();
        Calendar nextCalendar = Calendar.getInstance();
        nextCalendar.setTime(date);
        nextCalendar.add(Calendar.DAY_OF_WEEK, 7*3);
        return getNewCalender(nextCalendar.get(Calendar.YEAR), nextCalendar.get(Calendar.MONTH), nextCalendar.get(Calendar.DAY_OF_MONTH));

    }

    public static Calendar[] getWeek(Calendar date) {
        Calendar[] week = new Calendar[3];

        Calendar cloneCal=  Calendar.getInstance();
        cloneCal.setTime(date.getTime());
        week[0] = cloneCal;

        cloneCal= (Calendar) cloneCal.clone();
        cloneCal.setTime(cloneCal.getTime());
        cloneCal.add(Calendar.DAY_OF_WEEK, 7);
        week[1] = cloneCal;

        cloneCal= (Calendar) cloneCal.clone();
        cloneCal.setTime(cloneCal.getTime());
        cloneCal.add(Calendar.DAY_OF_WEEK, 7);
        week[2] = cloneCal;


        return week;

    }

}
