package com.example.nlpladmin.model;

public class DriverModel {
    private String user_id;
    private String truck_id;
    private String driver_id;
    private String driver_name;
    private String upload_dl;
    private String driver_number;
    private String driver_emailId;
    private String driver_selfie;
    private String created_at;
    private String updated_at;
    private String updated_by;
    private String is_driver_deleted;
    private String deleted_at;
    private String deleted_by;
    private String is_dl_verified;
    private String is_selfie_verified;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getDriver_number() {
        return driver_number;
    }

    public void setDriver_number(String driver_number) {
        this.driver_number = driver_number;
    }

    public String getDriver_emailId() {
        return driver_emailId;
    }

    public void setDriver_emailId(String driver_emailId) {
        this.driver_emailId = driver_emailId;
    }

    public String getDriver_selfie() {
        return driver_selfie;
    }

    public void setDriver_selfie(String driver_selfie) {
        this.driver_selfie = driver_selfie;
    }

    public String getTruck_id() {
        return truck_id;
    }

    public void setTruck_id(String truck_id) {
        this.truck_id = truck_id;
    }

    public String getUpload_dl() {
        return upload_dl;
    }

    public void setUpload_dl(String upload_dl) {
        this.upload_dl = upload_dl;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public String getIs_driver_deleted() {
        return is_driver_deleted;
    }

    public void setIs_driver_deleted(String is_driver_deleted) {
        this.is_driver_deleted = is_driver_deleted;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getDeleted_by() {
        return deleted_by;
    }

    public void setDeleted_by(String deleted_by) {
        this.deleted_by = deleted_by;
    }

    public String getIs_dl_verified() {
        return is_dl_verified;
    }

    public void setIs_dl_verified(String is_dl_verified) {
        this.is_dl_verified = is_dl_verified;
    }

    public String getIs_selfie_verified() {
        return is_selfie_verified;
    }

    public void setIs_selfie_verified(String is_selfie_verified) {
        this.is_selfie_verified = is_selfie_verified;
    }
}
