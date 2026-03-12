/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.sql.Timestamp;

public class PaymentDTO {

    private int payment_id;
    private int order_id;
    private String method;
    private double amount;
    private Timestamp payment_date;
    private String status;

    public PaymentDTO() {
    }

    public PaymentDTO(int payment_id, int order_id, String method, double amount, Timestamp payment_date, String status) {
        this.payment_id = payment_id;
        this.order_id = order_id;
        this.method = method;
        this.amount = amount;
        this.payment_date = payment_date;
        this.status = status;
    }
    
    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(Timestamp payment_date) {
        this.payment_date = payment_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
