package indwin.c3.shareapp;

import java.util.List;

/**
 * Created by Aniket Verma(Digo) on 2/3/2016.
 */
public class Contacts
{
    String name , organisation,nickName,address;
//    Phone ph=new Phone();
    List<Phone> phone;
    List<Email> email;
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNick() {
        return nickName;
    }

    public void setNick(String nickName) {
        this.nickName = nickName;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getOrg() {
        return organisation;
    }

    public void setOrg(String organisation) {
        this.organisation = organisation;
    }

    public List<Phone> getPh() {
        return phone;
    }

    public void setPh(List<Phone> phone) {
        this.phone = phone;
    }

    public List<Email> getEm() {
        return email;
    }

    public void setEm(List<Email> email) {

        this.email = email;
    }
}

