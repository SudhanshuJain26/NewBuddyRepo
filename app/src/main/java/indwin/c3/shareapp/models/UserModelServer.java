package indwin.c3.shareapp.models;

import java.util.List;

import indwin.c3.shareapp.utils.Constants;

/**
 * Created by rock on 6/24/16.
 */
public class UserModelServer {

    private String gender;
    private String userid;
    private String name;
    private String email;
    private String rollNumber;
    private boolean emailVerified = false;
    private boolean fbConnected;
    private String fbUserId;
    private String college;
    private String course;
    private String courseCompletionDate;
    private String aadhar;//to check
    private String pan;
    private String panOrAadhar;
    private List<String> signature;
    private List<String> selfie;
    private String dob;
    private Address currentAddress;
    private Address permanentAddress;
    private String accommodation;
    private boolean tncAccepted;
    private boolean scholarship;
    private String scholarshipProgram;
    private String monthlyExpeses;
    private boolean vehicle;
    private String vehicleType;
    private boolean takenLoan;
    private String bankAccountNumber;
    private String bankIFSC;
    private String friendName;
    private String friendNumber;
    private String collegeIdVerificationDate;
    private String annualFees;
    private String gpaType;
    private String gpa;
    private String scholarshipAmount;
    private String profileStatus;
    private Image gradeSheet;
    private FrontBackImage panProof;
    private Image collegeID;
    private String panStatus;
    private Image bankProof;
    private Image addressProof;
    private Image bankStatement;
    private String formStatus;
    private String status1K;
    private String status7K;
    private String status60K;
    private String rejectionReason;
    private int creditLimit = 0;
    private int availableCredit = 0;
    private int totalBorrowed = 0;
    private int totalCashback = 0;
    private String approvedBand;
    private List<FamilyMember> familyMember;

    private boolean appliedFor1k = false;
    private boolean appliedFor7k = false;
    private boolean appliedFor60k = false;
    private boolean emailSent;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isFbConnected() {
        return fbConnected;
    }

    public void setFbConnected(boolean fbConnected) {
        this.fbConnected = fbConnected;
    }

    public String getFbUserId() {
        return fbUserId;
    }

