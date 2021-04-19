package com.ultramega.shop.pojo;


import android.os.Parcel;
import android.os.Parcelable;

public class MyIssues implements Parcelable {

    private String ticketNumber, category, status, subject, date;

    public MyIssues(){
        super();
    }

    public MyIssues(String ticketNumber, String category, String status, String subject, String date) {
        this.ticketNumber = ticketNumber;
        this.category = category;
        this.status = status;
        this.subject = subject;
        this.date = date;
    }

    private MyIssues(Parcel in) {
        ticketNumber = in.readString();
        category = in.readString();
        status = in.readString();
        subject = in.readString();
        date = in.readString();
    }

    public static final Creator<MyIssues> CREATOR = new Creator<MyIssues>() {
        @Override
        public MyIssues createFromParcel(Parcel in) {
            return new MyIssues(in);
        }

        @Override
        public MyIssues[] newArray(int size) {
            return new MyIssues[size];
        }
    };

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ticketNumber);
        dest.writeString(category);
        dest.writeString(status);
        dest.writeString(subject);
        dest.writeString(date);
    }
}
