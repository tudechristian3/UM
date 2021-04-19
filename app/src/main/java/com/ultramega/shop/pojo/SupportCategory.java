package com.ultramega.shop.pojo;


public class SupportCategory {

    private String supportTitle, supportDescription;

    public SupportCategory(){
        super();
    }

    public SupportCategory(String supportTitle, String supportDescription) {
        this.supportTitle = supportTitle;
        this.supportDescription = supportDescription;
    }

    public String getSupportTitle() {
        return supportTitle;
    }

    public void setSupportTitle(String supportTitle) {
        this.supportTitle = supportTitle;
    }

    public String getSupportDescription() {
        return supportDescription;
    }

    public void setSupportDescription(String supportDescription) {
        this.supportDescription = supportDescription;
    }
}
