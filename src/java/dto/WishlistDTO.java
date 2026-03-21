package dto;

import java.sql.Timestamp;

public class WishlistDTO {
    private int wishlist_id;
    private int user_id;
    private int book_id;
    private Timestamp created_at;

    // ✅ THÊM: Các field từ bảng Books (dùng khi JOIN để hiển thị trên JSP)
    private String title;
    private String author;
    private double price;
    private int stock;
    private int bookId; // alias cho book_id để JSP dùng ${item.bookId}

    public WishlistDTO() {}

    public WishlistDTO(int wishlist_id, int user_id, int book_id, Timestamp created_at) {
        this.wishlist_id = wishlist_id;
        this.user_id     = user_id;
        this.book_id     = book_id;
        this.created_at  = created_at;
    }

    // Getters & Setters gốc
    public int getWishlist_id() { return wishlist_id; }
    public void setWishlist_id(int wishlist_id) { this.wishlist_id = wishlist_id; }

    public int getUser_id() { return user_id; }
    public void setUser_id(int user_id) { this.user_id = user_id; }

    public int getBook_id() { return book_id; }
    public void setBook_id(int book_id) {
        this.book_id = book_id;
        this.bookId  = book_id; // ✅ Sync cả 2
    }

    public Timestamp getCreated_at() { return created_at; }
    public void setCreated_at(Timestamp created_at) { this.created_at = created_at; }

    // ✅ Getters & Setters cho Book fields
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) {
        this.bookId  = bookId;
        this.book_id = bookId; // ✅ Sync cả 2
    }
}