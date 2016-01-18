package com.ashish.cop290assign0.data;

/**
 * Represents filled and saved form data.
 * @author Abhishek Kedia
 */
public class FormData {
    private Member[] members;
    private String teamName;
    private boolean[] isFilled;

    public FormData() {
        members = new Member[3];
        isFilled = new boolean[4];
    }

    public static FormData initialize (String teamName, String[] names, String[] entryNumbers, String[] images, boolean[] isFilled){
        FormData formData = new FormData();
        formData.teamName = teamName;
        for(int i=0; i<3; i++) {
            Member member = new Member();
            member.setEntryNumber(entryNumbers[i+1]);
            member.setName(names[i+1]);
            member.setImage(images[i+1]);
            formData.members[i] = member;
        }
        formData.isFilled = isFilled;
        return formData;
    }

    public Member getMember(int index) {
        if(index <= 3 && index >= 1) {
            return members[index - 1]; //index-1 is a ugly hack
            //TODO change back to index
        } else {
            return new Member();
        }
    }
    public void setMember(int index, Member member) {
        members[index] = new Member(member);
    }
    public String getTeamName() {
        return teamName;
    }
    public boolean getIsFilled(int index) {
        return isFilled[index];
    }
}
