package com.laalsa.yumzy.androidcomponent.weekrangeselector;

import java.util.Calendar;


public class CalendarWeekHolder
{
    private int year;
    private int month;
    private int day;

    Calendar[] beanDate;


    public CalendarWeekHolder(int year, int month, int day, Calendar[] dateArray)
    {
        this.year = year;
        this.month = month;
        this.day = day;
        beanDate = dateArray;

    }


    public Calendar[] getBeanDate()
    {
        return beanDate;
    }

    public void setBeanDate(Calendar[] beanDate)
    {
        this.beanDate = beanDate;
    }


    public int getYear()
    {
        return year;
    }

    public void setYear(int year)
    {
        this.year = year;
    }

    public int getMonth()
    {
        return month;
    }

    public void setMonth(int month)
    {
        this.month = month;
    }

    public int getDay()
    {
        return day;
    }

    public void setDay(int day)
    {
        this.day = day;
    }

    @Override
    public String toString()
    {
        return String.format("%d-%d-%d", year, month + 1, day);
    }

    public int compareTo(CalendarWeekHolder calenderBean)
    {
        if (year != calenderBean.getYear())
        {
            return year > calenderBean.getYear() ? 1 : -1;
        }
        else if (month != calenderBean.getMonth())
        {
            return month > calenderBean.getMonth() ? 1 : -1;
        }
        else if (day != calenderBean.getDay())
        {
            return day > calenderBean.getDay() ? 1 : -1;
        }
        return 0;
    }


}
