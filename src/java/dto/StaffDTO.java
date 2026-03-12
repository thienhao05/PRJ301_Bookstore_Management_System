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

}