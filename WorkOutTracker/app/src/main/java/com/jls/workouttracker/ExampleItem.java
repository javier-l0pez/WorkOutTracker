package com.jls.workouttracker;

public class ExampleItem {
    private int myImageResource;
    private String mText1;
    private String mText2;

    public ExampleItem(int imageResource, String text1, String text2) {
        myImageResource = imageResource;
        mText1 = text1;
        mText2 = text2;
    }

    public int getMyImageResource() {
        return myImageResource;
    }

    public String getmText1() {
        return mText1;
    }

    public String getmText2() {
        return mText2;
    }
}
