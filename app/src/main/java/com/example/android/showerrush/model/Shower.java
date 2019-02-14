package com.example.android.showerrush.model;
import java.util.*;

public class Shower {

    private int id;
    private long length;
    private Date date;

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

    public void setDate(Date date) {
        this.date = date;
    }
}
