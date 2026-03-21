package utils;

import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.activation.DataHandler;
import javax.activation.DataSource;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author PC
 */
public class Constants {
    
    public static void main(String[] args) {
        final String from = "";// Tên email 
        final String password = ""; // pass của email VD:fhnninbdjeebfjenjff //mail thiệt
        
//        Properties : khai báo các thuộc 
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP HOST
        props.put("mail.smtp.port", "587"); // TLS 587 SSl 465
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        
//        create Authenticator
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        };
        
        // Phiên làm việc
        Session session = Session.getInstance(props, auth);
        
        // Gửi mail 
        final String to = ""; //gmail thiệt của mình
        
        // Tạo một tin nhắn
        MimeMessage msg = new MimeMessage(session);
        
        try {
            // Kiểu nội dung
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            // Người gửi
            msg.setFrom(new InternetAddress(from));
            // Người nhận
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            // Tiêu đề email
            msg.setSubject("Thử Nghiệm gửi email");
            // Quy định ngày gửi
            msg.setSentDate(new Date());
            // Quy định email nhận phản hồi
            msg.setReplyTo(InternetAddress.parse(from, false));
            
            // Nội dung
            msg.setText("Đây là nội dung", "UTF-8");
            
            // Gửi email
            Transport.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}