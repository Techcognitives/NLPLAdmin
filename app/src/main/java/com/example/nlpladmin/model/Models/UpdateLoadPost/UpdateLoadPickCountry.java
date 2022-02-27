package com.example.nlpladmin.model.Models.UpdateLoadPost;

public class UpdateLoadPickCountry {

    private String pick_country;

    public UpdateLoadPickCountry(String pick_country) {
        this.pick_country = pick_country;
    }

    @Override
    public String toString() {
        return "UpdateLoadBodyPickCountry{" +
                "pick_country='" + pick_country + '\'' +
                '}';
    }
}
