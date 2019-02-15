package com.example.android.showerrush.model;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Shower implements Serializable, Parcelable {

    private int id;
    private long length;
    private Date date;

    public Shower() {}

    public Shower(int id, long length, Date date) {
        this.id = id;
        this.length = length;
        this.date = date;
    }

    private Shower(Parcel in) throws ParseException {
        id = in.readInt();
        length = in.readLong();
        setDate(in.readString());
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

    public String getStrLength() {
        String strLength = "";
        final long min = TimeUnit.MILLISECONDS.toMinutes(length);
        final long sec = TimeUnit.MILLISECONDS.toSeconds(length)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(length));
        strLength += min + "m";
        if(sec > 0) strLength += " " + sec + "s";
        return strLength;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public Date getDate() {
        return date;
    }

    public String getStrDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public void setDate(String strDate) throws ParseException {
        String pattern = "dd-MM-yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date date = simpleDateFormat.parse(strDate);
        this.date = date;
    }

    public static final Parcelable.Creator<Shower> CREATOR =
            new Parcelable.Creator<Shower>() {

                @Override
                public Shower createFromParcel(Parcel source) {
                    try {
                        return new Shower(source);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                public Shower[] newArray(int size) {
                    return new Shower[size];
                }

            };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeLong(length);
        parcel.writeString(getStrDate());
    }
}
