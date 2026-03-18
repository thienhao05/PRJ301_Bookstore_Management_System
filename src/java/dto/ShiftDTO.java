package dto;

import java.sql.Date;
import java.sql.Time;

public class ShiftDTO {
    private int id;
    private int staffId;
    private Date shiftDate;
    private Time startTime;
    private Time endTime;
    private String name;        // Để hiển thị tên nhân viên hoặc tên ca
    private String description; // Để hiển thị mô tả ca

    public ShiftDTO() {}
    // Trong ShiftDTO.java
public ShiftDTO(int id, int staffId, String startTime, String endTime, Date shiftDate) {
    this.id = id;
    this.staffId = staffId;
    // Chuyển String sang java.sql.Time
    this.startTime = java.sql.Time.valueOf(startTime + ":00"); 
    this.endTime = java.sql.Time.valueOf(endTime + ":00");
    this.shiftDate = shiftDate;
}

    // Getter & Setter chuẩn để JSP ${s.id}, ${s.name}, ${s.startTime} hoạt động
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getStaffId() { return staffId; }
    public void setStaffId(int staffId) { this.staffId = staffId; }

    public Date getShiftDate() { return shiftDate; }
    public void setShiftDate(Date shiftDate) { this.shiftDate = shiftDate; }

    public Time getStartTime() { return startTime; }
    public void setStartTime(Time startTime) { this.startTime = startTime; }

    public Time getEndTime() { return endTime; }
    public void setEndTime(Time endTime) { this.endTime = endTime; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}