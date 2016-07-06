package indwin.c3.shareapp.models;

/**
 * Created by rock on 6/24/16.
 */
public class Error {
    private String error;
    private String field;
    private FamilyMember value;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public FamilyMember getValue() {
        return value;
    }

    public void setValue(FamilyMember value) {
        this.value = value;
    }
}
