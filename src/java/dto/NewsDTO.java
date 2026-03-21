/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.sql.Timestamp;

public class NewsDTO {

    private int news_id;
    private String title;
    private String content;
    private Timestamp created_at;
    private int staff_id;

    public NewsDTO() {
    }

    public NewsDTO(int news_id, String title, String content, Timestamp created_at, int staff_id) {
        this.news_id = news_id;
        this.title = title;
        this.content = content;
        this.created_at = created_at;
        this.staff_id = staff_id;
    }

    public int getNews_id() {
        return news_id;
    }

    public void setNews_id(int news_id) {
        this.news_id = news_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }
    
}