    public void setFbUserId(String fbUserId) {
        this.fbUserId = fbUserId;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCourseCompletionDate() {
        return courseCompletionDate;
    }

    public void setCourseCompletionDate(String courseCompletionDate) {
        this.courseCompletionDate = courseCompletionDate;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getPanOrAadhar() {
        return panOrAadhar;
    }

    public void setPanOrAadhar(String panOrAadhar) {
        this.panOrAadhar = panOrAadhar;
    }

    public List<String> getSignature() {
        return signature;
    }

    public void setSignature(List<String> signature) {
        this.signature = signature;
    }

    public List<String> getSelfie() {
        return selfie;
    }

    public void setSelfie(List<String> selfie) {
        this.selfie = selfie;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Address getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(Address currentAddress) {
        this.currentAddress = currentAddress;
    }

    public Address getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(Address permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(String accommodation) {
        this.accommodation = accommodation;
    }

    public boolean isTncAccepted() {
        return tncAccepted;
    }

    public void setTncAccepted(boolean tncAccepted) {
        this.tncAccepted = tncAccepted;
    }

    public boolean isScholarship() {
        return scholarship;
    }

    public void setScholarship(boolean scholarship) {
        this.scholarship = scholarship;
    }

    public String getScholarshipProgram() {
        return scholarshipProgram;
    }

    public void setScholarshipProgram(String scholarshipProgram) {
        this.scholarshipProgram = scholarshipProgram;
    }

    public String getMonthlyExpeses() {
        return monthlyExpeses;
    }

    public void setMonthlyExpeses(String monthlyExpeses) {
        this.monthlyExpeses = monthlyExpeses;
    }

    public boolean isVehicle() {
        return vehicle;
    }

    public void setVehicle(boolean vehicle) {
        this.vehicle = vehicle;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public boolean isTakenLoan() {
        return takenLoan;
    }

    public void setTakenLoan(boolean takenLoan) {
        this.takenLoan = takenLoan;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankIFSC() {
        return bankIFSC;
    }

    public void setBankIFSC(String bankIFSC) {
        this.bankIFSC = bankIFSC;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendNumber() {
        return friendNumber;
    }

    public void setFriendNumber(String friendNumber) {
        this.friendNumber = friendNumber;
    }

    public String getCollegeIdVerificationDate() {
        return collegeIdVerificationDate;
    }

    public void setCollegeIdVerificationDate(String collegeIdVerificationDate) {
        this.collegeIdVerificationDate = collegeIdVerificationDate;
    }

    public String getAnnualFees() {
        return annualFees;
    }

    public void setAnnualFees(String annualFees) {
        this.annualFees = annualFees;
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

    public String getScholarshipAmount() {
        return scholarshipAmount;
    }

    public void setScholarshipAmount(String scholarshipAmount) {
        this.scholarshipAmount = scholarshipAmount;
    }

    public String getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(String profileStatus) {
        this.profileStatus = profileStatus;
    }

    public Image getGradeSheet() {
        return gradeSheet;
    }

    public void setGradeSheet(Image gradeSheet) {
        this.gradeSheet = gradeSheet;
    }

    public FrontBackImage getPanProof() {
        return panProof;
    }

    public void setPanProof(FrontBackImage panProof) {
        this.panProof = panProof;
    }

    public Image getCollegeID() {
        return collegeID;
    }

    public void setCollegeID(Image collegeID) {
        this.collegeID = collegeID;
    }

    public String getPanStatus() {
        return panStatus;
    }

    public void setPanStatus(String panStatus) {
        this.panStatus = panStatus;
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

    public String getFormStatus() {
        return formStatus;
    }

    public void setFormStatus(String formStatus) {
        this.formStatus = formStatus;
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

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
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

    public int getTotalBorrowed() {
        return totalBorrowed;
    }

    public void setTotalBorrowed(int totalBorrowed) {
        this.totalBorrowed = totalBorrowed;
    }

    public int getTotalCashback() {
        return totalCashback;
    }

    public void setTotalCashback(int totalCashback) {
        this.totalCashback = totalCashback;
    }

    public String getApprovedBand() {
        return approvedBand;
    }

    public void setApprovedBand(String approvedBand) {
        this.approvedBand = approvedBand;
    }

    public List<FamilyMember> getFamilyMember() {
        return familyMember;
    }

    public void setFamilyMember(List<FamilyMember> familyMember) {
        this.familyMember = familyMember;
    }

    public boolean isAppliedFor1k() {

        if (Constants.STATUS.DECLINED.toString().equals(status1K) || Constants.STATUS.WAITLISTED.toString().equals(status1K) || Constants.STATUS.APPLIED.toString().equals(status1K) || Constants.STATUS.APPROVED.toString().equals(status1K)) {
            return true;
        }
        return false;
    }

    public void setAppliedFor1k(boolean appliedFor1k) {
        this.appliedFor1k = appliedFor1k;
    }

    public boolean isAppliedFor7k() {
        if (Constants.STATUS.DECLINED.toString().equals(status7K) || Constants.STATUS.WAITLISTED.toString().equals(status7K) || Constants.STATUS.APPLIED.toString().equals(status7K) || Constants.STATUS.APPROVED.toString().equals(status7K)) {
            return true;
        }
        return false;

    }

    public void setAppliedFor7k(boolean appliedFor7k) {
        this.appliedFor7k = appliedFor7k;
    }

    public boolean isAppliedFor60k() {
        if (Constants.STATUS.DECLINED.toString().equals(status60K) || Constants.STATUS.WAITLISTED.toString().equals(status60K) || Constants.STATUS.APPLIED.toString().equals(status60K) || Constants.STATUS.APPROVED.toString().equals(status60K)) {
            return true;
        }
        return false;

    }

    public void setAppliedFor60k(boolean appliedFor60k) {
        this.appliedFor60k = appliedFor60k;
    }

    public boolean isEmailSent() {
        return emailSent;
    }

    public void setEmailSent(boolean emailSent) {
        this.emailSent = emailSent;
    }
}




