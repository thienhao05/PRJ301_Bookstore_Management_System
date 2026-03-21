/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author PC
 */


import java.sql.Timestamp;

public class OrderDTO {
    private int order_id;
    private int user_id;
    private int address_id;
    private int discount_id;
    private int shipping_provider_id;
    private int total_amount;
    private Timestamp order_date;
    private String status;

    public OrderDTO() {
    }

    public OrderDTO(int order_id, int user_id, int address_id, int discount_id, int shipping_provider_id, int total_amount, Timestamp order_date, String status) {
        this.order_id = order_id;
        this.user_id = user_id;
        this.address_id = address_id;
        this.discount_id = discount_id;
        this.shipping_provider_id = shipping_provider_id;
        this.total_amount = total_amount;
        this.order_date = order_date;
        this.status = status;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public int getDiscount_id() {
        return discount_id;
    }

    public void setDiscount_id(int discount_id) {
        this.discount_id = discount_id;
    }

    public int getShipping_provider_id() {
        return shipping_provider_id;
    }

    public void setShipping_provider_id(int shipping_provider_id) {
        this.shipping_provider_id = shipping_provider_id;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public Timestamp getOrder_date() {
        return order_date;
    }

    public void setOrder_date(Timestamp order_date) {
        this.order_date = order_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
