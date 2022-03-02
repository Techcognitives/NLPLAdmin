package com.example.nlpladmin.model.Models.UpdateUserDetails;

public class UpdateIsUserVerified {
    String is_user_verfied;

    public UpdateIsUserVerified(String is_user_verfied) {
        this.is_user_verfied = is_user_verfied;
    }

    @Override
    public String toString() {
        return "UpdateIsUserVerified{" +
                "is_user_verfied='" + is_user_verfied + '\'' +
                '}';
    }
}
