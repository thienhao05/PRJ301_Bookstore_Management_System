/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.sql.Date;
import java.sql.Time;

public class ShiftDTO {

    private int shift_id;
    private int staff_id;
    private Date shift_date;
    private Time start_time;
    private Time end_time;

    public ShiftDTO() {
    }

    public ShiftDTO(int shift_id, int staff_id, Date shift_date, Time start_time, Time end_time) {
        this.shift_id = shift_id;
        this.staff_id = staff_id;
        this.shift_date = shift_date;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public int getShift_id() {
        return shift_id;
    }

    public void setShift_id(int shift_id) {
        this.shift_id = shift_id;
    }

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    public Date getShift_date() {
        return shift_date;
    }

    public void setShift_date(Date shift_date) {
        this.shift_date = shift_date;
    }

    public Time getStart_time() {
        return start_time;
    }

    public void setStart_time(Time start_time) {
        this.start_time = start_time;
    }

    public Time getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Time end_time) {
        this.end_time = end_time;
    }
    
}
