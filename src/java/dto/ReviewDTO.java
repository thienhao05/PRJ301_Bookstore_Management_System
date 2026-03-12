/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;


import java.sql.Timestamp;

public class ReviewDTO {

    private int review_id;
    private int user_id;
    private int book_id;
    private int rating;
    private String comment;
    private Timestamp review_date;

    public ReviewDTO() {
    }

    public ReviewDTO(int review_id, int user_id, int book_id, int rating, String comment, Timestamp review_date) {
        this.review_id = review_id;
        this.user_id = user_id;
        this.book_id = book_id;
        this.rating = rating;
        this.comment = comment;
        this.review_date = review_date;
    }

    public int getReview_id() {
        return review_id;
    }

    public void setReview_id(int review_id) {
        this.review_id = review_id;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getReview_date() {
        return review_date;
    }

    public void setReview_date(Timestamp review_date) {
        this.review_date = review_date;
    }
    
}
