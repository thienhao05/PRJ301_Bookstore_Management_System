/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author PC
 */


import java.sql.Date;

import java.sql.Date;

public class StaffDTO {

    private int staff_id;
    private int user_id;
    private Date hire_date;
    private double salary;
    private String status;

    public StaffDTO() {
    }

    public StaffDTO(int staff_id, int user_id, Date hire_date, double salary, String status) {
        this.staff_id = staff_id;
        this.user_id = user_id;
        this.hire_date = hire_date;
        this.salary = salary;
        this.status = status;
    }

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Date getHire_date() {
        return hire_date;
    }

    public void setHire_date(Date hire_date) {
        this.hire_date = hire_date;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    

}