package indwin.c3.shareapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import indwin.c3.shareapp.application.BuddyApplication;
import indwin.c3.shareapp.models.UserModel;
import io.intercom.android.sdk.Intercom;
import io.intercom.com.google.gson.Gson;

/**
 * Created by rock on 5/10/16.
 */
public class AppUtils {

    static HashMap<String, HashMap<String, String>> image = new HashMap<>();
    static HashMap<String, HashMap<String, String>> mrp1;
    static HashMap<String, HashMap<String, String>> fkid1;
    static HashMap<String, HashMap<String, String>> title;
    static HashMap<String, HashMap<String, String>> sellers;
    static HashMap<String, HashMap<String, String>> selling;
    static HashMap<String, HashMap<String, String>> category;
    static HashMap<String, HashMap<String, String>> subCategory;
    static HashMap<String, HashMap<String, String>> brand;
    public static final String APP_NAME = "buddy";
    public static final String USER_OBJECT = "UserObject";
    public static final String IMAGE = "image";
    public static final String POSITION = "position";
    public static final String SOURCE = "source";
    public static final String HEADING = "heading";
    public static final String TnC_URL = "https://hellobuddy.in/termsApp";
    public static int TIMEOUT_MILLISECS = 30000;

    public static boolean isNotEmpty(String checkString) {
        if (checkString != null && !checkString.trim().isEmpty()) {
            return true;
        }
        return false;


    }


