/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author PC
 */
public class OrderDetailDTO {
    private int order_detail_id;
    private int order_id;
    private int book_id;
    private int quantity;
    private int price;

    public OrderDetailDTO() {
    }

    public OrderDetailDTO(int order_detail_id, int order_id, int book_id, int quantity, int price) {
        this.order_detail_id = order_detail_id;
        this.order_id = order_id;
        this.book_id = book_id;
        this.quantity = quantity;
        this.price = price;
    }

    public int getOrder_detail_id() {
        return order_detail_id;
    }

    public void setOrder_detail_id(int order_detail_id) {
        this.order_detail_id = order_detail_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    
    
}
