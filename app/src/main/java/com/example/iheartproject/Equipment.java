package com.example.iheartproject;

public class Equipment {

    private String equipmentName;
    private String brand;
    private String modelNumber;
    private String quantity;
    private String status;

    public Equipment() {

    }

    public Equipment(String equipmentName, String brand, String modelNumber, String quantity, String status) {
        this.equipmentName = equipmentName;
        this.brand = brand;
        this.modelNumber = modelNumber;
        this.quantity = quantity;
        this.status = status;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String newEquipmentName) {
        this.equipmentName = newEquipmentName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String newBrand) {
        this.brand = newBrand;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String newModelNumber) {
        this.modelNumber = newModelNumber;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String newQuantity) {
        this.quantity = newQuantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String newStatus) {
        this.status = newStatus;
    }
}
