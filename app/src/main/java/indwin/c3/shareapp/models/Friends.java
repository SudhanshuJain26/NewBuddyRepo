package indwin.c3.shareapp.models;

import java.io.Serializable;

/**
 * Created by sudhanshu on 24/6/16.
 */
public class Friends implements Serializable{
    String phone_Num;
    String email;
    boolean isSelected;

    public boolean isBuddy() {
        return buddy;
    }

    public void setBuddy(boolean buddy) {
        this.buddy = buddy;
    }

    public Friends(String phone_Num, String email, boolean buddy, boolean invited, String name) {
        this.phone_Num = phone_Num;
        this.email = email;
        this.buddy = buddy;
        this.invited = invited;
        this.name = name;
    }

    public boolean isInvited() {
        return invited;

    }

    public void setInvited(boolean invited) {
        this.invited = invited;
    }

    boolean buddy;
    boolean invited;

    public String getPhone_Num() {
        return phone_Num;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setPhone_Num(String phone_Num) {
        this.phone_Num = phone_Num;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String type;
    String name;

    public Friends(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Friends() {
    }

    public Friends(String email, String name, String phone_Num) {
        this.email = email;
        this.name = name;
        this.phone_Num = phone_Num;
    }

    public Friends(String phone_Num, String email, String type, String name) {
        this.phone_Num = phone_Num;
        this.email = email;
        this.type = type;

        this.name = name;
    }
}
