/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

public class ShippingProviderDTO {

    private int shipping_provider_id;
    private String name;
    private String phone;
    private double fee;

    public ShippingProviderDTO() {
    }
    
    public ShippingProviderDTO(int shipping_provider_id, String name, String phone, double fee) {
        this.shipping_provider_id = shipping_provider_id;
        this.name = name;
        this.phone = phone;
        this.fee = fee;
    }

    public int getShipping_provider_id() {
        return shipping_provider_id;
    }

    public void setShipping_provider_id(int shipping_provider_id) {
        this.shipping_provider_id = shipping_provider_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }
    
}
