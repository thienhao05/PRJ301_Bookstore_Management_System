/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.sql.Timestamp;

public class NotificationDTO {

    private int notification_id;
    private int user_id;
    private String content;
    private Timestamp created_at;
    private boolean is_read;

    public NotificationDTO() {
    }

    public NotificationDTO(int notification_id, int user_id, String content, Timestamp created_at, boolean is_read) {
        this.notification_id = notification_id;
        this.user_id = user_id;
        this.content = content;
        this.created_at = created_at;
        this.is_read = is_read;
    }

    public int getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(int notification_id) {
        this.notification_id = notification_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public boolean isIs_read() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }
    
}
