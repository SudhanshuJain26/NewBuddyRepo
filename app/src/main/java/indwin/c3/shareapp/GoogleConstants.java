package indwin.c3.shareapp;

/**
 * Created by sudhanshu on 27/6/16.
 */
public class GoogleConstants {

//    public static final String CLIENT_ID	= "875864589454-i3m057319aq6b66445uf7vbf08skr2a0.apps.googleusercontent.com";
    public static final String CLIENT_ID	= "253617796244-qf0bl3s11nnrpoo2q9enhfllvivv9q54.apps.googleusercontent.com";

//    public static final String CLIENT_SECRET 	= "ZBzLS2_3lP5j6k1NJlteHuVi";
    public static final String CLIENT_SECRET 	= "QIERh_87MKx8f6FOnCjHuhjp";

    public static String REDIRECT_URI = "http://localhost";
    public static String GRANT_TYPE = "authorization_code";
    public static String TOKEN_URL = "https://accounts.google.com/o/oauth2/token";
    public static String OAUTH_URL = "https://accounts.google.com/o/oauth2/auth";
    public static String OAUTH_SCOPE_CONTACTS = "https://www.googleapis.com/auth/contacts.readonly";
    public static String OAUTH_SCOPE_PROFILE = "https://www.googleapis.com/auth/userinfo.email";

    public static final String CONTACTS_URL = "https://www.google.com/m8/feeds/contacts/default/full?max-results=1000";
    public static final int MAX_NB_CONTACTS = 1000;
    public static final String APP = "OAuthGoogle";
}
