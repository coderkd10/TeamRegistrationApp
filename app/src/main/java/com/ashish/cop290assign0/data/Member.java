package com.ashish.cop290assign0.data;

import android.graphics.Bitmap;

import com.ashish.cop290assign0.utils.InputValidator;

import java.io.Serializable;

/**
 * Represents a member (i.e. student) who details is to be entered.
 * @author Abhishek Kedia
 */
public class Member implements Serializable {
    private String entryNumber;
    private String name;
    private Bitmap image;

    public static Member initialize() {
        Member member = new Member();
        member.entryNumber = "";
        member.name = "";
        member.image = null;
        return member;
    }

    public Member() {};
    public Member(String entryNumber, String name) throws IllegalArgumentException {
        this();
        this.setEntryNumber(entryNumber);
        this.setName(name);
    }
    public Member(Member member){
        this(member.getEntryNumber(), member.getName());
    }

    public String getEntryNumber() {
        return  entryNumber;
    }
    public String getName() {
        return name;
    }
    public Bitmap getImage() {
        return image;
    }

//    public void setEntryNumber(String entryNumber) throws IllegalArgumentException {
//        if(InputValidator.isValidEntryCodeStructure(entryNumber))
//            this.entryNumber = entryNumber;
//        else
//            throw new IllegalArgumentException("Structurally invalid entry number");
//    }
//    public void setName(String name) {
//        if(InputValidator.isValidName(name))
//            this.name = name;
//        else
//            throw new IllegalArgumentException("Invalid name");
//    }

    public Member setEntryNumber(String entryNumber) {
        this.entryNumber = entryNumber;
        return this;
    }
    public Member setName(String name) {
        this.name = name;
        return this;
    }
    public Member setImage(Bitmap image)
    {
        this.image = image;
        return this;
    }
    public String toString() {
        return String.format("{EntryNumber:%s, Name:%s, isImageNull:%b}",entryNumber,name,(image==null));
    }
}
