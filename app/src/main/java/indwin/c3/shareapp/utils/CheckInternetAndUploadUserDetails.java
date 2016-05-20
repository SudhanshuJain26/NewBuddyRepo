package indwin.c3.shareapp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.models.UserModel;
import io.intercom.android.sdk.Intercom;

/**
 * Created by shubhang on 22/03/16.
 */
public class CheckInternetAndUploadUserDetails extends BroadcastReceiver {
    boolean isConnected;
    Cloudinary cloudinary;
    UserModel user;
    private String authToken;
    private Context mContext;
    SharedPreferences mPrefs;
    Gson gson;
    ArrayList<String> uploadCollegeIds, uploadAddressProofs, uploadBankStmts, uploadBankProofs;
    int retryCount = 0;
    String selfieUrl = "", signatureUrl = "";

    @Override
    public void onReceive(Context context, Intent arg1) {
        mContext = context;
        mPrefs = mContext.getSharedPreferences("buddy", Context.MODE_PRIVATE);
        if (!mPrefs.getBoolean("updatingDB", false)) {
            new AsyncTaskRunner().execute();
        }
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        private String resp;

        @Override
        protected String doInBackground(String... params) {
            try {
                InetAddress ipAddr = InetAddress.getByName("google.com");
                if (ipAddr.equals("")) {
                    isConnected = false;
                } else {
                    isConnected = true;
                }
            } catch (Exception e) {
                isConnected = false;
                resp = e.getMessage();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            if (isConnected) {
                mPrefs.edit().putBoolean("updatingDB", true).apply();
                gson = new Gson();
                String json = mPrefs.getString("UserObject", "");
                user = gson.fromJson(json, UserModel.class);
                Map config = new HashMap();
                config.put("cloud_name", "mesh");
                config.put("api_key", "541578479444757");
                config.put("api_secret", "1ITDPajCmIcYn2UBlE-kbwHkr5A");
                cloudinary = new Cloudinary(config);
                uploadCollegeIds = new ArrayList<>();
                uploadAddressProofs = new ArrayList<>();
                uploadBankStmts = new ArrayList<>();
                uploadBankProofs = new ArrayList<>();
                if (user != null)
                    new CloudinaryUploader().execute();
            }
        }
    }

    private class CloudinaryUploader extends AsyncTask<String, String, String> {
        boolean updateUser = false;

        @Override
        protected synchronized String doInBackground(String... params) {
            UserModel userImages = AppUtils.getUserObject(mContext);
            if (userImages.getNewCollegeIds() != null && user.getNewCollegeIds().size() > 0) {
                int i = 0;
                for (Map.Entry<String, String> entry : userImages.getNewCollegeIds().entrySet()) {
                    if (AppUtils.uploadStatus.OPEN.toString().equals(entry.getValue())) {
                        entry.setValue(AppUtils.uploadStatus.PICKED.toString());
                        AppUtils.saveUserObject(mContext, userImages);
                        i++;

                        try {
                            Long tsLong = System.currentTimeMillis() / 1000;
                            String ts = tsLong.toString();
                            cloudinary.uploader().upload(entry.getKey(),
                                    ObjectUtils.asMap("public_id", userImages.getUserId() + "collegeId" + ts + i));
                            String cloudinaryUrl = cloudinary.url().secure(true).generate(userImages.getUserId() + "collegeId" + ts + i);
                            uploadCollegeIds.add(cloudinaryUrl);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        updateUser = true;
                    }
                }
            }
            userImages = AppUtils.getUserObject(mContext);
            if (userImages.getNewAddressProofs() != null) {
                int i = 0;
                for (Map.Entry<String, String> entry : userImages.getNewAddressProofs().entrySet()) {
                    if (AppUtils.uploadStatus.OPEN.toString().equals(entry.getValue())) {
                        entry.setValue(AppUtils.uploadStatus.PICKED.toString());
                        AppUtils.saveUserObject(mContext, userImages);
                        i++;

                        try {
                            Long tsLong = System.currentTimeMillis() / 1000;
                            String ts = tsLong.toString();
                            cloudinary.uploader().upload(entry.getKey(),
                                    ObjectUtils.asMap("public_id", userImages.getUserId() + "addressProof" + ts + i));
                            String cloudinaryUrl = cloudinary.url().secure(true).generate(userImages.getUserId() + "addressProof" + ts + i);
                            uploadAddressProofs.add(cloudinaryUrl);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        updateUser = true;
                    }
                }
            }
            userImages = AppUtils.getUserObject(mContext);
            if (userImages.getNewBankStmts() != null) {
                int i = 0;
                for (Map.Entry<String, String> entry : userImages.getNewBankStmts().entrySet()) {
                    if (AppUtils.uploadStatus.OPEN.toString().equals(entry.getValue())) {
                        i++;
                        entry.setValue(AppUtils.uploadStatus.PICKED.toString());
                        AppUtils.saveUserObject(mContext, userImages);

                        try {
                            Long tsLong = System.currentTimeMillis() / 1000;
                            String ts = tsLong.toString();
                            cloudinary.uploader().upload(entry.getKey(),
                                    ObjectUtils.asMap("public_id", userImages.getUserId() + "bankStmt" + ts + i));
                            String cloudinaryUrl = cloudinary.url().secure(true).generate(userImages.getUserId() + "bankStmt" + ts + i);
                            uploadBankStmts.add(cloudinaryUrl);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        updateUser = true;
                    }
                }
            }
            userImages = AppUtils.getUserObject(mContext);
            if (userImages.getNewBankProofs() != null) {
                int i = 0;
                for (Map.Entry<String, String> entry : userImages.getNewBankProofs().entrySet()) {
                    if (AppUtils.uploadStatus.OPEN.toString().equals(entry.getValue())) {
                        i++;
                        entry.setValue(AppUtils.uploadStatus.PICKED.toString());
                        AppUtils.saveUserObject(mContext, userImages);

                        try {
                            Long tsLong = System.currentTimeMillis() / 1000;
                            String ts = tsLong.toString();
                            cloudinary.uploader().upload(entry.getKey(),
                                    ObjectUtils.asMap("public_id", userImages.getUserId() + "bankProof" + ts + i));
                            String cloudinaryUrl = cloudinary.url().secure(true).generate(userImages.getUserId() + "bankProof" + ts + i);
                            uploadBankProofs.add(cloudinaryUrl);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        updateUser = true;
                    }
                }
            }
            if (user.isUpdateSelfie()) {
                try {
                    Long tsLong = System.currentTimeMillis() / 1000;
                    String ts = tsLong.toString();
                    cloudinary.uploader().upload(user.getSelfie(),
                            ObjectUtils.asMap("public_id", user.getUserId() + "selfie" + ts));
                    selfieUrl = cloudinary.url().secure(true).generate(user.getUserId() + "selfie" + ts);
                    user.setSelfie(selfieUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                updateUser = true;
            }
            if (user.isUpdateSignature()) {
                try {
                    Long tsLong = System.currentTimeMillis() / 1000;
                    String ts = tsLong.toString();
                    cloudinary.uploader().upload(user.getSignature(),
                            ObjectUtils.asMap("public_id", user.getUserId() + "signature" + ts));
                    signatureUrl = cloudinary.url().secure(true).generate(user.getUserId() + "signature" + ts);
                    user.setSignature(signatureUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                updateUser = true;
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            if (updateUser) {
                String json = gson.toJson(user);
                mPrefs.edit().putString("UserObject", json).apply();
                new UploadCoudinaryToServer().execute();
            }
            new UploadDetailsToServer().execute();
        }
    }


    private class UploadCoudinaryToServer extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            try {
                boolean doApiCall = false;
                HttpClient client = new DefaultHttpClient();
                String url = mContext.getResources().getString(R.string.server) + "api/user/form/docs?phone=" + user.getUserId();
                HttpPost postReq = new HttpPost(url);
                JSONObject jsonobj = new JSONObject();
                if (uploadCollegeIds.size() > 0) {
                    doApiCall = true;
                    jsonobj.put("collegeIDs", new JSONArray(uploadCollegeIds));
                }
                if (uploadAddressProofs.size() > 0) {
                    doApiCall = true;
                    jsonobj.put("addressProofs", new JSONArray(uploadAddressProofs));
                }
                if (uploadBankStmts.size() > 0) {
                    doApiCall = true;
                    jsonobj.put("bankStatements", new JSONArray(uploadBankStmts));
                }
                if (uploadBankProofs.size() > 0) {
                    doApiCall = true;
                    jsonobj.put("bankProofs", new JSONArray(uploadBankProofs));
                }
                if (!"".equals(selfieUrl)) {
                    doApiCall = true;
                    JSONArray array = new JSONArray();
                    array.put(selfieUrl);
                    jsonobj.put("selfie", array);
                }
                if (!"".equals(signatureUrl)) {
                    doApiCall = true;
                    JSONArray array = new JSONArray();
                    array.put(signatureUrl);
                    jsonobj.put("signature", array);
                }
                if (!doApiCall)
                    return "";
                StringEntity se = new StringEntity(jsonobj.toString());
                postReq.setHeader("Accept", "application/json");
                postReq.setHeader("Content-type", "application/json");
                SharedPreferences toks = mContext.getSharedPreferences("token", Context.MODE_PRIVATE);
                String tok_sp = toks.getString("token_value", "");
                postReq.setHeader("x-access-token", tok_sp);
                postReq.setEntity(se);
                HttpResponse httpresponse = client.execute(postReq);
                String responseText = EntityUtils.toString(httpresponse.getEntity());
                JSONObject json = new JSONObject(responseText);
                if (json.get("msg").toString().contains("Details updated")) {
                    JSONObject data = json.getJSONObject("data");
                    if (jsonobj.opt("collegeIDs") != null && !"".equals(jsonobj.get("collegeIDs"))) {
                        uploadCollegeIds.clear();
                        user.setNewCollegeIds(new HashMap<String, String>());
                        user.setUpdateNewCollegeIds(false);
                        JSONArray stmts = data.getJSONArray("collegeIDs");
                        ArrayList<String> yourArray = gson.fromJson(stmts.toString(), new TypeToken<List<String>>() {
                        }.getType());
                        user.setCollegeIds(yourArray);
                    }
                    if (jsonobj.opt("addressProofs") != null && !"".equals(jsonobj.get("addressProofs"))) {
                        uploadAddressProofs.clear();
                        user.setNewAddressProofs(new HashMap<String, String>());
                        user.setUpdateNewAddressProofs(false);
                        JSONArray stmts = data.getJSONArray("addressProofs");
                        ArrayList<String> yourArray = gson.fromJson(stmts.toString(), new TypeToken<List<String>>() {
                        }.getType());
                        user.setAddressProofs(yourArray);
                    }
                    if (jsonobj.opt("bankStatements") != null && !"".equals(jsonobj.get("bankStatements"))) {
                        uploadBankStmts.clear();
                        user.setNewBankStmts(new HashMap<String, String>());
                        user.setUpdateNewBankStmts(false);
                        JSONArray stmts = data.getJSONArray("bankStatements");
                        ArrayList<String> yourArray = gson.fromJson(stmts.toString(), new TypeToken<List<String>>() {
                        }.getType());
                        user.setBankStmts(yourArray);
                    }
                    if (jsonobj.opt("bankProofs") != null && !"".equals(jsonobj.get("bankProofs"))) {
                        uploadBankProofs.clear();
                        user.setNewBankProofs(new HashMap<String, String>());
                        user.setUpdateNewBankProofs(false);
                        JSONArray stmts = data.getJSONArray("bankProofs");
                        ArrayList<String> yourArray = gson.fromJson(stmts.toString(), new TypeToken<List<String>>() {
                        }.getType());
                        user.setBankProofs(yourArray);
                    }
                    if (jsonobj.opt("selfie") != null && !"".equals(jsonobj.get("selfie"))) {
                        user.setUpdateSelfie(false);
                    }
                    if (jsonobj.opt("signature") != null && !"".equals(jsonobj.get("signature"))) {
                        user.setUpdateSignature(false);
                    }
                    String jsonUser = gson.toJson(user);
                    mPrefs.edit().putString("UserObject", jsonUser).apply();
                    return "success";
                } else if (json.get("msg").toString().contains("Invalid Token")) {
                    return "authFailed";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "fail";
            }
            return "fail";
        }

        @Override
        protected void onPostExecute(String result) {
            if (retryCount < 3) {
                retryCount++;
                if (result.equals("authFailed")) {
                    new FetchNewToken(mContext).execute();
                    new UploadCoudinaryToServer().execute();
                } else if (result.equals("fail"))
                    new UploadCoudinaryToServer().execute();
                else {
                    mPrefs.edit().putBoolean("updatingDB", false).apply();
                }
            } else {
                retryCount = 0;
                mPrefs.edit().putBoolean("updatingDB", false).apply();
            }
        }
    }

    private class UploadDetailsToServer extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                boolean doApiCall = false;
                HttpClient client = new DefaultHttpClient();
                String url = mContext.getResources().getString(R.string.server) + "api/user/form?phone=" + user.getUserId();
                HttpPut putReq = new HttpPut(url);
                JSONObject jsonobj = new JSONObject();
                Map userMap = new HashMap<>();

                if (user.isUpdateName()) {
                    doApiCall = true;
                    jsonobj.put("name", user.getName());
                    userMap.put("name", user.getName());
                }
                if (user.isUpdateGender()) {
                    doApiCall = true;
                    jsonobj.put("gender", user.getGender());
                    userMap.put("gender", user.getGender());
                }
                if (user.isUpdateCollegeName()) {
                    doApiCall = true;
                    jsonobj.put("college", user.getCollegeName());
                    userMap.put("college", user.getCollegeName());
                }

                if (user.isGpaTypeUpdate()) {
                    doApiCall = true;
                    jsonobj.put("gpaType", user.getGpaType());
                    userMap.put("gpaType", user.getGpaType());
                }

                if (user.isGpaValueUpdate()) {
                    doApiCall = true;
                    jsonobj.put("gpa", user.getGpa());
                    userMap.put("gpa", user.getGpa());
                }

                if (user.isUpdateCourseName()) {
                    doApiCall = true;
                    jsonobj.put("course", user.getCourseName());
                    userMap.put("course", user.getCourseName());
                }
                if (user.isUpdateCourseEndDate()) {
                    doApiCall = true;
                    jsonobj.put("courseCompletionDate", user.getCourseEndDate());
                    userMap.put("courseCompletionDate", user.getCourseEndDate());
                }
                if (user.isUpdateAadharNumber()) {
                    doApiCall = true;
                    jsonobj.put("aadhar", user.getAadharNumber());
                    jsonobj.put("panOrAadhar", "Aadhar");
                    userMap.put("panOrAadhar", "Aadhar");
                    userMap.put("aadhar", user.getAadharNumber());
                }
                if (user.isUpdatePanNumber()) {
                    doApiCall = true;
                    jsonobj.put("pan", user.getPanNumber());
                    jsonobj.put("panOrAadhar", "PAN");
                    userMap.put("panOrAadhar", "PAN");
                    userMap.put("pan", user.getPanNumber());
                }
                if (user.isUpdateDOB()) {
                    doApiCall = true;
                    jsonobj.put("dob", user.getDob());
                    userMap.put("dob", user.getDob());
                }
                if (user.isUpdateAccommodation()) {
                    doApiCall = true;
                    jsonobj.put("accomodation", user.getAccommodation());
                    userMap.put("accomodation", user.getAccommodation());
                }
                if (user.isUpdateCurrentAddress()) {
                    doApiCall = true;
                    JSONObject json = new JSONObject();
                    json.put("line1", user.getCurrentAddress());
                    jsonobj.put("currentAddress", json);
                    jsonobj.put("currentAddressCity", user.getCurrentAddressCity());
                    userMap.put("currentAddress", user.getCurrentAddress());
                    userMap.put("currentAddressCity", user.getCurrentAddressCity());
                }

                if (user.isUpdateRollNumber()) {
                    doApiCall = true;
                    jsonobj.put("rollNumber", user.getRollNumber());
                    userMap.put("rollNumber", user.getRollNumber());

                }

                if (user.isUpdatePermanentAddress()) {
                    doApiCall = true;
                    JSONObject json = new JSONObject();
                    json.put("line1", user.getPermanentAddress());
                    jsonobj.put("permanentAddress", json);
                    userMap.put("permanentAddress", user.getPermanentAddress());
                }
                JSONArray relation = new JSONArray();
                if (user.isUpdateFamilyMemberType1() &&
                        user.isUpdateProfessionFamilyMemberType1() &&
                        user.isUpdatePreferredLanguageFamilyMemberType1() &&
                        user.isUpdatePhoneFamilyMemberType1()) {
                    doApiCall = true;
                    JSONObject json = new JSONObject();
                    json.put("relation", user.getFamilyMemberType1());
                    json.put("occupation", user.getProfessionFamilyMemberType1());
                    json.put("preferredLanguage", user.getPrefferedLanguageFamilyMemberType1());
                    json.put("phone", user.getPhoneFamilyMemberType1());
                    relation.put(json);
                    jsonobj.put("familyMember", relation);
                    userMap.put("familyMember", relation);
                }
                if (user.isUpdateFamilyMemberType2() &&
                        user.isUpdateProfessionFamilyMemberType2() &&
                        user.isUpdatePreferredLanguageFamilyMemberType2() &&
                        user.isUpdatePhoneFamilyMemberType2()) {
                    doApiCall = true;
                    JSONObject json = new JSONObject();
                    json.put("relation", user.getFamilyMemberType2());
                    json.put("occupation", user.getProfessionFamilyMemberType2());
                    json.put("preferredLanguage", user.getPrefferedLanguageFamilyMemberType2());
                    json.put("phone", user.getPhoneFamilyMemberType2());
                    relation.put(json);
                    jsonobj.put("familyMember", relation);
                }
                if (user.isUpdateBankAccNum()) {
                    doApiCall = true;
                    jsonobj.put("bankAccountNumber", user.getBankAccNum());
                    jsonobj.put("bankAccountEncrypted", false);
                }
                if (user.isUpdateBankIfsc()) {
                    doApiCall = true;
                    jsonobj.put("bankIFSC", user.getBankIfsc());
                }
                if (user.isUpdateClassmateName()) {
                    doApiCall = true;
                    jsonobj.put("friendName", user.getClassmateName());
                    userMap.put("friendName", user.getClassmateName());
                }
                if (user.isUpdateClassmatePhone()) {
                    doApiCall = true;
                    jsonobj.put("friendNumber", user.getClassmatePhone());
                    userMap.put("friendNumber", user.getClassmatePhone());
                }
                if (user.isUpdateVerificationDate()) {
                    doApiCall = true;
                    jsonobj.put("collegeIdVerificationDate", user.getVerificationDate());
                    userMap.put("collegeIdVerificationDate", user.getVerificationDate());
                }
                if (user.isUpdateAnnualFees()) {
                    doApiCall = true;
                    jsonobj.put("annualFees", user.getAnnualFees());
                    userMap.put("annualFees", user.getAnnualFees());
                }
                if (user.isUpdateScholarship()) {
                    doApiCall = true;
                    jsonobj.put("scholarship", Boolean.valueOf(user.getScholarship()));
                    userMap.put("scholarship", Boolean.valueOf(user.getScholarship()));
                }
                if (user.isUpdateScholarshipType()) {
                    doApiCall = true;
                    jsonobj.put("scholarshipProgram", user.getScholarshipType());
                }
                if (user.isUpdateScholarshipAmount()) {
                    doApiCall = true;
                    jsonobj.put("scholarshipAmount", user.getScholarshipAmount());
                }
                if (user.isUpdateStudentLoan()) {
                    doApiCall = true;
                    jsonobj.put("takenLoan", user.getStudentLoan());
                    userMap.put("takenLoan", user.getStudentLoan());
                }
                if (user.isUpdateMonthlyExpenditure()) {
                    doApiCall = true;
                    jsonobj.put("monthlyExpense", user.getMonthlyExpenditure());
                    userMap.put("monthlyExpense", user.getMonthlyExpenditure());
                }
                if (user.isUpdateVehicle()) {
                    doApiCall = true;
                    jsonobj.put("ownVehicle", Boolean.valueOf(user.getVehicle()));
                    userMap.put("ownVehicle", Boolean.valueOf(user.getVehicle()));
                }
                if (user.isUpdateVehicleType()) {
                    doApiCall = true;
                    jsonobj.put("vehicleType", user.getVehicleType());
                    userMap.put("vehicleType", user.getVehicleType());
                }
                if (!doApiCall)
                    return "";
                Intercom.client().updateUser(userMap);
                StringEntity se = new StringEntity(jsonobj.toString());
                putReq.setHeader("Accept", "application/json");
                putReq.setHeader("Content-type", "application/json");
                SharedPreferences toks = mContext.getSharedPreferences("token", Context.MODE_PRIVATE);
                String tok_sp = toks.getString("token_value", "");
                putReq.setHeader("x-access-token", tok_sp);
                putReq.setEntity(se);
                HttpResponse httpresponse = client.execute(putReq);
                String responseText = EntityUtils.toString(httpresponse.getEntity());
                JSONObject json = new JSONObject(responseText);
                if (json.get("msg").toString().contains("details updated")) {
                    // Need to get fields which are updated from DB so as to set what is updated.
                    if (jsonobj.opt("gpa") != null && !"".equals(jsonobj.get("gpa")))
                        user.setGpaValueUpdate(false);
                    if (jsonobj.opt("rollNumber") != null && !"".equals(jsonobj.get("rollNumber")))
                        user.setUpdateRollNumber(false);
                    if (jsonobj.opt("gpaType") != null && !"".equals(jsonobj.get("gpaType")))
                        user.setGpaTypeUpdate(false);
                    if (jsonobj.opt("name") != null && !"".equals(jsonobj.get("name")))
                        user.setUpdateName(false);
                    if (jsonobj.opt("gender") != null && !"".equals(jsonobj.get("gender")))
                        user.setUpdateGender(false);
                    if (jsonobj.opt("college") != null && !"".equals(jsonobj.get("college")))
                        user.setUpdateCollegeName(false);
                    if (jsonobj.opt("course") != null && !"".equals(jsonobj.get("course")))
                        user.setUpdateCourseName(false);
                    if (jsonobj.opt("courseCompletionDate") != null && !"".equals(jsonobj.get("courseCompletionDate")))
                        user.setUpdateCourseEndDate(false);
                    if (jsonobj.opt("pan") != null && !"".equals(jsonobj.get("pan")))
                        user.setUpdatePanNumber(false);
                    if (jsonobj.opt("aadhar") != null && !"".equals(jsonobj.get("aadhar")))
                        user.setUpdateAadharNumber(false);
                    if (jsonobj.opt("dob") != null && !"".equals(jsonobj.get("dob")))
                        user.setUpdateDOB(false);
                    if (jsonobj.opt("currentAddress") != null && !"".equals(jsonobj.get("currentAddress")))
                        user.setUpdateCurrentAddress(false);
                    if (jsonobj.opt("permanentAddress") != null && !"".equals(jsonobj.get("permanentAddress")))
                        user.setUpdatePermanentAddress(false);
                    if (jsonobj.opt("accomodation") != null && !"".equals(jsonobj.get("accomodation")))
                        user.setUpdateAccommodation(false);
                    if (jsonobj.opt("familyMember") != null && !"".equals(jsonobj.get("familyMember"))) {
                        user.setUpdateFamilyMemberType1(false);
                        user.setUpdateProfessionFamilyMemberType1(false);
                        user.setUpdatePreferredLanguageFamilyMemberType1(false);
                        user.setUpdatePhoneFamilyMemberType1(false);
                        user.setUpdateFamilyMemberType2(false);
                        user.setUpdateProfessionFamilyMemberType2(false);
                        user.setUpdatePreferredLanguageFamilyMemberType2(false);
                        user.setUpdatePhoneFamilyMemberType2(false);
                    }
                    if (jsonobj.opt("bankAccountNumber") != null && !"".equals(jsonobj.get("bankAccountNumber")))
                        user.setUpdateBankAccNum(false);
                    if (jsonobj.opt("bankIFSC") != null && !"".equals(jsonobj.get("bankIFSC")))
                        user.setUpdateBankIfsc(false);
                    if (jsonobj.opt("friendName") != null && !"".equals(jsonobj.get("friendName")))
                        user.setUpdateClassmateName(false);
                    if (jsonobj.opt("friendNumber") != null && !"".equals(jsonobj.get("friendNumber")))
                        user.setUpdateClassmatePhone(false);
                    if (jsonobj.opt("collegeIdVerificationDate") != null && !"".equals(jsonobj.get("collegeIdVerificationDate")))
                        user.setUpdateVerificationDate(false);

                    if (jsonobj.opt("annualFees") != null && !"".equals(jsonobj.get("annualFees")))
                        user.setUpdateAnnualFees(false);
                    if (jsonobj.opt("scholarship") != null && !"".equals(jsonobj.get("scholarship")))
                        user.setUpdateScholarship(false);
                    if (jsonobj.opt("scholarshipProgram") != null && !"".equals(jsonobj.get("scholarshipProgram")))
                        user.setUpdateScholarshipType(false);
                    if (jsonobj.opt("scholarshipAmount") != null && !"".equals(jsonobj.get("scholarshipAmount")))
                        user.setUpdateScholarshipAmount(false);
                    if (jsonobj.opt("takenLoan") != null && !"".equals(jsonobj.get("takenLoan")))
                        user.setUpdateStudentLoan(false);
                    if (jsonobj.opt("monthlyExpense") != null && !"".equals(jsonobj.get("monthlyExpense")))
                        user.setUpdateMonthlyExpenditure(false);
                    if (jsonobj.opt("ownVehicle") != null && !"".equals(jsonobj.get("ownVehicle")))
                        user.setUpdateVehicle(false);
                    if (jsonobj.opt("vehicleType") != null && !"".equals(jsonobj.get("vehicleType")))
                        user.setUpdateVehicleType(false);

                    String jsonUser = gson.toJson(user);
                    mPrefs.edit().putString("UserObject", jsonUser).apply();
                    return "success";
                } else if (json.get("msg").toString().contains("Invalid Token")) {
                    return "authFailed";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "fail";
            }
            return "fail";
        }

        @Override
        protected void onPostExecute(String result) {
            if (retryCount < 3) {
                retryCount++;
                if (result.equals("authFailed")) {
                    new FetchNewToken(mContext).execute();
                    new UploadDetailsToServer().execute();
                } else if (result.equals("fail"))
                    new UploadDetailsToServer().execute();
                else {
                    mPrefs.edit().putBoolean("updatingDB", false).apply();
                }
            } else {
                retryCount = 0;
                mPrefs.edit().putBoolean("updatingDB", false).apply();
            }
        }
    }
}