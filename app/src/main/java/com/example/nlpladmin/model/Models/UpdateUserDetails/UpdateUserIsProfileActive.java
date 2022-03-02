package com.example.nlpladmin.model.Models.UpdateUserDetails;

public class UpdateUserIsProfileActive {
    String is_account_active;

    public UpdateUserIsProfileActive(String is_account_active) {
        this.is_account_active = is_account_active;
    }

    @Override
    public String toString() {
        return "UpdateUserIsProfileActive{" +
                "is_account_active='" + is_account_active + '\'' +
                '}';
    }

}
