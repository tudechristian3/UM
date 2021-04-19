package com.ultramega.shop.pojo;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConsumerProfile implements Parcelable {
    @SerializedName("ID")
    @Expose
    private int ID;
    @SerializedName("ConsumerID")
    @Expose
    private String ConsumerID;
    @SerializedName("FacebookID")
    @Expose
    private String FacebookID;
    @SerializedName("UserID")
    @Expose
    private String UserID;
    @SerializedName("Password")
    @Expose
    private String Password;
    @SerializedName("ConsumerType")
    @Expose
    private String ConsumerType;
    @SerializedName("FirstName")
    @Expose
    private String FirstName;
    @SerializedName("LastName")
    @Expose
    private String LastName;
    @SerializedName("BirthDate")
    @Expose
    private String BirthDate;
    @SerializedName("Gender")
    @Expose
    private String Gender;
    @SerializedName("Occupation")
    @Expose
    private String Occupation;
    @SerializedName("Interest")
    @Expose
    private String Interest;
    @SerializedName("EmailAddress")
    @Expose
    private String EmailAddress;
    @SerializedName("isVerified")
    @Expose
    private int isVerified;
    @SerializedName("CountryCode")
    @Expose
    private String CountryCode;
    @SerializedName("MobileNo")
    @Expose
    private String MobileNo;
    @SerializedName("IMEI")
    @Expose
    private String IMEI;
    @SerializedName("ProfilePicURL")
    @Expose
    private String ProfilePicURL;
    @SerializedName("DateTimeRegistered")
    @Expose
    private String DateTimeRegistered;
    @SerializedName("DateTimeUpdated")
    @Expose
    private String DateTimeUpdated;
    @SerializedName("LastLogin")
    @Expose
    private String LastLogin;
    @SerializedName("Status")
    @Expose
    private String Status;
    @SerializedName("RewardPoints")
    @Expose
    private double RewardPoints;
    @SerializedName("Extra1")
    @Expose
    private String Extra1;
    @SerializedName("Extra2")
    @Expose
    private String Extra2;
    @SerializedName("Extra3")
    @Expose
    private String Extra3;
    @SerializedName("Extra4")
    @Expose
    private String Extra4;
    @SerializedName("Notes1")
    @Expose
    private String Notes1;
    @SerializedName("Notes2")
    @Expose
    private String Notes2;

    public ConsumerProfile(){
        super();
    }

    public ConsumerProfile(String consumerID, String userID){
        ConsumerID = consumerID;
        UserID = userID;
    }

    public ConsumerProfile(String firstName, String lastName, String mobileNo){
        FirstName = firstName;
        LastName = lastName;
        MobileNo = mobileNo;
    }

    public ConsumerProfile(int ID, String consumerID, String facebookID, String userID, String password, String consumerType, String firstName, String lastName, String birthDate, String gender, String occupation, String interest, String emailAddress, int isVerified, String countryCode, String mobileNo, String IMEI, String profilePicURL, String dateTimeRegistered, String dateTimeUpdated, String lastLogin, String status, double rewardPoints, String extra1, String extra2, String extra3, String extra4, String notes1, String notes2) {
        this.ID = ID;
        ConsumerID = consumerID;
        FacebookID = facebookID;
        UserID = userID;
        Password = password;
        ConsumerType = consumerType;
        FirstName = firstName;
        LastName = lastName;
        BirthDate = birthDate;
        Gender = gender;
        Occupation = occupation;
        Interest = interest;
        EmailAddress = emailAddress;
        this.isVerified = isVerified;
        CountryCode = countryCode;
        MobileNo = mobileNo;
        this.IMEI = IMEI;
        ProfilePicURL = profilePicURL;
        DateTimeRegistered = dateTimeRegistered;
        DateTimeUpdated = dateTimeUpdated;
        LastLogin = lastLogin;
        Status = status;
        RewardPoints = rewardPoints;
        Extra1 = extra1;
        Extra2 = extra2;
        Extra3 = extra3;
        Extra4 = extra4;
        Notes1 = notes1;
        Notes2 = notes2;
    }

    private ConsumerProfile(Parcel in) {
        ID = in.readInt();
        ConsumerID = in.readString();
        FacebookID = in.readString();
        UserID = in.readString();
        Password = in.readString();
        ConsumerType = in.readString();
        FirstName = in.readString();
        LastName = in.readString();
        BirthDate = in.readString();
        Gender = in.readString();
        Occupation = in.readString();
        Interest = in.readString();
        EmailAddress = in.readString();
        isVerified = in.readInt();
        CountryCode = in.readString();
        MobileNo = in.readString();
        IMEI = in.readString();
        ProfilePicURL = in.readString();
        DateTimeRegistered = in.readString();
        DateTimeUpdated = in.readString();
        LastLogin = in.readString();
        Status = in.readString();
        RewardPoints = in.readDouble();
        Extra1 = in.readString();
        Extra2 = in.readString();
        Extra3 = in.readString();
        Extra4 = in.readString();
        Notes1 = in.readString();
        Notes2 = in.readString();
    }

    public static final Creator<ConsumerProfile> CREATOR = new Creator<ConsumerProfile>() {
        @Override
        public ConsumerProfile createFromParcel(Parcel in) {
            return new ConsumerProfile(in);
        }

        @Override
        public ConsumerProfile[] newArray(int size) {
            return new ConsumerProfile[size];
        }
    };

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getConsumerID() {
        return ConsumerID;
    }

    public void setConsumerID(String consumerID) {
        ConsumerID = consumerID;
    }

    public String getFacebookID() {
        return FacebookID;
    }

    public void setFacebookID(String facebookID) {
        FacebookID = facebookID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getConsumerType() {
        return ConsumerType;
    }

    public void setConsumerType(String consumerType) {
        ConsumerType = consumerType;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getOccupation() {
        return Occupation;
    }

    public void setOccupation(String occupation) {
        Occupation = occupation;
    }

    public String getInterest() {
        return Interest;
    }

    public void setInterest(String interest) {
        Interest = interest;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public int getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(int isVerified) {
        this.isVerified = isVerified;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public String getProfilePicURL() {
        return ProfilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        ProfilePicURL = profilePicURL;
    }

    public String getDateTimeRegistered() {
        return DateTimeRegistered;
    }

    public void setDateTimeRegistered(String dateTimeRegistered) {
        DateTimeRegistered = dateTimeRegistered;
    }

    public String getDateTimeUpdated() {
        return DateTimeUpdated;
    }

    public void setDateTimeUpdated(String dateTimeUpdated) {
        DateTimeUpdated = dateTimeUpdated;
    }

    public String getLastLogin() {
        return LastLogin;
    }

    public void setLastLogin(String lastLogin) {
        LastLogin = lastLogin;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public double getRewardPoints() {
        return RewardPoints;
    }

    public void setRewardPoints(double rewardPoints) {
        RewardPoints = rewardPoints;
    }

    public String getExtra1() {
        return Extra1;
    }

    public void setExtra1(String extra1) {
        Extra1 = extra1;
    }

    public String getExtra2() {
        return Extra2;
    }

    public void setExtra2(String extra2) {
        Extra2 = extra2;
    }

    public String getExtra3() {
        return Extra3;
    }

    public void setExtra3(String extra3) {
        Extra3 = extra3;
    }

    public String getExtra4() {
        return Extra4;
    }

    public void setExtra4(String extra4) {
        Extra4 = extra4;
    }

    public String getNotes1() {
        return Notes1;
    }

    public void setNotes1(String notes1) {
        Notes1 = notes1;
    }

    public String getNotes2() {
        return Notes2;
    }

    public void setNotes2(String notes2) {
        Notes2 = notes2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(ConsumerID);
        dest.writeString(FacebookID);
        dest.writeString(UserID);
        dest.writeString(Password);
        dest.writeString(ConsumerType);
        dest.writeString(FirstName);
        dest.writeString(LastName);
        dest.writeString(BirthDate);
        dest.writeString(Gender);
        dest.writeString(Occupation);
        dest.writeString(Interest);
        dest.writeString(EmailAddress);
        dest.writeInt(isVerified);
        dest.writeString(CountryCode);
        dest.writeString(MobileNo);
        dest.writeString(IMEI);
        dest.writeString(ProfilePicURL);
        dest.writeString(DateTimeRegistered);
        dest.writeString(DateTimeUpdated);
        dest.writeString(LastLogin);
        dest.writeString(Status);
        dest.writeDouble(RewardPoints);
        dest.writeString(Extra1);
        dest.writeString(Extra2);
        dest.writeString(Extra3);
        dest.writeString(Extra4);
        dest.writeString(Notes1);
        dest.writeString(Notes2);
    }
}
