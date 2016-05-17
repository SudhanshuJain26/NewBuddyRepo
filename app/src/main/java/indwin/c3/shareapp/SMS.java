package indwin.c3.shareapp;

/**
 * Created by buddy on 5/1/16.
 */
public class SMS {

    private String smsid;
    private String from;
    private String content;
    private String read; // "0" for have not read sms and "1" for have
    // read sms
    private String date;
    private String type;

    public String getId() {
        return smsid;
    }

    public String getAddress() {
        return from;
    }

    public String getMsg() {
        return content;
    }

    public String getReadState() {
        return read;
    }

    public String getTime() {
        return date;
    }

    public String getFolderName() {
        return type;
    }

    public void setId(String smsid) {
        this.smsid = smsid;
    }

    public void setAddress(String from) {
        this.from = from;
    }

    public void setMsg(String msg) {
        this.content = msg;
    }

    public void setReadState(String readState) {
        this.read = readState;
    }

    public void setTime(String time) {
        this.date = time;
    }

    public void setFolderName(String folderName) {
        this.type = folderName;
    }
}
