package indwin.c3.shareapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

import indwin.c3.shareapp.R;

/**
 * Created by shubhang on 19/03/16.
 */
public class FetchNewToken extends
        AsyncTask<String, Void, String> {

    private Context mContext;
    int retryCount = 0;

    public FetchNewToken(Context context){
        mContext = context;
    }

    @Override
    protected String doInBackground(String... params) {

        JSONObject payload = new JSONObject();
        try {
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams
                    .setConnectionTimeout(httpParameters, 30000);
            HttpClient client = new DefaultHttpClient(httpParameters);
            HttpPost httppost = new HttpPost(mContext.getString(R.string.server) + "authenticate");
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
                    SharedPreferences userP = mContext.getSharedPreferences("buddy", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = userP.edit();
                    editor.putString("token_value", resp.getString("token"));
                    editor.commit();
                    return "win";
                }
            }
        } catch (Exception e) {
            Log.e("mesherror111", e.getMessage());
            return "fail";
        }
    }

    protected void onPostExecute(String result) {
        if (result.contains("fail")) {
            if(retryCount<3){
                retryCount++;
                new FetchNewToken(mContext).execute();
            }
            else{
                Toast.makeText(mContext,"Please try reoping the app!",Toast.LENGTH_LONG).show();
            }
        }
    }
}