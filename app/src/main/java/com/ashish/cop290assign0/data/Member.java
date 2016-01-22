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
    public Bitmap getImage() {
        if(image == null)
            return null;
        return BitmapFactory.decodeByteArray(image, 0, image.length);
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
    //TODO take byte[]/base64 string as input
    public Member setImage(Bitmap imageBitmap)
    {
        //Log.d("--> member setImage","isImageNull:"+(image==null)+", isGivenImageNull:"+(imageBitmap==null));
        if(imageBitmap == null)
            this.image = null;
        else {
//            ByteBuffer buffer = ByteBuffer.allocate(imageBitmap.getByteCount());
//            imageBitmap.copyPixelsToBuffer(buffer);
//            this.image = buffer.array();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            image = stream.toByteArray();
        }
        //Log.d("--> member setImage","done. isImageNull:"+(image==null));
        return this;
    }
    public String toString() {
        return String.format("{EntryNumber:%s, Name:%s, isImageNull:%b}",entryNumber,name,(image==null));
    }
}
