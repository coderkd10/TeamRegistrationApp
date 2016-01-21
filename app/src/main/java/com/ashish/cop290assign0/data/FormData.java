package com.ashish.cop290assign0.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents filled and saved form data.
 * @author Abhishek Kedia
 */
public class FormData implements Serializable{
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

    public static FormData initialize() {
        FormData formData = new FormData();
        formData.teamName = new String();
        for(int index = 0; index < formData.members.length; index++) {
            formData.members[index] = Member.initialize();
        }
        for(int index = 0; index < formData.isFilled.length; index++) {
            formData.isFilled[index] = false;
        }
        return  formData;
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
    public void setIsFilled(int index, boolean isFilled) {
        this.isFilled[index] = isFilled;
    }
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }


    public boolean isComplete() {
        return isFilled[0] && isFilled[1] && isFilled[2];
    }

    public Map<String, String> toMap(){
        if(!this.isComplete())
            return null;
        Map<String, String> data = new HashMap<String, String>();
        data.put("teamname", teamName);
        data.put("entry1", members[0].getEntryNumber());
        data.put("name1", members[0].getName());
        data.put("entry2", members[1].getEntryNumber());
        data.put("name2", members[1].getName());
        if(isFilled[3]) {
            data.put("entry3", members[2].getEntryNumber());
            data.put("name3", members[2].getName());
        } else {
            data.put("entry3","");
            data.put("name3","");
        }

        return data;
    }

}
