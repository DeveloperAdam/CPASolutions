package com.techease.cpasolutions.Retro;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Adamnoor on 8/7/2018.
 */

public class UpdateProfileData {


    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("zip")
    @Expose
    private String zip;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("client_email")
    @Expose
    private String clientEmail;
    @SerializedName("signature_email")
    @Expose
    private String signatureEmail;
    @SerializedName("all_email")
    @Expose
    private String allEmail;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getSignatureEmail() {
        return signatureEmail;
    }

    public void setSignatureEmail(String signatureEmail) {
        this.signatureEmail = signatureEmail;
    }

    public String getAllEmail() {
        return allEmail;
    }

    public void setAllEmail(String allEmail) {
        this.allEmail = allEmail;
    }
}
