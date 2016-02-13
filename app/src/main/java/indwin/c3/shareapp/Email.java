package indwin.c3.shareapp;

/**
 * Created by USER on 2/3/2016.
 */
public class Email {
    private String email;
    private String type;
    public String getemail() {
        return email;
    }

    public String getemail_type() {
        return type;
    }
    public void setemail(String email) {
        this.email = email;
    }

    public void setemail_type(String type) {
        this.type = type;
    }
}

