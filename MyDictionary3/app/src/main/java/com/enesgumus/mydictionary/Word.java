package com.enesgumus.mydictionary;

public class Word {
    public String english;
    public String turkish;
    public int id;

    public Word(String english, String turkish, int id) {
        this.english = english;
        this.turkish = turkish;
        this.id = id;
    }

    public String getEnglish() {
        return english;
    }
    public String getTurkish() {
        return turkish;
    }

    public Integer getId() {
        return id;
    }


}