    public static void checkDataForNormalUser(UserModel user, Gson gson, JSONObject data1) {
               try {
                   user.setEmailSent(false);
                   if (data1.opt("gender") != null)
                       user.setGender(data1.getString("gender"));
                   if (data1.opt("approvedBand") != null)
                       user.setApprovedBand(data1.getString("approvedBand"));
                   if (data1.opt("creditLimit") != null)
                       user.setCreditLimit(data1.getInt("creditLimit"));
                   if (data1.opt("totalBorrowed") != null) {
                       if (user.getCreditLimit() == 0) {
                           user.setAvailableCredit(0);
                       } else {
                           int available = user.getCreditLimit() - data1.getInt("totalBorrowed");
                           if (available > 0) {
                               user.setAvailableCredit(available);
                           } else {
                               user.setAvailableCredit(0);
                           }
                       }
                   } else {
                       user.setAvailableCredit(user.getCreditLimit());
                   }
                   if (data1.opt("totalCashback") != null)
                       user.setCashBack(data1.getInt("totalCashback"));
                   if (data1.opt("emailVerified") != null)
                       user.setEmailVerified(data1.getBoolean("emailVerified"));
                   if (data1.opt("formStatus") != null)
                       user.setFormStatus(data1.getString("formStatus"));
                   Map userMap = new HashMap<>();
                   if (data1.opt("profileStatus") != null) {
                       user.setProfileStatus(data1.getString("profileStatus"));
                       userMap.put("profileStatus", user.getProfileStatus());
                   }
                   if (data1.opt("status1K") != null) {
                       String status1K = data1.getString("status1K");
                       user.setStatus1K(status1K);
                       userMap.put("status1K", status1K);
                       if (Constants.STATUS.DECLINED.toString().equals(status1K) || Constants.STATUS.APPLIED.toString().equals(status1K) || Constants.STATUS.APPROVED.toString().equals(status1K))
                           user.setAppliedFor1k(true);
                       else user.setAppliedFor1k(false);
                   }
                   if (data1.opt("status7K") != null) {
                       String status7K = data1.getString("status7K");
                       user.setStatus7K(status7K);
                       userMap.put("status7K", status7K);
                       if (Constants.STATUS.DECLINED.toString().equals(status7K) || Constants.STATUS.APPLIED.toString().equals(status7K) || Constants.STATUS.APPROVED.toString().equals(status7K))
                           user.setAppliedFor7k(true);
                       else user.setAppliedFor7k(false);
                   }
                   if (data1.opt("status60K") != null) {
                       String status60K = data1.getString("status60K");
                       user.setStatus60K(status60K);
                       userMap.put("status60K", status60K);
                       if (Constants.STATUS.DECLINED.toString().equals(status60K) || Constants.STATUS.APPLIED.toString().equals(status60K) || Constants.STATUS.APPROVED.toString().equals(status60K))
                           user.setAppliedFor60k(true);
                       else user.setAppliedFor60k(false);
                   }
                   Intercom.client().updateUser(userMap);


                   if (data1.opt("gpa") != null) {

                       user.setGpa(data1.getString("gpa"));
                   }
                   if (data1.opt("gpaType") != null) {

                       user.setGpaType(data1.getString("gpaType"));
                   }
                   if (data1.opt("fbConnected") != null)
                       user.setIsFbConnected(Boolean.parseBoolean(data1.getString("fbConnected")));
                   if (data1.opt("college") != null)
                       user.setCollegeName(data1.getString("college"));
                   if (data1.opt("course") != null)
                       user.setCourseName(data1.getString("course"));
                   if (data1.opt("courseCompletionDate") != null)
                       user.setCourseEndDate(data1.getString("courseCompletionDate"));
                   if (data1.opt("collegeIDs") != null)
                       user.setCollegeIds(gson.fromJson(data1.getString("collegeIDs"), ArrayList.class));


                   if (data1.opt("addressProofs") != null)
                       user.setAddressProofs(gson.fromJson(data1.getString("addressProofs"), ArrayList.class));
                   if (data1.opt("panOrAadhar") != null) {
                       user.setPanOrAadhar(data1.getString("panOrAadhar"));
                       if ("PAN".equals(user.getPanOrAadhar()))
                           user.setPanNumber(data1.getString("pan"));
                       else
                           user.setAadharNumber(data1.getString("aadhar"));
                   }
                   if (data1.opt("dob") != null)
                       user.setDob(data1.getString("dob"));
                   if (data1.opt("accomodation") != null)
                       user.setAccommodation(data1.getString("accomodation"));
                   if (data1.opt("currentAddress") != null)
                       user.setCurrentAddress(data1.getJSONObject("currentAddress").getString("line1"));
                   if (data1.opt("permanentAddress") != null)
                       user.setPermanentAddress(data1.getJSONObject("permanentAddress").getString("line1"));
                   if (data1.opt("rollNumber") != null)
                       user.setRollNumber(data1.getString("rollNumber"));

                   if (data1.opt("rejectionReason") != null)
                       user.setRejectionReason(data1.getString("rejectionReason"));
                   if (data1.optJSONArray("familyMember") != null) {
                       JSONArray familyMembers = data1.getJSONArray("familyMember");
                       for (int i = 0; i < familyMembers.length(); i++) {
                           JSONObject familyJson = familyMembers.getJSONObject(i);
                           if (i == 1) {
                               if (familyJson.getString("relation") != null) {
                                   user.setFamilyMemberType2(familyJson.getString("relation"));
                                   user.setProfessionFamilyMemberType2(familyJson.getString("occupation"));
                                   user.setPhoneFamilyMemberType2(familyJson.getString("phone"));
                                   if (familyJson.opt("preferredLanguage") != null)
                                       user.setPrefferedLanguageFamilyMemberType2(familyJson.getString("preferredLanguage"));
                               }
                           } else {
                               if (familyJson.getString("relation") != null) {
                                   user.setFamilyMemberType1(familyJson.getString("relation"));
                                   user.setProfessionFamilyMemberType1(familyJson.getString("occupation"));
                                   user.setPhoneFamilyMemberType1(familyJson.getString("phone"));
                                   if (familyJson.opt("preferredLanguage") != null)
                                       user.setPrefferedLanguageFamilyMemberType1(familyJson.getString("preferredLanguage"));
                               }
                           }
                       }
                   }

                   if (data1.opt("bankAccountNumber") != null)
                       user.setBankAccNum(data1.getString("bankAccountNumber"));
                   if (data1.opt("bankIFSC") != null)
                       user.setBankIfsc(data1.getString("bankIFSC"));
                   if (data1.opt("friendName") != null)
                       user.setClassmateName(data1.getString("friendName"));
                   if (data1.opt("friendNumber") != null)
                       user.setClassmatePhone(data1.getString("friendNumber"));
                   if (data1.opt("collegeIdVerificationDate") != null)
                       user.setVerificationDate(data1.getString("collegeIdVerificationDate"));
                   if (data1.opt("annualFees") != null)
                       user.setAnnualFees(data1.getString("annualFees"));
                   if (data1.opt("scholarship") != null)
                       user.setScholarship(String.valueOf(data1.getString("scholarship")));
                   if (data1.opt("scholarshipProgram") != null)
                       user.setScholarshipType(data1.getString("scholarshipProgram"));
                   if (data1.opt("scholarshipAmount") != null)
                       user.setScholarshipAmount(data1.getString("scholarshipAmount"));
                   if (data1.opt("takenLoan") != null)
                       user.setStudentLoan(data1.getString("takenLoan"));
                   if (data1.opt("monthlyExpense") != null)
                       user.setMonthlyExpenditure(data1.getString("monthlyExpense"));
                   if (data1.opt("ownVehicle") != null)
                       user.setVehicle(String.valueOf(data1.getString("ownVehicle")));
                   if (data1.opt("vehicleType") != null)
                       user.setVehicleType(data1.getString("vehicleType"));
                   if (data1.opt("bankStatements") != null)
                       user.setBankStmts(gson.fromJson(data1.getString("bankStatements"), ArrayList.class));
                   if (data1.opt("bankProofs") != null)
                       user.setBankProofs(gson.fromJson(data1.getString("bankProofs"), ArrayList.class));
                   if (data1.opt("selfie") != null) {
                       JSONArray array = data1.getJSONArray("selfie");
                       user.setSelfie((String) array.get(0));
                   } else {
                       user.setSelfie("");
                   }
                   if (data1.opt("signature") != null) {
                       JSONArray array = data1.getJSONArray("signature");
                       user.setSignature((String) array.get(0));
                   } else {
                       user.setSignature("");
                   }
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }

    public static HttpResponse connectToServerGet(String url, String x_access_token, String basicAuth) {

        HttpParams httpParameters = new BasicHttpParams();

        HttpConnectionParams
                .setConnectionTimeout(httpParameters, 30000);

        HttpClient client = new DefaultHttpClient(httpParameters);
        //                String url2="http://54.255.147.43:80/api/user/form?phone="+sh_otp.getString("number","");

        HttpGet httppost = new HttpGet(url);


        httppost.setHeader("x-access-token", x_access_token);


        httppost.setHeader("Content-Type", "application/json");


        try {
            HttpResponse response = client.execute(httppost);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }


    public static HttpResponse connectToServerPost(String url, String json, String x_access_token) {
        HttpParams httpParameters = new BasicHttpParams();

        HttpConnectionParams
                .setConnectionTimeout(httpParameters, 30000);

        HttpClient client = new DefaultHttpClient(httpParameters);

        HttpPost httppost = new HttpPost(url);
        httppost.setHeader("Authorization", "Basic YnVkZHlhcGlhZG1pbjptZW1vbmdvc2gx");
        httppost.setHeader("Content-Type", "application/json");
        if (isNotEmpty(x_access_token)) {

            httppost.setHeader("x-access-token", x_access_token);
        }

        try {
            if (isNotEmpty(json)) {
                StringEntity entity = new StringEntity(json);
                httppost.setEntity(entity);
            }

            HttpResponse response = client.execute(httppost);
            return response;
        } catch (IOException e) {
          e.printStackTrace();
        }
        return null;
    }

    public static UserModel getUserObject(Context context) {
        String json = getFromSharedPrefs(context, USER_OBJECT);
        return new Gson().fromJson(json, UserModel.class);
    }

    public static void saveUserObject(Context context, UserModel userModel) {
        String userJson = new Gson().toJson(userModel);
        saveInSharedPrefs(context, USER_OBJECT, userJson);
    }

    public static boolean isEmpty(String value) {
        return !isNotEmpty(value);
    }

    public static enum uploadStatus {
        OPEN {
            @Override
            public String toString() {

                return "open";
            }
        },
        PICKED {
            @Override
            public String toString() {

                return "picked";
            }
        }, UPLOADED {
            @Override
            public String toString() {

                return "uploaded";
            }
        }

    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static String getFromSelectedSharedPrefs(Context context, String key, String sharedPrefKey) {
        SharedPreferences editor = context.getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE);
        return editor.getString(key, "");

    }

    public static String getFromSharedPrefs(Context context, String key) {
        SharedPreferences editor = context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE);
        return editor.getString(key, "");

    }

    public static void saveInSharedPrefs(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();

    }

    public static void sendGoogleAnalytics(BuddyApplication application) {
        Tracker mTracker = application.getDefaultTracker();
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }


}
