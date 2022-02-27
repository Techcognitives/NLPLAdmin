package com.example.nlpladmin.model.Models.UpdateTruckDetails;

public class UpdateTruckVehicleInsurance {

    private String vehicle_insurance;

    public UpdateTruckVehicleInsurance(String vehicle_insurance) {
        this.vehicle_insurance = vehicle_insurance;
    }

    @Override
    public String toString() {
        return "UpdateTruckVehicleInsurance{" +
                "vehicle_insurance='" + vehicle_insurance + '\'' +
                '}';
    }

    public String getVehicle_insurance() {
        return vehicle_insurance;
    }

    public void setVehicle_insurance(String vehicle_insurance) {
        this.vehicle_insurance = vehicle_insurance;
    }
}
