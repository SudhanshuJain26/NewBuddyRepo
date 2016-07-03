package indwin.c3.shareapp.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import indwin.c3.shareapp.BuildConfig;
import indwin.c3.shareapp.activities.ProfileFormStep1;
import indwin.c3.shareapp.activities.ProfileFormStep2;
import indwin.c3.shareapp.activities.ProfileFormStep3;
import indwin.c3.shareapp.models.FrontBackImage;
import indwin.c3.shareapp.models.Image;
import indwin.c3.shareapp.models.ResponseModel;
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
    ArrayList<String> uploadCollegeIds, uploadAddressProofs, uploadBankStmts, uploadBankProofs, uploadGradeSheets;
    int retryCount = 0;
    String selfieUrl = "", signatureUrl = "";
    String frontCollegeId, backCollegeId, frontAadharId, backAadharId, panImage;
    private String isLastStep;
    private Activity activity;

    @Override
    public synchronized void onReceive(final Context context, final Intent arg1) {
        callServer(context, null);
    }

    private void callServer(Context context, final String isLastStep) {
        mContext = context;
        mPrefs = mContext.getSharedPreferences("buddy", Context.MODE_PRIVATE);
        boolean isUpdatingDB = mPrefs.getBoolean("updatingDB", false);
        if (AppUtils.getUserObject(mContext) == null) {
            mPrefs.edit().putBoolean("updatingDB", false).apply();
        } else {
            if (!isUpdatingDB) {

                new AsyncTaskRunner().execute(this.isLastStep);
            } else {
                Runnable myRunnable = new Runnable() {

                    public void run() {
                        checkForDBUpdate(isLastStep);
                    }


                };
                Thread thread = new Thread(myRunnable);
                thread.start();
            }
        }
    }

    public CheckInternetAndUploadUserDetails(Activity activity, String islastStep) {
        this.isLastStep = islastStep;
        this.activity = activity;
        callServer(activity, islastStep);

    }

    public CheckInternetAndUploadUserDetails() {
    }

    private void checkForDBUpdate(String isLastStep) {
        try {
            Thread.sleep(1000);

        } catch (Exception e) {

        }
        SharedPreferences sh = mContext.getSharedPreferences("buddy", Context.MODE_PRIVATE);
        boolean isUpdatingDB = sh.getBoolean("updatingDB", false);
        if (isUpdatingDB)
            checkForDBUpdate(isLastStep);
        else
            new AsyncTaskRunner().execute(isLastStep);
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        private String resp;

        @Override
        protected String doInBackground(String... params) {
            try {
                isConnected = AppUtils.isOnline(mContext);
            } catch (Exception e) {
                isConnected = false;
                resp = e.getMessage();
            }
            return params[0];
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
                uploadGradeSheets = new ArrayList<>();
                if (user != null)
                    new CloudinaryUploader().execute(result);
            }
        }
    }

    private class CloudinaryUploader extends AsyncTask<String, String, String> {
        boolean updateUser = false;

        @Override
        protected synchronized String doInBackground(String... params) {
            UserModel userImages = AppUtils.getUserObject(mContext);

            if (userImages.getPanProof() != null) {


                try {
                    Long tsLong = System.currentTimeMillis() / 1000;
                    String ts = tsLong.toString();
                    FrontBackImage panProof = userImages.getPanProof();
                    if (AppUtils.isNotEmpty(panProof.getImgUrl()) && (AppUtils.uploadStatus.OPEN.toString().equals(userImages.getPanStatus()) || AppUtils.isEmpty(userImages.getPanStatus()))) {
                        userImages.setPanStatus(AppUtils.uploadStatus.PICKED.toString());
                        AppUtils.saveUserObject(mContext, userImages);
                        updateUser = true;
                        cloudinary.uploader().upload(userImages.getPanProof().getImgUrl(),
                                ObjectUtils.asMap("public_id", userImages.getUserId() + "pan" + ts));
                        panImage = cloudinary.url().secure(true).generate(userImages.getUserId() + "pan" + ts);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (userImages.getCollegeID() != null) {
                try {
                    Long tsLong = System.currentTimeMillis() / 1000;
                    String ts = tsLong.toString();
                    Image collegeId = userImages.getCollegeID();
                    if ((collegeId.getFront() != null && collegeId.isUpdateFront()) && (AppUtils.uploadStatus.OPEN.toString().equals(collegeId.getFrontStatus()) || AppUtils.isEmpty(collegeId.getFrontStatus()))) {
                        collegeId.setFrontStatus(AppUtils.uploadStatus.PICKED.toString());
                        AppUtils.saveUserObject(mContext, userImages);
                        updateUser = true;
                        cloudinary.uploader().upload(userImages.getCollegeID().getFront().getImgUrl(),
                                ObjectUtils.asMap("public_id", userImages.getUserId() + "collegeId_front" + ts));
                        frontCollegeId = cloudinary.url().secure(true).generate(userImages.getUserId() + "collegeId_front" + ts);
                    }

                    if ((collegeId.getBack() != null && collegeId.isUpdateBack()) && (AppUtils.uploadStatus.OPEN.toString().equals(collegeId.getBackStatus()) || AppUtils.isEmpty(collegeId.getBackStatus()))) {
                        collegeId.setBackStatus(AppUtils.uploadStatus.PICKED.toString());
                        AppUtils.saveUserObject(mContext, userImages);
                        updateUser = true;
                        cloudinary.uploader().upload(userImages.getCollegeID().getBack().getImgUrl(),
                                ObjectUtils.asMap("public_id", userImages.getUserId() + "collegeId_back" + ts));
                        backCollegeId = cloudinary.url().secure(true).generate(userImages.getUserId() + "collegeId_back" + ts);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            userImages = AppUtils.getUserObject(mContext);
            if (userImages.getAddressProof() != null) {
                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();
                Image addressProof = userImages.getAddressProof();
                if ((addressProof.getFront() != null && addressProof.isUpdateFront()) && (AppUtils.uploadStatus.OPEN.toString().equals(addressProof.getFrontStatus()) || AppUtils.isEmpty(addressProof.getFrontStatus()))) {
                    updateUser = true;
                    try {

                        addressProof.setFrontStatus(AppUtils.uploadStatus.PICKED.toString());
                        cloudinary.uploader().upload(userImages.getAddressProof().getFront().getImgUrl(),
                                ObjectUtils.asMap("public_id", userImages.getUserId() + "aadhar_front" + ts));
                        frontAadharId = cloudinary.url().secure(true).generate(userImages.getUserId() + "aadhar_front" + ts);


                    } catch (Exception e3) {
                    }
                }
                if ((addressProof.getBack() != null && addressProof.isUpdateBack()) && (AppUtils.uploadStatus.OPEN.toString().equals(addressProof.getBackStatus()) || AppUtils.isEmpty(addressProof.getBackStatus()))) {
                    addressProof.setBackStatus(AppUtils.uploadStatus.PICKED.toString());
                    try {
                        cloudinary.uploader().upload(userImages.getAddressProof().getBack().getImgUrl(),
                                ObjectUtils.asMap("public_id", userImages.getUserId() + "aadhar_back" + ts));
                        backAadharId = cloudinary.url().secure(true).generate(userImages.getUserId() + "aadhar_back" + ts);
                    } catch (Exception e) {

                    }
                    updateUser = true;
                }
                AppUtils.saveUserObject(mContext, userImages);
            }
            userImages = AppUtils.getUserObject(mContext);
            if (userImages.getBankStatement() != null && userImages.getBankStatement().getNewImgUrls().size() > 0) {
                int i = 0;
                for (Map.Entry<String, String> entry : userImages.getBankStatement().getNewImgUrls().entrySet()) {
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
            if (userImages.getBankProof() != null && userImages.getBankProof().getNewImgUrls().size() > 0) {
                int i = 0;
                for (Map.Entry<String, String> entry : userImages.getBankProof().getNewImgUrls().entrySet()) {
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

            userImages = AppUtils.getUserObject(mContext);
            if (userImages.getGradeSheet() != null && userImages.getGradeSheet().getNewImgUrls().size() > 0) {
                int i = 0;
                for (Map.Entry<String, String> entry : userImages.getGradeSheet().getNewImgUrls().entrySet()) {
                    if (AppUtils.uploadStatus.OPEN.toString().equals(entry.getValue())) {
                        i++;
                        entry.setValue(AppUtils.uploadStatus.PICKED.toString());
                        AppUtils.saveUserObject(mContext, userImages);

                        try {
                            Long tsLong = System.currentTimeMillis() / 1000;
                            String ts = tsLong.toString();
                            cloudinary.uploader().upload(entry.getKey(),
                                    ObjectUtils.asMap("public_id", userImages.getUserId() + "gradeSheet" + ts + i));
                            String cloudinaryUrl = cloudinary.url().secure(true).generate(userImages.getUserId() + "gradeSheet" + ts + i);
                            uploadGradeSheets.add(cloudinaryUrl);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        updateUser = true;
                    }
                }
            }
            if (userImages.isUpdateSelfie() && (userImages.getSelfieStatus() == null || AppUtils.uploadStatus.OPEN.toString().equals(userImages.getSelfieStatus()))) {
                try {
                    userImages.setSelfieStatus(AppUtils.uploadStatus.PICKED.toString());
                    AppUtils.saveUserObject(mContext, userImages);
                    Long tsLong = System.currentTimeMillis() / 1000;
                    String ts = tsLong.toString();
                    cloudinary.uploader().upload(userImages.getSelfie(),
                            ObjectUtils.asMap("public_id", userImages.getUserId() + "selfie" + ts));
                    selfieUrl = cloudinary.url().secure(true).generate(userImages.getUserId() + "selfie" + ts);


                } catch (Exception e) {
                    e.printStackTrace();
                }
                updateUser = true;
            }
            if (userImages.isUpdateSignature() && (userImages.getSignatureStatus() == null || AppUtils.uploadStatus.OPEN.toString().equals(userImages.getSignatureStatus()))) {
                try {
                    userImages.setSignatureStatus(AppUtils.uploadStatus.PICKED.toString());
                    AppUtils.saveUserObject(mContext, userImages);
                    Long tsLong = System.currentTimeMillis() / 1000;
                    String ts = tsLong.toString();
                    cloudinary.uploader().upload(userImages.getSignature(),
                            ObjectUtils.asMap("public_id", userImages.getUserId() + "signature" + ts));
                    signatureUrl = cloudinary.url().secure(true).generate(userImages.getUserId() + "signature" + ts);
                    user.setSignature(signatureUrl);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                updateUser = true;
            }
            if (params[0] != null) {
                updateUser = true;
            }
            return params[0];
        }

        @Override
        protected void onPostExecute(String result) {
            if (updateUser) {
                new UploadCoudinaryToServer().execute(result);
            }
            new UploadDetailsToServer().execute(result);
        }
    }


    private class UploadCoudinaryToServer extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            try {
                boolean doApiCall = false;
                HttpClient client = new DefaultHttpClient();
                String url = BuildConfig.SERVER_URL + "api/v1/user/profile/docs/upload?phone=" + user.getUserId();
                HttpPost postReq = new HttpPost(url);
                JSONObject jsonobj = new JSONObject();
                jsonobj.put("userid", user.getUserId());

                Image collegeID = user.getCollegeID();
                Image addressproof = user.getAddressProof();
                if (Constants.YES_1k.equals(params[0])) {
                    if (collegeID != null && collegeID.getFront() != null && AppUtils.isNotEmpty(collegeID.getFront().getImgUrl()) && AppUtils.isCloudinaryUrl(collegeID.getFront().getImgUrl())) {
                        frontCollegeId = collegeID.getFront().getImgUrl();
                    }
                    if (collegeID != null && collegeID.getBack() != null && AppUtils.isNotEmpty(collegeID.getBack().getImgUrl()) && AppUtils.isCloudinaryUrl(collegeID.getBack().getImgUrl())) {
                        backCollegeId = collegeID.getBack().getImgUrl();
                    }

                    if (AppUtils.isNotEmpty(user.getSelfie())) {
                        selfieUrl = user.getSelfie();
                    }
                    if (AppUtils.isNotEmpty(user.getSignature())) {
                        selfieUrl = user.getSignature();
                    }
                }
                if (Constants.YES_1k.equals(params[0]) && addressproof != null) {
                    if (addressproof.getFront() != null && AppUtils.isNotEmpty(addressproof.getFront().getImgUrl()) && AppUtils.isCloudinaryUrl(addressproof.getFront().getImgUrl())) {
                        frontAadharId = addressproof.getFront().getImgUrl();
                    }
                    if (addressproof.getBack() != null && AppUtils.isNotEmpty(addressproof.getBack().getImgUrl()) && AppUtils.isCloudinaryUrl(addressproof.getBack().getImgUrl())) {
                        backAadharId = addressproof.getBack().getImgUrl();
                    }
                }
                if (frontCollegeId != null || backCollegeId != null) {
                    JSONObject collegeIDJson = new JSONObject();
                    doApiCall = true;
                    if (frontCollegeId != null) {
                        JSONObject front = new JSONObject();
                        front.put("imgUrl", frontCollegeId);
                        collegeIDJson.put("front", front);
                    }
                    if (backCollegeId != null) {
                        JSONObject back = new JSONObject();
                        back.put("imgUrl", backCollegeId);
                        collegeIDJson.put("back", back);
                    }
                    jsonobj.put("collegeID", collegeIDJson);
                }
                if (frontAadharId != null || backAadharId != null) {
                    JSONObject addressProof = new JSONObject();
                    doApiCall = true;
                    if (user.getAddressProof() != null)
                        addressProof.put("type", user.getAddressProof().getType());
                    if (frontAadharId != null) {
                        JSONObject front = new JSONObject();
                        front.put("imgUrl", frontAadharId);
                        addressProof.put("front", front);
                    }
                    if (backAadharId != null) {
                        JSONObject back = new JSONObject();
                        back.put("imgUrl", backAadharId);
                        addressProof.put("back", back);
                    }
                    jsonobj.put("addressProof", addressProof);
                }
                if (Constants.YES_7k.equals(params[0]) || Constants.YES_60k.equals(params[0])) {
                    if (user.getBankStatement() != null && user.getBankStatement().getImgUrls() != null) {
                        for (String bankStatement : user.getBankStatement().getImgUrls()) {
                            if (AppUtils.isCloudinaryUrl(bankStatement)) {
                                uploadBankStmts.add(bankStatement);
                            }
                        }
                    }
                }
                if (Constants.YES_7k.equals(params[0])) {

                    if (user.getBankProof() != null && user.getBankProof().getImgUrls() != null) {
                        for (String bankProof : user.getBankProof().getImgUrls()) {
                            if (AppUtils.isCloudinaryUrl(bankProof)) {
                                uploadBankProofs.add(bankProof);
                            }
                        }
                    }
                    if (user.getBankStatement() != null && user.getBankStatement().getImgUrls() != null) {
                        for (String bankStatement : user.getBankStatement().getImgUrls()) {
                            if (AppUtils.isCloudinaryUrl(bankStatement)) {
                                uploadBankStmts.add(bankStatement);
                            }
                        }
                    }
                    if (user.getGradeSheet() != null && user.getGradeSheet().getImgUrls() != null) {
                        for (String gradesheet : user.getGradeSheet().getImgUrls()) {
                            if (AppUtils.isCloudinaryUrl(gradesheet)) {
                                uploadGradeSheets.add(gradesheet);
                            }
                        }
                    }
                }
                if (uploadBankStmts.size() > 0) {
                    JSONObject bankStmnt = new JSONObject();
                    doApiCall = true;
                    bankStmnt.put("imgUrls", new JSONArray(uploadBankStmts));
                    jsonobj.put("bankStatement", bankStmnt);
                }
                if (uploadBankProofs.size() > 0) {
                    JSONObject bankStmnt = new JSONObject();
                    doApiCall = true;
                    bankStmnt.put("imgUrls", new JSONArray(uploadBankProofs));
                    jsonobj.put("bankProof", bankStmnt);
                }

                if (uploadGradeSheets.size() > 0) {
                    JSONObject bankStmnt = new JSONObject();
                    doApiCall = true;
                    bankStmnt.put("imgUrls", new JSONArray(uploadGradeSheets));
                    jsonobj.put("gradeSheet", bankStmnt);
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

                if (panImage != null) {
                    doApiCall = true;
                    JSONObject panProof = new JSONObject();
                    panProof.put("imgUrl", panImage);
                    jsonobj.put("panProof", panProof);
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
                Gson gson = new Gson();
                ResponseModel responseModel = (ResponseModel) gson.fromJson(responseText, ResponseModel.class);
                try {
                    isLastStep = params[0];
                } catch (Exception e) {
                }

                if (json.get("status").equals("success")) {


                    return "success";
                } else if (json.get("status").equals("error")) {

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
                    new UploadCoudinaryToServer().execute(isLastStep);
                } else if (result.equals("fail"))
                    new UploadCoudinaryToServer().execute(isLastStep);
                else {
                    mPrefs.edit().putBoolean("updatingDB", false).apply();
                }
            } else {
                retryCount = 0;
                mPrefs.edit().putBoolean("updatingDB", false).apply();
            }
        }
    }


    private void prepareStep1Data(UserModel user, Map userMap, JSONObject jsonobj, boolean pushAllData) {
        try {
            if (user.isUpdateGender() || pushAllData) {
                jsonobj.put("gender", user.getGender());
                userMap.put("gender", user.getGender());
            }
            if (user.isUpdateDOB() || pushAllData) {
                jsonobj.put("dob", user.getDob());
                userMap.put("dob", user.getDob());
            }

            if (user.isUpdateCollegeName() || pushAllData) {
                jsonobj.put("college", user.getCollegeName());
                userMap.put("college", user.getCollegeName());
            }

            if (user.isUpdateCourseName() || pushAllData) {
                jsonobj.put("course", user.getCourseName());
                userMap.put("course", user.getCourseName());
            }
            if (user.isUpdateCourseEndDate() || pushAllData) {
                jsonobj.put("courseCompletionDate", user.getCourseEndDate());
                userMap.put("courseCompletionDate", user.getCourseEndDate());
            }
            if ((user.isUpdateAadharNumber() || pushAllData) && AppUtils.isNotEmpty(user.getAadharNumber())) {
                jsonobj.put("aadhar", user.getAadharNumber());
                jsonobj.put("panOrAadhar", "Aadhar");
                userMap.put("panOrAadhar", "Aadhar");
                userMap.put("aadhar", user.getAadharNumber());
            }
            if ((user.isUpdatePanNumber() || pushAllData) && AppUtils.isNotEmpty(user.getPanNumber())) {
                jsonobj.put("pan", user.getPanNumber());
                jsonobj.put("panOrAadhar", "PAN");
                userMap.put("panOrAadhar", "PAN");
                userMap.put("pan", user.getPanNumber());
            }


            if (user.isUpdateAccommodation() || pushAllData) {
                jsonobj.put("accomodation", user.getAccommodation());
                userMap.put("accomodation", user.getAccommodation());
            }
            if (user.isUpdateCurrentAddress() || pushAllData) {
                JSONObject json = new JSONObject();
                json.put("line1", user.getCurrentAddress());
                json.put("line2", user.getCurrentAddressLine2());
                json.put("city", user.getCurrentAddressCity());
                json.put("pincode", user.getCurrentAddressPinCode());
                jsonobj.put("currentAddress", json);
                jsonobj.put("currentAddressCity", user.getCurrentAddressCity());
                userMap.put("currentAddress", user.getCurrentAddress());
                userMap.put("currentAddressCity", user.getCurrentAddressCity());
            }

            if (user.isUpdatePermanentAddress() || pushAllData) {
                JSONObject json = new JSONObject();
                json.put("line1", user.getPermanentAddress());
                json.put("line2", user.getPermanentAddressLine2());
                json.put("city", user.getPermanentAddressCity());
                json.put("pincode", user.getPermanentAddressPinCode());
                jsonobj.put("permanentAddress", json);
                userMap.put("permanentAddress", user.getPermanentAddress());
            }
            if (user.isTncUpdate()) {
                jsonobj.put("tncAccepted", user.isTncAccepted());
            }

        } catch (Exception e) {
        }
    }

    private void prepareStep2Data(UserModel user, Map userMap, JSONObject jsonobj, boolean pushAllData) {
        try {
            if (user.isGpaTypeUpdate() || pushAllData) {
                jsonobj.put("gpaType", user.getGpaType());
                userMap.put("gpaType", user.getGpaType());
            }

            if (user.isGpaValueUpdate() || pushAllData) {
                jsonobj.put("gpa", user.getGpa());
                userMap.put("gpa", user.getGpa());
            }
            if (user.isUpdateStudentLoan() || pushAllData) {
                jsonobj.put("takenLoan", user.getStudentLoan());
                userMap.put("takenLoan", user.getStudentLoan());
            }

            JSONArray relation = new JSONArray();
            if ((user.isUpdateFamilyMemberType1() &&
                    user.isUpdateProfessionFamilyMemberType1() &&
                    user.isUpdatePreferredLanguageFamilyMemberType1() &&
                    user.isUpdatePhoneFamilyMemberType1()) || pushAllData) {
                JSONObject json = new JSONObject();
                json.put("relation", user.getFamilyMemberType1());
                json.put("occupation", user.getProfessionFamilyMemberType1());
                json.put("preferredLanguage", user.getPrefferedLanguageFamilyMemberType1());
                if (AppUtils.isNotEmpty(user.getPhoneFamilyMemberType1()))
                    json.put("phone", user.getPhoneFamilyMemberType1());
                relation.put(json);
                jsonobj.put("familyMember", relation);
                userMap.put("familyMember", relation);
            }
            if ((user.isUpdateFamilyMemberType2() &&
                    user.isUpdateProfessionFamilyMemberType2() &&
                    user.isUpdatePreferredLanguageFamilyMemberType2() &&
                    user.isUpdatePhoneFamilyMemberType2()) || pushAllData) {
                if (AppUtils.isNotEmpty(user.getFamilyMemberType2()) || AppUtils.isNotEmpty(user.getProfessionFamilyMemberType2()) || AppUtils.isNotEmpty(user.getPrefferedLanguageFamilyMemberType2()) || AppUtils.isNotEmpty(user.getPhoneFamilyMemberType2())) {
                    JSONObject json = new JSONObject();
                    json.put("relation", user.getFamilyMemberType2());
                    json.put("occupation", user.getProfessionFamilyMemberType2());
                    json.put("preferredLanguage", user.getPrefferedLanguageFamilyMemberType2());
                    json.put("phone", user.getPhoneFamilyMemberType2());
                    relation.put(json);
                    jsonobj.put("familyMember", relation);
                    userMap.put("familyMember", relation);
                }

            }
            if (user.isUpdateRollNumber() || pushAllData) {
                jsonobj.put("rollNumber", user.getRollNumber());
                userMap.put("rollNumber", user.getRollNumber());
            }
            if (user.isUpdateClassmateName() || pushAllData) {
                jsonobj.put("friendName", user.getClassmateName());
                userMap.put("friendName", user.getClassmateName());
            }
            if (user.isUpdateClassmatePhone() || pushAllData) {
                jsonobj.put("friendNumber", user.getClassmatePhone());
                userMap.put("friendNumber", user.getClassmatePhone());
            }
            if (user.isUpdateVerificationDate() || pushAllData) {
                jsonobj.put("collegeIdVerificationDate", user.getVerificationDate());
                userMap.put("collegeIdVerificationDate", user.getVerificationDate());
            }
            if (user.isOptionalNACH() != null) {
                jsonobj.put("optionalNACH", user.isOptionalNACH());
                userMap.put("optionalNACH", user.isOptionalNACH());

            }
            if ((user.isUpdateBankAccNum() || pushAllData) && AppUtils.isNotEmpty(user.getBankAccNum())) {
                jsonobj.put("bankAccountNumber", user.getBankAccNum());
                jsonobj.put("bankAccountEncrypted", false);
                userMap.put("bankAccountNumber", user.getBankAccNum());
            }
            if (user.isUpdateBankIfsc() || pushAllData) {
                jsonobj.put("bankIFSC", user.getBankIfsc());
                userMap.put("bankIFSC", user.getBankIfsc());
            }

        } catch (Exception e) {
        }
    }


    private void prepareStep3Data(UserModel user, Map userMap, JSONObject jsonobj, Boolean pushAllData) {
        try {
            if (user.isUpdateAnnualFees() || pushAllData) {
                jsonobj.put("annualFees", user.getAnnualFees());
                userMap.put("annualFees", user.getAnnualFees());
            }
            if (user.isUpdateScholarship() || pushAllData) {
                jsonobj.put("scholarship", Boolean.valueOf(user.getScholarship()));
                userMap.put("scholarship", Boolean.valueOf(user.getScholarship()));
            }
            if (user.isUpdateScholarshipType() || pushAllData) {
                jsonobj.put("scholarshipProgram", user.getScholarshipType());
                userMap.put("scholarshipProgram", user.getScholarshipType());

            }


            if (user.isUpdateScholarshipAmount() || pushAllData) {
                jsonobj.put("scholarshipAmount", user.getScholarshipAmount());
                userMap.put("scholarshipAmount", user.getScholarshipAmount());
            }

            if (user.isUpdateMonthlyExpenditure() || pushAllData) {
                jsonobj.put("monthlyExpense", user.getMonthlyExpenditure());
                userMap.put("monthlyExpense", user.getMonthlyExpenditure());
            }
            if (user.isUpdateVehicle() || pushAllData) {
                jsonobj.put("ownVehicle", Boolean.valueOf(user.getVehicle()));
                userMap.put("ownVehicle", Boolean.valueOf(user.getVehicle()));
            }
            if (user.isUpdateVehicleType() || pushAllData) {
                jsonobj.put("vehicleType", user.getVehicleType());
                userMap.put("vehicleType", user.getVehicleType());
            }
        } catch (Exception e) {
        }
    }

    private class UploadDetailsToServer extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            ResponseModel responseModel = null;
            Map userMap = new HashMap<>();
            try {
                Boolean doApiCall = false;
                HttpClient client = new DefaultHttpClient();
                String url = BuildConfig.SERVER_URL + "api/v1.01/user/profile";
                HttpPost putReq = new HttpPost(url);
                JSONObject jsonobj = new JSONObject();

                jsonobj.put("userid", user.getUserId());
                if (user.isUpdateName()) {
                    doApiCall = true;
                    jsonobj.put("name", user.getName());
                    userMap.put("name", user.getName());
                }
                isLastStep = params[0];
                prepareStep1Data(user, userMap, jsonobj, Constants.YES_1k.equals(isLastStep));
                prepareStep2Data(user, userMap, jsonobj, Constants.YES_7k.equals(isLastStep));
                prepareStep3Data(user, userMap, jsonobj, Constants.YES_60k.equals(isLastStep));
                if (userMap.size() == 0)
                    return "";
                try {
                    Intercom.client().updateUser(userMap);
                } catch (Exception e) {
                    System.out.println("Intercom four" + e.toString());
                }
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
                UserModel user = AppUtils.getUserObject(mContext);
                responseModel = (ResponseModel) gson.fromJson(responseText, ResponseModel.class);


                boolean isSuccess = json.get("status").equals("success");
                boolean isAuthFailed = false;
                try {
                    isAuthFailed = json.get("msg").toString().contains("Invalid Token");
                } catch (Exception e) {
                }

                boolean isFailed = false;
                try {
                    isFailed = json.get("status").equals("error") && (json.opt("msg") == null || !json.get("msg").toString().contains("Invalid Token"));
                } catch (Exception e) {
                }
                if (isSuccess || isFailed) {
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
                    if (jsonobj.opt("tncAccepted") != null)
                        user.setTncUpdate(false);
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
                    AppUtils.saveUserObject(mContext, user);
                    return "success";
                } else if (isAuthFailed) {
                    return "authFailed";
                }
            } catch (Exception e) {

                e.printStackTrace();
                return "fail";
            } finally {
                if (userMap.size() > 0) {
                    try {
                        ProfileFormStep1 pfs1 = (ProfileFormStep1) activity;
                        pfs1.notifyUploadData(responseModel, params[0], activity);
                    } catch (Exception e) {
                        try {
                            ProfileFormStep2 pfs2 = (ProfileFormStep2) activity;
                            pfs2.notifyUploadData(responseModel, params[0], activity);
                        } catch (Exception e1) {
                            try {
                                ProfileFormStep3 pfs3 = (ProfileFormStep3) activity;
                                pfs3.notifyUploadData(responseModel, params[0], activity);
                            } catch (Exception e2) {

                            }

                        }
                    }
                }
            }
            return "fail";
        }


        @Override
        protected void onPostExecute(String result) {
            if (retryCount < 3) {
                retryCount++;
                if (result.equals("authFailed")) {
                    new FetchNewToken(mContext).execute();
                    new UploadDetailsToServer().execute(isLastStep);
                }
                //new UploadDetailsToServer().execute(isLastStep);
                else if ("success".equals(result) || "fail".equals(result)) {
                    mPrefs.edit().putBoolean("updatingDB", false).apply();
                }
            } else {
                retryCount = 0;
                mPrefs.edit().putBoolean("updatingDB", false).apply();
            }
        }
    }

    public interface NotifyProgress {
        public void notifyUploadData(ResponseModel responseModel, String isLastStep, Activity activity);
    }
}