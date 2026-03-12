/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.sql.Timestamp;

public class WishlistDTO {

    private int wishlist_id;
    private int user_id;
    private int book_id;
    private Timestamp created_at;

    public WishlistDTO() {
    }

    public WishlistDTO(int wishlist_id, int user_id, int book_id, Timestamp created_at) {
        this.wishlist_id = wishlist_id;
        this.user_id = user_id;
        this.book_id = book_id;
        this.created_at = created_at;
    }

    public int getWishlist_id() {
        return wishlist_id;
    }

    public void setWishlist_id(int wishlist_id) {
        this.wishlist_id = wishlist_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
    
}