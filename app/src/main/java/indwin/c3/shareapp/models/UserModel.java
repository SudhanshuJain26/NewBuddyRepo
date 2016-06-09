package indwin.c3.shareapp.models;

import java.util.ArrayList;
import java.util.Map;

import indwin.c3.shareapp.utils.AppUtils;

/**
 * Created by shubhang on 18/03/16.
 */
public class UserModel {
    String gender;
    String userId;
    String name;
    String email;
    private String rollNumber;
    private boolean updateRollNumber;
    private boolean incompleteRollNumber;
    boolean isInCompleteAgreement;
    boolean emailVerified = false;
    boolean updateEmail;
    boolean isFbConnected;
    boolean updateFbConnected;
    String fbUserId;
    ArrayList<String> collegeIds;
    Map<String, String> newCollegeIds;
    boolean updateNewCollegeIds;
    String collegeName;
    boolean updateCollegeName;
    String courseName;
    boolean updateCourseName;
    String courseEndDate;
    boolean updateCourseEndDate;
    ArrayList<String> addressProofs;
    Map<String, String> newAddressProofs;
    boolean updateNewAddressProofs;
    String aadharNumber;
    boolean updateAadharNumber;
    String panNumber;
    boolean updatePanNumber;
    String panOrAadhar;
    String signature;
    private String signatureStatus;
    String selfie;
    private String selfieStatus;
    String dob;
    String accommodation;
    String currentAddress;
    private String currentAddressLine2;
    private String currentAddressPinCode;
    private String currentAddressCity;
    String permanentAddress;
    private String permanentAddressLine2;
    private String permanentAddressPinCode;
    private String permanentAddressCity;
    String familyMemberType1;
    String professionFamilyMemberType1;
    String prefferedLanguageFamilyMemberType1;
    String designationFamilyMemberType1;
    String phoneFamilyMemberType1;
    String familyMemberType2;
    String professionFamilyMemberType2;
    String prefferedLanguageFamilyMemberType2;
    String designationFamilyMemberType2;
    String phoneFamilyMemberType2;
    String bankAccNum;
    String bankIfsc;
    String classmateName;
    String classmatePhone;
    String verificationDate;
    String annualFees;
    String scholarship;
    String scholarshipType;
    String studentLoan;
    String monthlyExpenditure;
    String vehicle;
    String vehicleType;
    ArrayList<String> bankStmts;
    Map<String, String> newBankStmts;
    ArrayList<String> bankProofs;
    Map<String, String> newBankProofs;

    String iv;
    int creditLimit = 0;
    int availableCredit = 0;
    int cashBack = 0;
    String formStatus = "signedUp";
    String approvedBand;
    String rejectionReason;
    String status1K;
    String status7K;
    String status60K;
    String profileStatus;
    String scholarshipAmount;
    String gpaType;
    String gpa;

    private ArrayList<String> marksheets;
    private Map<String, String> newMarksheets;
    private boolean updateNewMarksheets;
    private boolean incompleteMarksheets;
    private boolean updatePreferredLanguageFamilyMemberType1;
    private boolean updatePreferredLanguageFamilyMemberType2;
    boolean emailSent;
    boolean updateCurrentAddressCity;
    boolean updateGender;
    boolean updateName;
    boolean updateNewBankStmts;
    boolean updateNewBankProofs;
    boolean updateMonthlyExpenditure;
    boolean updateVehicle;
    boolean updateVehicleType;
    boolean updateScholarship;
    boolean updateScholarshipType;
    boolean updateScholarshipAmount;
    boolean updateStudentLoan;
    boolean updateAnnualFees;
    boolean updateClassmateName;
    boolean updateClassmatePhone;
    boolean updateVerificationDate;
    boolean updateBankAccNum;
    private boolean incompleteGpa;
    boolean gpaTypeUpdate;
    boolean gpaValueUpdate;
    boolean updateBankIfsc;
    boolean updateFamilyMemberType1;
    boolean updateProfessionFamilyMemberType1;
    boolean updateDesignationFamilyMemberType1;
    boolean updatePhoneFamilyMemberType1;
    boolean updateFamilyMemberType2;
    boolean updateProfessionFamilyMemberType2;
    boolean updateDesignationFamilyMemberType2;
    boolean updatePhoneFamilyMemberType2;
    boolean updatePermanentAddress;
    boolean updateCurrentAddress;
    boolean updateAccommodation;
    boolean updateDOB;
    boolean updateSelfie;
    boolean updateSignature;
    boolean incompleteCollegeId = false;
    boolean incompleteCollegeDetails = false;
    boolean incompleteEmail = false;
    boolean incompleteFb = false;
    boolean incompleteAadhar = false;
    boolean incompletePermanentAddress = false;
    boolean incompleteAddressDetails = false;
    boolean incompleteDOB = false;
    boolean incompleteFamilyDetails = false;
    boolean incompleteRepaymentSetup = false;
    boolean incompleteClassmateDetails = false;
    boolean incompleteVerificationDate = false;
    boolean incompleteAnnualFees = false;
    boolean incompleteScholarship = false;
    boolean incompleteStudentLoan = false;
    boolean incompleteMonthlyExpenditure = false;
    boolean incompleteVehicleDetails = false;
    boolean incompleteBankStmt = false;
    boolean incompleteGender = false;
    boolean appliedFor1k = false;
    boolean appliedFor7k = false;
    boolean appliedFor60k = false;

