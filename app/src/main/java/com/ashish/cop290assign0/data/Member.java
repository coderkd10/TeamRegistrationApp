package com.ashish.cop290assign0.data;

import com.ashish.cop290assign0.utils.InputValidator;

/**
 * Represents a member (i.e. student) who details is to be entered.
 * @author Abhishek Kedia
 */
public class Member {
    private String entryNumber;
    private String name;

    public Member() {};
    public Member(String entryNumber, String name) throws IllegalArgumentException {
        this();
        this.setEntryNumber(entryNumber);
        this.setName(name);
    }
    public Member(Member member){
        this(member.getEntryNumber(),member.getName());
    }

    public String getEntryNumber() {
        return  entryNumber;
    }
    public String getName() {
        return name;
    }

    public void setEntryNumber(String entryNumber) throws IllegalArgumentException {
        if(InputValidator.isValidEntryCodeStructure(entryNumber))
            this.entryNumber = entryNumber;
        else
            throw new IllegalArgumentException("Structurally invalid entry number");
    }
    public void setName(String name) {
        if(InputValidator.isValidName(name))
            this.name = name;
        else
            throw new IllegalArgumentException("Invalid name");
    }
}
