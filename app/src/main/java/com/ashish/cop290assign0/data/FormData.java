package com.ashish.cop290assign0.data;

/**
 * Represents filled and saved form data.
 * @author Abhishek Kedia
 */
public class FormData {
    private Member[] members;

    public FormData() {
        members = new Member[3];
    }

    public Member getMember(int index) {
        return members[index];
    }
    public void setMember(int index, Member member) {
        members[index] = new Member(member);
    }
}