    private Image gradeSheet;
    private FrontBackImage panProof;
    private Image collegeID;
    private boolean updatePanProof;
    private Image bankProof;

    private Image addressProof;
    private Image bankStatement;

    public boolean isEmailSent() {
        return emailSent;
    }

    public void setEmailSent(boolean emailSent) {
        this.emailSent = emailSent;
    }

    public boolean isUpdateScholarshipAmount() {
        return updateScholarshipAmount;
    }

    public void setUpdateScholarshipAmount(boolean updateScholarshipAmount) {
        this.updateScholarshipAmount = updateScholarshipAmount;
    }

    public boolean getIsUpdateRollNumber() {
        return updateRollNumber;
    }

    public void setIsUpdateRollNumber(boolean isUpdateRollNumber) {
        this.updateRollNumber = isUpdateRollNumber;
    }

    public boolean isUpdateRollNumber() {
        return updateRollNumber;
    }

    public void setUpdateRollNumber(boolean updateRollNumber) {
        this.updateRollNumber = updateRollNumber;
    }

    public boolean isIncompleteRollNumber() {
        return incompleteRollNumber;
    }

    public void setIncompleteRollNumber(boolean incompleteRollNumber) {
        this.incompleteRollNumber = incompleteRollNumber;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getScholarshipAmount() {
        return scholarshipAmount;
    }

    public void setScholarshipAmount(String scholarshipAmount) {
        this.scholarshipAmount = scholarshipAmount;
    }


    public String getGpaType() {
        return gpaType;
    }

    public void setGpaType(String gpaType) {
        this.gpaType = gpaType;
    }

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    public String getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(String profileStatus) {
        this.profileStatus = profileStatus;
    }

    public String getStatus1K() {
        return status1K;
    }

    public void setStatus1K(String status1K) {
        this.status1K = status1K;
    }

    public String getStatus7K() {
        return status7K;
    }

    public void setStatus7K(String status7K) {
        this.status7K = status7K;
    }

    public String getStatus60K() {
        return status60K;
    }

    public void setStatus60K(String status60K) {
        this.status60K = status60K;
    }

    public boolean isUpdateNewBankProofs() {
        return updateNewBankProofs;
    }

    public void setUpdateNewBankProofs(boolean updateNewBankProofs) {
        this.updateNewBankProofs = updateNewBankProofs;
    }

    public ArrayList<String> getBankProofs() {
        return bankProofs;
    }

    public void setBankProofs(ArrayList<String> bankProofs) {
        this.bankProofs = bankProofs;
    }

    public Map<String, String> getNewBankProofs() {
        return newBankProofs;
    }

    public void setNewBankProofs(Map<String, String> newBankProofs) {
        this.newBankProofs = newBankProofs;
    }

    public String getApprovedBand() {
        return approvedBand;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public void setApprovedBand(String approvedBand) {
        this.approvedBand = approvedBand;
    }

    public boolean isAppliedFor1k() {
        return appliedFor1k;
    }

    public void setAppliedFor1k(boolean appliedFor1k) {
        this.appliedFor1k = appliedFor1k;
    }

    public boolean isAppliedFor7k() {
        return appliedFor7k;
    }

    public void setAppliedFor7k(boolean appliedFor7k) {
        this.appliedFor7k = appliedFor7k;
    }

    public boolean isAppliedFor60k() {
        return appliedFor60k;
    }

    public void setAppliedFor60k(boolean appliedFor60k) {
        this.appliedFor60k = appliedFor60k;
    }

    public int getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(int creditLimit) {
        this.creditLimit = creditLimit;
    }

    public int getAvailableCredit() {
        return availableCredit;
    }

    public void setAvailableCredit(int availableCredit) {
        this.availableCredit = availableCredit;
    }

    public int getCashBack() {
        return cashBack;
    }

    public void setCashBack(int cashBack) {
        this.cashBack = cashBack;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getCurrentAddressCity() {
        return currentAddressCity;
    }

    public boolean isUpdateCurrentAddressCity() {
        return updateCurrentAddressCity;
    }

    public void setCurrentAddressCity(String currentAddressCity) {
        this.currentAddressCity = currentAddressCity;
    }

    public void setUpdateCurrentAddressCity(boolean updateCurrentAddressCity) {
        this.updateCurrentAddressCity = updateCurrentAddressCity;
    }

    public String getFormStatus() {
        return formStatus;
    }

    public void setFormStatus(String formStatus) {
        this.formStatus = formStatus;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isUpdateGender() {
        return updateGender;
    }

    public void setUpdateGender(boolean updateGender) {
        this.updateGender = updateGender;
    }

    public boolean isIncompleteGender() {
        return incompleteGender;
    }

    public void setIncompleteGender(boolean incompleteGender) {
        this.incompleteGender = incompleteGender;
    }

    public boolean isUpdateName() {
        return updateName;
    }

    public void setUpdateName(boolean updateName) {
        this.updateName = updateName;
    }

    public ArrayList<String> getBankStmts() {
        return bankStmts;
    }

    public void setBankStmts(ArrayList<String> bankStmts) {
        this.bankStmts = bankStmts;
    }

    public Map<String, String> getNewBankStmts() {
        return newBankStmts;
    }

    public void setNewBankStmts(Map<String, String> newBankStmts) {
        this.newBankStmts = newBankStmts;
    }

    public boolean isUpdateNewBankStmts() {
        return updateNewBankStmts;
    }

    public void setUpdateNewBankStmts(boolean updateNewBankStmts) {
        this.updateNewBankStmts = updateNewBankStmts;
    }

    public boolean isIncompleteBankStmt() {
        return incompleteBankStmt;
    }

    public void setIncompleteBankStmt(boolean incompleteBankStmt) {
        this.incompleteBankStmt = incompleteBankStmt;
    }

    public String getMonthlyExpenditure() {
        return monthlyExpenditure;
    }

    public void setMonthlyExpenditure(String monthlyExpenditure) {
        this.monthlyExpenditure = monthlyExpenditure;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public boolean isUpdateMonthlyExpenditure() {
        return updateMonthlyExpenditure;
    }

    public void setUpdateMonthlyExpenditure(boolean updateMonthlyExpenditure) {
        this.updateMonthlyExpenditure = updateMonthlyExpenditure;
    }

    public boolean isUpdateVehicle() {
        return updateVehicle;
    }

    public void setUpdateVehicle(boolean updateVehicle) {
        this.updateVehicle = updateVehicle;
    }

    public boolean isUpdateVehicleType() {
        return updateVehicleType;
    }

    public void setUpdateVehicleType(boolean updateVehicleType) {
        this.updateVehicleType = updateVehicleType;
    }

    public boolean isIncompleteMonthlyExpenditure() {
        return incompleteMonthlyExpenditure;
    }

    public void setIncompleteMonthlyExpenditure(boolean incompleteMonthlyExpenditure) {
        this.incompleteMonthlyExpenditure = incompleteMonthlyExpenditure;
    }

    public boolean isIncompleteVehicleDetails() {
        return incompleteVehicleDetails;
    }

    public void setIncompleteVehicleDetails(boolean incompleteVehicleDetails) {
        this.incompleteVehicleDetails = incompleteVehicleDetails;
    }

    public String getScholarship() {
        return scholarship;
    }

    public void setScholarship(String scholarship) {
        this.scholarship = scholarship;
    }

    public String getScholarshipType() {
        return scholarshipType;
    }

    public void setScholarshipType(String scholarshipType) {
        this.scholarshipType = scholarshipType;
    }

    public String getStudentLoan() {
        return studentLoan;
    }

    public void setStudentLoan(String studentLoan) {
        this.studentLoan = studentLoan;
    }

    public boolean isUpdateScholarship() {
        return updateScholarship;
    }

    public void setUpdateScholarship(boolean updateScholarship) {
        this.updateScholarship = updateScholarship;
    }

    public boolean isUpdateScholarshipType() {
        return updateScholarshipType;
    }

    public void setUpdateScholarshipType(boolean updateScholarshipType) {
        this.updateScholarshipType = updateScholarshipType;
    }

    public boolean isUpdateStudentLoan() {
        return updateStudentLoan;
    }

    public void setUpdateStudentLoan(boolean updateStudentLoan) {
        this.updateStudentLoan = updateStudentLoan;
    }

    public boolean isIncompleteScholarship() {
        return incompleteScholarship;
    }

    public void setIncompleteScholarship(boolean incompleteScholarship) {
        this.incompleteScholarship = incompleteScholarship;
    }

    public boolean isIncompleteStudentLoan() {
        return incompleteStudentLoan;
    }

    public void setIncompleteStudentLoan(boolean incompleteStudentLoan) {
        this.incompleteStudentLoan = incompleteStudentLoan;
    }

    public String getAnnualFees() {
        return annualFees;
    }

    public void setAnnualFees(String annualFees) {
        this.annualFees = annualFees;
    }

    public boolean isUpdateAnnualFees() {
        return updateAnnualFees;
    }

    public void setUpdateAnnualFees(boolean updateAnnualFees) {
        this.updateAnnualFees = updateAnnualFees;
    }

    public boolean isIncompleteAnnualFees() {
        return incompleteAnnualFees;
    }

    public void setIncompleteAnnualFees(boolean incompleteAnnualFees) {
        this.incompleteAnnualFees = incompleteAnnualFees;
    }

    public boolean isIncompleteClassmateDetails() {
        return incompleteClassmateDetails;
    }

    public void setIncompleteClassmateDetails(boolean incompleteClassmateDetails) {
        this.incompleteClassmateDetails = incompleteClassmateDetails;
    }

    public boolean isIncompleteVerificationDate() {
        return incompleteVerificationDate;
    }

    public void setIncompleteVerificationDate(boolean incompleteVerificationDate) {
        this.incompleteVerificationDate = incompleteVerificationDate;
    }

    public String getClassmateName() {
        return classmateName;
    }

    public void setClassmateName(String classmateName) {
        this.classmateName = classmateName;
    }

    public String getClassmatePhone() {
        return classmatePhone;
    }

    public void setClassmatePhone(String classmatePhone) {
        this.classmatePhone = classmatePhone;
    }

    public String getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(String verificationDate) {
        this.verificationDate = verificationDate;
    }

    public boolean isUpdateClassmateName() {
        return updateClassmateName;
    }

    public void setUpdateClassmateName(boolean updateClassmateName) {
        this.updateClassmateName = updateClassmateName;
    }

    public boolean isUpdateClassmatePhone() {
        return updateClassmatePhone;
    }

    public void setUpdateClassmatePhone(boolean updateClassmatePhone) {
        this.updateClassmatePhone = updateClassmatePhone;
    }

    public boolean isUpdateVerificationDate() {
        return updateVerificationDate;
    }

    public void setUpdateVerificationDate(boolean updateVerificationDate) {
        this.updateVerificationDate = updateVerificationDate;
    }

    public String getBankAccNum() {
        return bankAccNum;
    }

    public void setBankAccNum(String bankAccNum) {
        this.bankAccNum = bankAccNum;
    }

    public String getBankIfsc() {
        return bankIfsc;
    }

    public void setBankIfsc(String bankIfsc) {
        this.bankIfsc = bankIfsc;
    }

    public boolean isUpdateBankAccNum() {
        return updateBankAccNum;
    }

    public void setUpdateBankAccNum(boolean updateBankAccNum) {
        this.updateBankAccNum = updateBankAccNum;
    }

    public boolean isUpdateBankIfsc() {
        return updateBankIfsc;
    }

    public void setUpdateBankIfsc(boolean updateBankIfsc) {
        this.updateBankIfsc = updateBankIfsc;
    }

    public boolean isIncompleteRepaymentSetup() {
        return incompleteRepaymentSetup;
    }

    public void setIncompleteRepaymentSetup(boolean incompleteRepaymentSetup) {
        this.incompleteRepaymentSetup = incompleteRepaymentSetup;
    }

    public String getFamilyMemberType1() {
        return familyMemberType1;
    }

    public void setFamilyMemberType1(String familyMemberType1) {
        this.familyMemberType1 = familyMemberType1;
    }

    public String getProfessionFamilyMemberType1() {
        return professionFamilyMemberType1;
    }

    public void setProfessionFamilyMemberType1(String professionFamilyMemberType1) {
        this.professionFamilyMemberType1 = professionFamilyMemberType1;
    }

    public String getDesignationFamilyMemberType1() {
        return designationFamilyMemberType1;
    }

    public void setDesignationFamilyMemberType1(String designationFamilyMemberType1) {
        this.designationFamilyMemberType1 = designationFamilyMemberType1;
    }

    public String getPhoneFamilyMemberType1() {
        return phoneFamilyMemberType1;
    }

    public void setPhoneFamilyMemberType1(String phoneFamilyMemberType1) {
        this.phoneFamilyMemberType1 = phoneFamilyMemberType1;
    }

    public String getFamilyMemberType2() {
        return familyMemberType2;
    }

    public void setFamilyMemberType2(String familyMemberType2) {
        this.familyMemberType2 = familyMemberType2;
    }

    public String getProfessionFamilyMemberType2() {
        return professionFamilyMemberType2;
    }

    public void setProfessionFamilyMemberType2(String professionFamilyMemberType2) {
        this.professionFamilyMemberType2 = professionFamilyMemberType2;
    }

    public String getDesignationFamilyMemberType2() {
        return designationFamilyMemberType2;
    }

    public void setDesignationFamilyMemberType2(String designationFamilyMemberType2) {
        this.designationFamilyMemberType2 = designationFamilyMemberType2;
    }

    public String getPhoneFamilyMemberType2() {
        return phoneFamilyMemberType2;
    }

    public void setPhoneFamilyMemberType2(String phoneFamilyMemberType2) {
        this.phoneFamilyMemberType2 = phoneFamilyMemberType2;
    }

    public boolean isUpdateFamilyMemberType1() {
        return updateFamilyMemberType1;
    }

    public void setUpdateFamilyMemberType1(boolean updateFamilyMemberType1) {
        this.updateFamilyMemberType1 = updateFamilyMemberType1;
    }

    public boolean isUpdateProfessionFamilyMemberType1() {
        return updateProfessionFamilyMemberType1;
    }

    public void setUpdateProfessionFamilyMemberType1(boolean updateProfessionFamilyMemberType1) {
        this.updateProfessionFamilyMemberType1 = updateProfessionFamilyMemberType1;
    }

    public boolean isUpdateDesignationFamilyMemberType1() {
        return updateDesignationFamilyMemberType1;
    }

    public void setUpdateDesignationFamilyMemberType1(boolean updateDesignationFamilyMemberType1) {
        this.updateDesignationFamilyMemberType1 = updateDesignationFamilyMemberType1;
    }

    public boolean isUpdatePhoneFamilyMemberType1() {
        return updatePhoneFamilyMemberType1;
    }

    public void setUpdatePhoneFamilyMemberType1(boolean updatePhoneFamilyMemberType1) {
        this.updatePhoneFamilyMemberType1 = updatePhoneFamilyMemberType1;
    }

    public boolean isUpdateFamilyMemberType2() {
        return updateFamilyMemberType2;
    }

    public void setUpdateFamilyMemberType2(boolean updateFamilyMemberType2) {
        this.updateFamilyMemberType2 = updateFamilyMemberType2;
    }

    public boolean isUpdateProfessionFamilyMemberType2() {
        return updateProfessionFamilyMemberType2;
    }

    public void setUpdateProfessionFamilyMemberType2(boolean updateProfessionFamilyMemberType2) {
        this.updateProfessionFamilyMemberType2 = updateProfessionFamilyMemberType2;
    }

    public boolean isUpdateDesignationFamilyMemberType2() {
        return updateDesignationFamilyMemberType2;
    }

    public void setUpdateDesignationFamilyMemberType2(boolean updateDesignationFamilyMemberType2) {
        this.updateDesignationFamilyMemberType2 = updateDesignationFamilyMemberType2;
    }

    public boolean isUpdatePhoneFamilyMemberType2() {
        return updatePhoneFamilyMemberType2;
    }

    public void setUpdatePhoneFamilyMemberType2(boolean updatePhoneFamilyMemberType2) {
        this.updatePhoneFamilyMemberType2 = updatePhoneFamilyMemberType2;
    }

    public boolean isIncompleteFamilyDetails() {
        return incompleteFamilyDetails;
    }

    public void setIncompleteFamilyDetails(boolean incompleteFamilyDetails) {
        this.incompleteFamilyDetails = incompleteFamilyDetails;
    }

    public boolean isIncompleteDOB() {
        return incompleteDOB;
    }

    public void setIncompleteDOB(boolean incompleteDOB) {
        this.incompleteDOB = incompleteDOB;
    }

    public boolean isIncompleteAddressDetails() {
        return incompleteAddressDetails;
    }

    public void setIncompleteAddressDetails(boolean incompleteAddressDetails) {
        this.incompleteAddressDetails = incompleteAddressDetails;
    }

    public String getSelfie() {
        return selfie;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(String accommodation) {
        this.accommodation = accommodation;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public boolean isUpdatePermanentAddress() {
        return updatePermanentAddress;
    }

    public void setUpdatePermanentAddress(boolean updatePermanentAddress) {
        this.updatePermanentAddress = updatePermanentAddress;
    }

    public boolean isUpdateCurrentAddress() {
        return updateCurrentAddress;
    }

    public void setUpdateCurrentAddress(boolean updateCurrentAddress) {
        this.updateCurrentAddress = updateCurrentAddress;
    }

    public boolean isUpdateAccommodation() {
        return updateAccommodation;
    }

    public void setUpdateAccommodation(boolean updateAccommodation) {
        this.updateAccommodation = updateAccommodation;
    }

    public boolean isUpdateDOB() {
        return updateDOB;
    }

    public void setUpdateDOB(boolean updateDOB) {
        this.updateDOB = updateDOB;
    }

    public void setSelfie(String selfie) {
        this.selfie = selfie;
    }

    public boolean isUpdateSelfie() {
        return updateSelfie;
    }

    public void setUpdateSelfie(boolean updateSelfie) {
        this.updateSelfie = updateSelfie;
    }

    public boolean isIncompleteAadhar() {
        return incompleteAadhar;
    }

    public void setIncompleteAadhar(boolean incompleteAadhar) {
        this.incompleteAadhar = incompleteAadhar;
    }

    public boolean isIncompletePermanentAddress() {
        return incompletePermanentAddress;
    }

    public void setIncompletePermanentAddress(boolean incompletePermanentAddress) {
        this.incompletePermanentAddress = incompletePermanentAddress;
    }

    public boolean isIncompleteEmail() {
        return incompleteEmail;
    }

    public void setIncompleteEmail(boolean incompleteEmail) {
        this.incompleteEmail = incompleteEmail;
    }

    public boolean isIncompleteFb() {
        return incompleteFb;
    }

    public void setIncompleteFb(boolean incompleteFb) {
        this.incompleteFb = incompleteFb;
    }

    public boolean isUpdateAadharNumber() {
        return updateAadharNumber;
    }

    public void setUpdateAadharNumber(boolean updateAadharNumber) {
        this.updateAadharNumber = updateAadharNumber;
    }

    public boolean isUpdatePanNumber() {
        return updatePanNumber;
    }

    public void setUpdatePanNumber(boolean updatePanNumber) {
        this.updatePanNumber = updatePanNumber;
    }

    public boolean isUpdateEmail() {
        return updateEmail;
    }

    public void setUpdateEmail(boolean updateEmail) {
        this.updateEmail = updateEmail;
    }

    public boolean isUpdateFbConnected() {
        return updateFbConnected;
    }

    public void setUpdateFbConnected(boolean updateFbConnected) {
        this.updateFbConnected = updateFbConnected;
    }


    public Map<String, String> getNewCollegeIds() {
        return newCollegeIds;
    }

    public void addCollegeId(int index, String collegeId, ArrayList<String> collegeIds) {
        collegeIds.add(index, collegeId);
    }

    public void addBankStmts(int index, String bankStmt, ArrayList<String> bankStmts) {
        bankStmts.add(index, bankStmt);
    }

    public void addBankProofs(int index, String bankProof, ArrayList<String> bankProofs) {
        bankProofs.add(index, bankProof);
    }

    public void addAddressProofs(int index, String addressProof, ArrayList<String> addressProofs) {
        addressProofs.add(index, addressProof);
    }

    public void setNewCollegeIds(Map<String, String> newCollegeIds) {
        this.newCollegeIds = newCollegeIds;
    }

    public boolean isUpdateNewCollegeIds() {
        return updateNewCollegeIds;
    }

    public void setUpdateNewCollegeIds(boolean updateNewCollegeIds) {
        this.updateNewCollegeIds = updateNewCollegeIds;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public boolean isUpdateCollegeName() {
        return updateCollegeName;
    }

    public void setUpdateCollegeName(boolean updateCollegeName) {
        this.updateCollegeName = updateCollegeName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public boolean isUpdateCourseName() {
        return updateCourseName;
    }

    public void setUpdateCourseName(boolean updateCourseName) {
        this.updateCourseName = updateCourseName;
    }

    public String getCourseEndDate() {
        return courseEndDate;
    }

    public void setCourseEndDate(String courseEndDate) {
        this.courseEndDate = courseEndDate;
    }

    public boolean isUpdateCourseEndDate() {
        return updateCourseEndDate;
    }

    public void setUpdateCourseEndDate(boolean updateCourseEndDate) {
        this.updateCourseEndDate = updateCourseEndDate;
    }

    public ArrayList<String> getCollegeIds() {
        return collegeIds;
    }

    public void setCollegeIds(ArrayList<String> collegeIds) {
        this.collegeIds = collegeIds;
    }

    public ArrayList<String> getAddressProofs() {
        return addressProofs;
    }

    public void setAddressProofs(ArrayList<String> addressProofs) {
        this.addressProofs = addressProofs;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isFbConnected() {
        return isFbConnected;
    }

    public void setIsFbConnected(boolean isFbConnected) {
        this.isFbConnected = isFbConnected;
    }

    public String getFbUserId() {
        return fbUserId;
    }

    public void setFbUserId(String fbUserId) {
        this.fbUserId = fbUserId;
    }

    public Map<String, String> getNewAddressProofs() {
        return newAddressProofs;
    }

    public void setNewAddressProofs(Map<String, String> newAddressProofs) {
        this.newAddressProofs = newAddressProofs;
    }

    public boolean isUpdateNewAddressProofs() {
        return updateNewAddressProofs;
    }

    public void setUpdateNewAddressProofs(boolean updateNewAddressProofs) {
        this.updateNewAddressProofs = updateNewAddressProofs;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getPanOrAadhar() {
        return panOrAadhar;
    }

    public void setPanOrAadhar(String panOrAadhar) {
        this.panOrAadhar = panOrAadhar;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public boolean isUpdateSignature() {
        return updateSignature;
    }

    public void setUpdateSignature(boolean updateSignature) {
        this.updateSignature = updateSignature;
    }

    public boolean isIncompleteCollegeId() {
        return incompleteCollegeId;
    }

    public void setIncompleteCollegeId(boolean incompleteCollegeId) {
        this.incompleteCollegeId = incompleteCollegeId;
    }

    public boolean isIncompleteCollegeDetails() {
        return incompleteCollegeDetails;
    }

    public void setIncompleteCollegeDetails(boolean incompleteCollegeDetails) {
        this.incompleteCollegeDetails = incompleteCollegeDetails;
    }

    public boolean isGpaTypeUpdate() {
        return gpaTypeUpdate;
    }

    public void setGpaTypeUpdate(boolean gpaTypeUpdate) {
        this.gpaTypeUpdate = gpaTypeUpdate;
    }

    public boolean isGpaValueUpdate() {
        return gpaValueUpdate;
    }

    public void setGpaValueUpdate(boolean gpaValueUpdate) {
        this.gpaValueUpdate = gpaValueUpdate;
    }


    public boolean isInCompleteAgreement() {
        return isInCompleteAgreement;
    }

    public void setInCompleteAgreement(boolean inCompleteAgreement) {
        isInCompleteAgreement = inCompleteAgreement;
    }

    public String getPrefferedLanguageFamilyMemberType1() {
        return prefferedLanguageFamilyMemberType1;
    }

    public void setPrefferedLanguageFamilyMemberType1(String prefferedLanguageFamilyMemberType1) {
        this.prefferedLanguageFamilyMemberType1 = prefferedLanguageFamilyMemberType1;
    }

    public String getPrefferedLanguageFamilyMemberType2() {
        return prefferedLanguageFamilyMemberType2;
    }

    public void setPrefferedLanguageFamilyMemberType2(String prefferedLanguageFamilyMemberType2) {
        this.prefferedLanguageFamilyMemberType2 = prefferedLanguageFamilyMemberType2;
    }

    public boolean isUpdatePreferredLanguageFamilyMemberType1() {
        return updatePreferredLanguageFamilyMemberType1;
    }

    public void setUpdatePreferredLanguageFamilyMemberType1(boolean updatePreferredLanguageFamilyMemberType1) {
        this.updatePreferredLanguageFamilyMemberType1 = updatePreferredLanguageFamilyMemberType1;
    }

    public boolean isUpdatePreferredLanguageFamilyMemberType2() {
        return updatePreferredLanguageFamilyMemberType2;
    }

    public void setUpdatePreferredLanguageFamilyMemberType2(boolean updatePreferredLanguageFamilyMemberType2) {
        this.updatePreferredLanguageFamilyMemberType2 = updatePreferredLanguageFamilyMemberType2;
    }

    public String getSignatureStatus() {
        return signatureStatus;
    }

    public void setSignatureStatus(String signatureStatus) {
        this.signatureStatus = signatureStatus;
    }

    public String getSelfieStatus() {
        return selfieStatus;
    }

    public void setSelfieStatus(String selfieStatus) {
        this.selfieStatus = selfieStatus;
    }


    public boolean isIncompleteGpa() {
        return incompleteGpa;
    }

    public void setIncompleteGpa(boolean incompleteGpa) {
        this.incompleteGpa = incompleteGpa;
    }

    public ArrayList<String> getMarksheets() {
        return marksheets;
    }

    public void setMarksheets(ArrayList<String> marksheets) {
        this.marksheets = marksheets;
    }

    public Map<String, String> getNewMarksheets() {
        return newMarksheets;
    }

    public void setNewMarksheets(Map<String, String> newMarksheets) {
        this.newMarksheets = newMarksheets;
    }

    public boolean isUpdateNewMarksheets() {
        return updateNewMarksheets;
    }

    public void setUpdateNewMarksheets(boolean updateNewMarksheets) {
        this.updateNewMarksheets = updateNewMarksheets;
    }

    public String getCurrentAddressLine2() {
        return currentAddressLine2;
    }

    public void setCurrentAddressLine2(String currentAddressLine2) {
        this.currentAddressLine2 = currentAddressLine2;
    }

    public String getCurrentAddressPinCode() {
        return currentAddressPinCode;
    }

    public void setCurrentAddressPinCode(String currentAddressPinCode) {
        this.currentAddressPinCode = currentAddressPinCode;
    }

    public String getPermanentAddressLine2() {
        return permanentAddressLine2;
    }

    public void setPermanentAddressLine2(String permanentAddressLine2) {
        this.permanentAddressLine2 = permanentAddressLine2;
    }

    public String getPermanentAddressPinCode() {
        return permanentAddressPinCode;
    }

    public void setPermanentAddressPinCode(String permanentAddressPinCode) {
        this.permanentAddressPinCode = permanentAddressPinCode;
    }

    public String getPermanentAddressCity() {
        return permanentAddressCity;
    }

    public void setPermanentAddressCity(String permanentAddressCity) {
        this.permanentAddressCity = permanentAddressCity;
    }

    public boolean isIncompleteMarksheets() {
        return incompleteMarksheets;
    }

    public void setIncompleteMarksheets(boolean incompleteMarksheets) {
        this.incompleteMarksheets = incompleteMarksheets;
    }

    public Image getGradeSheet() {
        return gradeSheet;
    }

    public void setGradeSheet(Image gradeSheet) {
        this.gradeSheet = gradeSheet;
    }

    public Image getCollegeID() {
        return collegeID;
    }

    public void setCollegeID(Image collegeID) {
        this.collegeID = collegeID;
    }

    public Image getBankProof() {
        return bankProof;
    }

    public void setBankProof(Image bankProof) {
        this.bankProof = bankProof;
    }

    public Image getAddressProof() {
        return addressProof;
    }

    public void setAddressProof(Image addressProof) {
        this.addressProof = addressProof;
    }

    public Image getBankStatement() {
        return bankStatement;
    }

    public void setBankStatement(Image bankStatement) {
        this.bankStatement = bankStatement;
    }

    public String getFullCurrentAddress() {
        StringBuffer currentAddressStr = new StringBuffer("");
        if (AppUtils.isNotEmpty(currentAddress)) {
            currentAddressStr.append(currentAddress);
        }
        if (AppUtils.isNotEmpty(currentAddressLine2)) {
            currentAddressStr.append("," + currentAddressLine2);
        }
        if (AppUtils.isNotEmpty(currentAddressCity)) {
            currentAddressStr.append("," + currentAddressCity);
        }
        if (AppUtils.isNotEmpty(currentAddressPinCode)) {
            currentAddressStr.append("," + currentAddressPinCode);
        }
        return currentAddressStr.toString();

    }

    public String getFullPermanentAddress() {
        StringBuffer permanentAddressStr = new StringBuffer("");
        if (AppUtils.isNotEmpty(permanentAddress)) {
            permanentAddressStr.append(permanentAddress);
        }
        if (AppUtils.isNotEmpty(permanentAddressLine2)) {
            permanentAddressStr.append("," + permanentAddressLine2);
        }
        if (AppUtils.isNotEmpty(permanentAddressCity)) {
            permanentAddressStr.append("," + permanentAddressCity);
        }
        if (AppUtils.isNotEmpty(permanentAddressPinCode)) {
            permanentAddressStr.append("," + permanentAddressPinCode);
        }
        return permanentAddressStr.toString();

    }

    public FrontBackImage getPanProof() {
        return panProof;
    }

    public void setPanProof(FrontBackImage panProof) {
        this.panProof = panProof;
    }


    public boolean isUpdatePanProof() {
        return updatePanProof;
    }

    public void setUpdatePanProof(boolean updatePanProof) {
        this.updatePanProof = updatePanProof;
    }
}
