package com.ashish.cop290assign0.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Log;

import com.ashish.cop290assign0.utils.InputValidator;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * Represents a member (i.e. student) who details is to be entered.
 * @author Abhishek Kedia
 */
public class Member implements Serializable {
    private String entryNumber;
    private String name;
    private byte[] image;

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
    public boolean hasImage() {
        return (image != null);
    }
    public Bitmap getImageBitmap() {
        if(image == null)
            return null;
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    public Member setEntryNumber(String entryNumber) {
        this.entryNumber = entryNumber;
        return this;
    }
    public Member setName(String name) {
        this.name = name;
        return this;
    }

    public Member setImage(byte[] image) {
        this.image = image;
        return this;
    }
//    public Member setImage(Bitmap imageBitmap)
//    {
//        if(imageBitmap == null)
//            this.image = null;
//        else {
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            image = stream.toByteArray();
//        }
//        return this;
//    }
    public String toString() {
        return String.format("{EntryNumber:%s, Name:%s, isImageNull:%b}",entryNumber,name,(image==null));
    }
}
