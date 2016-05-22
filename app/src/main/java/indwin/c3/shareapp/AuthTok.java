package indwin.c3.shareapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import indwin.c3.shareapp.FacebookAuth;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import static indwin.c3.shareapp.FacebookAuth.*;

/**
 * Created by Aniket Verma(Digo) on 3/21/2016.
 */
public  class AuthTok extends
        AsyncTask<String, Void, String> {

private String apiN="";
Context context;
    AuthTok(Context context) {
        this.context = context;
    }
//    Splash obj=new Splash();
    @Override
    protected String doInBackground(String... params) {
        JSONObject payload = new JSONObject();
        String urldisplay = params[0];
        apiN=urldisplay;
        try {

            // userid=12&productid=23&action=add
            // TYPE: POST
            //      payload.put("userid", details.get("userid"));
            // payload.put("productid", details.get("productid"));
            // payload.put("action", details.get("action"));


            HttpParams httpParameters = new BasicHttpParams();

            HttpConnectionParams
                    .setConnectionTimeout(httpParameters, 30000);

            HttpClient client = new DefaultHttpClient(httpParameters);
String urll=context.getString(R.string.server) + "authenticate";
            HttpPost httppost = new HttpPost(urll);
            httppost.setHeader("Authorization", "Basic YnVkZHlhcGlhZG1pbjptZW1vbmdvc2gx");

            HttpResponse response = client.execute(httppost);
            HttpEntity ent = response.getEntity();
            String responseString = EntityUtils.toString(ent, "UTF-8");
            if (response.getStatusLine().getStatusCode() != 200) {

                Log.e("MeshCommunication", "Server returned code "
                        + response.getStatusLine().getStatusCode());
                return "fail";
            } else {
                JSONObject resp = new JSONObject(responseString);

                if (resp.getString("status").contains("fail")) {

                    Log.e("MeshCommunication", "Server returned code "
                            + response.getStatusLine().getStatusCode());
                    return "fail";
                } else {
                    String token1="";

                    SharedPreferences userP = con.getSharedPreferences("token", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editorP = userP.edit();
                    token1 = resp.getString("token");
                    editorP.putString("token_value",token1);
                    editorP.putLong("expires", resp.getLong("expiresAt"));
                    editorP.commit();
                    return "win";

                }

            }

        } catch (Exception e) {
            Log.e("mesherror111", e.getMessage());
            return "fail";

        }
    }
    protected void onPostExecute(String result) {
        if(result.equals("win")){

         ViewForm next=new ViewForm();
          //  next.cc2();
//            next.new FacebookAuth.fblogin().execute();

//            next.fblogin().execute();


        }}}