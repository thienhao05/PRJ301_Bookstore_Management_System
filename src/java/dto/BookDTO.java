package dto;

import java.sql.Timestamp; // Dùng để hứng cột DATETIME

public class BookDTO {
    
    private int bookId;
    private String title;
    private String author;
    private double price;    // DECIMAL trong SQL thường map với double hoặc BigDecimal trong Java
    private int stock;
    private int categoryId;
    private int publisherId;
    private Timestamp createdAt;

    // Constructor rỗng
    public BookDTO() {
    }

    // Constructor full tham số
    public BookDTO(int bookId, String title, String author, double price, int stock, int categoryId, int publisherId, Timestamp createdAt) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.price = price;
        this.stock = stock;
        this.categoryId = categoryId;
        this.publisherId = publisherId;
        this.createdAt = createdAt;
    }

    // Getter và Setter
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public int getPublisherId() { return publisherId; }
    public void setPublisherId(int publisherId) { this.publisherId = publisherId; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}