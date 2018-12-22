package com.teamturing.dreamteam.MainFragments.Adapters;

public class RequestPojo {
    String name ,description ;
    String requesId;
            int profilePic;

    public RequestPojo(String name, String description, String requesId, int profilePic) {
        this.name = name;
        this.description = description;
        this.requesId = requesId;
        this.profilePic = profilePic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequesId() {
        return requesId;
    }

    public void setRequesId(String requesId) {
        this.requesId = requesId;
    }

    public int getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(int profilePic) {
        this.profilePic = profilePic;
    }
}
