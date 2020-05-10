package com.example.demo.utils;

import org.apache.commons.lang3.text.WordUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Utility {
    private ModelMapper modelMapper;

    public Utility(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public static Long convertToLongDate(String dateTime) {
        DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
        Long parsedDate = null;
        try {
            parsedDate = date.parse(dateTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parsedDate;

    }

    public static Long beginDayMidnight(Date date) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime().getTime();
    }

    // get Date object
    public static Date convertToTimeObject(String time) {
        DateFormat date = new SimpleDateFormat("hh:mm:ss");
        Date parsedDate = null;
        try {
            parsedDate = date.parse(time);
        } catch (ParseException e) {
            //TODO Auto-generated catch block
            e.printStackTrace();
        }
        return parsedDate;

    }

    public static Date convertToHourandMinutes(String time) {

        DateFormat date = new SimpleDateFormat("hh:mm");
        Date parsedDate = null;
        try {
            parsedDate = date.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return parsedDate;

    }

    // get Date object
    public static Date convertToDateObject(String dateTime) {
        DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
        Date parsedDate = null;
        try {
            if(parsedDate==null)
                return parsedDate;
            parsedDate = date.parse(dateTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parsedDate;

    }

    // format date
    public static String formatDate(Long dateTime) {
        DateFormat date = new SimpleDateFormat("MMMM dd,yyyy");
        date.format(dateTime);
        return date.format(dateTime);
    }

    // formate time
    public static String formateFullDateTime(Long dateTime) {
        DateFormat time = new SimpleDateFormat("MMMM dd,yyyy hh:mm a");
        time.format(dateTime);
        return time.format(dateTime);
    }

    // formate time
    public static String formateTime(Long dateTime) {
        DateFormat time = new SimpleDateFormat("hh:mm a");
        time.format(dateTime);
        return time.format(dateTime);
    }

    // formate time
    public static String formateShortTime(Long dateTime) {
        DateFormat time = new SimpleDateFormat("hh:mm ");
        time.format(dateTime);
        return time.format(dateTime);
    }

    public static Long beginDay(Date date) throws ParseException {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime().getTime();
    }

    public static Long endDay(Date date) throws ParseException {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime().getTime();

    }

    public static Long beginMonth(Date date) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime().getTime();
    }

    public static Long endMonth(Date date) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime().getTime();
    }

    public static Long beginYear(Date date) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime().getTime();
    }

    public static Long endYear(Date date) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, 11);
        cal.set(Calendar.DAY_OF_MONTH, cal.getMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime().getTime();
    }


    public static String extractFileName(String fileName) {
        String file_name;
        if (fileName.lastIndexOf("\\") >= 0) {

            file_name = ((new Date().getTime())) + fileName.substring(fileName.lastIndexOf("\\"));
        } else {

            file_name = (new Date().getTime()) + fileName.substring(fileName.lastIndexOf("\\") + 1);
        }
        return file_name;
    }

    public  String formatMoneyNumber(String money) {
        if (money != null && money != "") {
            Double amount = Double.parseDouble(money);
            DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
            return formatter.format(amount);
        }
        return "0";
    }

    public  List<Date> returnDateRangeList(String beginDate, String endDate) {
        List<Date> dateRangeList = new ArrayList<>();
        Date fromDate = convertToDateObject(beginDate);
        Date toDate = convertToDateObject(endDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(fromDate);
        while (cal.getTime().before(toDate)) {
            cal.add(Calendar.DATE, 1);
            dateRangeList.add(cal.getTime());
        }
        return dateRangeList;
    }


    public  String capitalizeFirstLetter(String str) {
        return WordUtils.capitalize(str);
    }


    public <T> T convertToDto(Object entityObject, final Class<T> tClass) {
        T postDto = modelMapper.map(entityObject, tClass);
        return postDto;
    }


    public <T> List<T> convertToDtoList(Object entityObjectList, final Class<T> tClass) {
        List<T> list = (List)entityObjectList;
        return  list.stream().map(e -> modelMapper.map(e, tClass)).collect(Collectors.toList());

    }

}
