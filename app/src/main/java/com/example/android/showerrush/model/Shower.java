package com.example.android.showerrush.model;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Shower {

    private int id;
    private long length;
    private Date date;

    public Shower() {}

    public Shower(int id, long length, Date date) {
        this.id = id;
        this.length = length;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public Date getDate() {
        return date;
    }

    public String getStrDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public void setDate(String strDate) throws ParseException {
        String pattern = "yyyy-MM-dd hh:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date date = simpleDateFormat.parse(strDate);
        this.date = date;
    }
}
