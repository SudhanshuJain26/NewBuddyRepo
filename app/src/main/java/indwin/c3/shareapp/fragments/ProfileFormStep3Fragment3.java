package indwin.c3.shareapp.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.gun0912.tedpicker.ImagePickerActivity;

import java.util.ArrayList;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.activities.ImageHelperActivity;
import indwin.c3.shareapp.activities.ProfileFormStep3;
import indwin.c3.shareapp.adapters.ImageUploaderRecyclerAdapter;
import indwin.c3.shareapp.models.Image;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.Constants;
import indwin.c3.shareapp.utils.HelpTipDialog;
import indwin.c3.shareapp.utils.RecyclerItemClickListener;

/**
 * Created by ROCK
 */
public class ProfileFormStep3Fragment3 extends Fragment {
    private UserModel user;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    ImageUploaderRecyclerAdapter adapter;
    ArrayList<Uri> imageUris;
    ImageView completeBankStmt, incompleteBankStmt;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    boolean deniedPermissionForever = false;
    private static final int REQUEST_PERMISSION_SETTING = 99;
    private ImageButton bankHelptip;
    private Image bankStmnt;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.profile_form_step3_fragment3, container, false);
        RecyclerView rvImages = (RecyclerView) rootView.findViewById(R.id.rvImages);
        ProfileFormStep3 profileFormStep3 = (ProfileFormStep3) getActivity();
        user = profileFormStep3.getUser();

        getAllViews(rootView);
        try {
            if (user.getBankStatement() == null) {
                user.setBankStatement(new Image());
            } else {
                completeBankStmt.setVisibility(View.VISIBLE);
                user.setIncompleteBankStmt(false);
            }
        } catch (Exception e) {
        }
        bankStmnt = user.getBankStatement();
        if (!bankStmnt.getImgUrls().contains("add") && !user.isAppliedFor60k())
            bankStmnt.getImgUrls().add("add");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvImages.setLayoutManager(layoutManager);

        rvImages.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (bankStmnt.getImgUrls().get(position - bankStmnt.getInvalidImgUrls().size() - bankStmnt.getValidImgUrls().size()).equals("add")) {

                            String[] temp =             AppUtils.hasPermissions(getActivity(), deniedPermissionForever, REQUEST_PERMISSION_SETTING, PERMISSIONS);

                            if (temp != null && temp.length != 0) {
                                deniedPermissionForever = true;
                                PERMISSIONS = temp;
                                requestPermissions(PERMISSIONS, ProfileFormStep1Fragment2.PERMISSION_ALL);
                            } else {
                                Intent intent = new Intent(getActivity(), ImageHelperActivity.class);
                                startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
                            }
                        }
                    }
                })
        );
        adapter = new ImageUploaderRecyclerAdapter(getActivity(), bankStmnt, "Bank Statements", user.isAppliedFor60k(), Constants.IMAGE_TYPE.BANK_STMNTS.toString());
        rvImages.setAdapter(adapter);
        if (user.isAppliedFor60k()) {
            ProfileFormStep1Fragment1.setViewAndChildrenEnabled(rootView, false);
        }
        setAllHelpTipsEnabled();
        setOnClickListener();

        return rootView;
    }

    private void setOnClickListener() {
        bankHelptip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = "We require your last 3 months financial history to be able to provide you with a higher borrowing limit. " +
                        "You can either take photos/scans of your passbook pages or download/take screenshots from your " +
                        "netbanking account";
                String text2 = "Please remember to upload both front and back sides of the card.";
                Dialog dialog = new HelpTipDialog(getActivity(), "Upload your College ID", text1, text2, "#f2954e");
                dialog.show();
            }
        });

    }

    private void getAllViews(View rootView) {
        completeBankStmt = (ImageView) rootView.findViewById(R.id.complete_bank_stmt);
        incompleteBankStmt = (ImageView) rootView.findViewById(R.id.incomplete_bank_stmt);
        bankHelptip = (ImageButton) rootView.findViewById(R.id.bank_helptip);
    }

    private void setAllHelpTipsEnabled() {

        bankHelptip.setEnabled(true);

    }

    public void checkIncomplete() {
        Image image = user.getBankStatement();
        int totalSize = image.getImgUrls().size() + image.getValidImgUrls().size() + image.getInvalidImgUrls().size();
        if (totalSize == 0) {
            user.setIncompleteBankStmt(true);
        } else if (totalSize == 1) {
            if ("add".equals(image.getImgUrls().get(0))) {
                user.setIncompleteBankStmt(true);
            } else {
                user.setIncompleteBankStmt(false);
            }
        } else {
            user.setIncompleteBankStmt(false);
        }

        if (user.isIncompleteBankStmt()) {
            incompleteBankStmt.setVisibility(View.VISIBLE);
            completeBankStmt.setVisibility(View.GONE);
        } else {
            completeBankStmt.setVisibility(View.VISIBLE);
            incompleteBankStmt.setVisibility(View.GONE);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);

        if (requestCode == INTENT_REQUEST_GET_IMAGES && resuleCode == Activity.RESULT_OK) {
            UserModel user = AppUtils.getUserObject(getActivity());
            if (user.getBankStmts() == null)
                user.setBankStmts(new ArrayList<String>());
            imageUris = intent.getParcelableArrayListExtra(ImageHelperActivity.EXTRA_IMAGE_URIS);
            if (user.getBankStatement() == null)
                user.setBankStatement(new Image());
            Image image = user.getBankStatement();
            for (Uri uri : imageUris) {
                image.getImgUrls().add(0, uri.getPath());
                image.getNewImgUrls().put(uri.getPath(), AppUtils.uploadStatus.OPEN.toString());
                this.user.getBankStatement().getImgUrls().add(0, uri.getPath());

            }
            adapter.notifyDataSetChanged();
            image.setUpdateNewImgUrls(true);
            AppUtils.saveUserObject(getActivity(), user);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == ProfileFormStep1Fragment2.PERMISSION_ALL) {
            if (grantResults.length > 0)
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
            Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
            startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}