package dto;

public class AddressDTO {
    private int address_id;
    private int user_id;
    private String full_address;
    private String city;
    private String district;
    private boolean is_default;

    // Constructor rỗng
    public AddressDTO() {}

    // ✅ Constructor đầy đủ - AddressController dùng khi update
    public AddressDTO(int address_id, int user_id, String full_address,
                      String city, String district, boolean is_default) {
        this.address_id  = address_id;
        this.user_id     = user_id;
        this.full_address = full_address;
        this.city        = city;
        this.district    = district;
        this.is_default  = is_default;
    }

    public int getAddress_id() { return address_id; }
    public void setAddress_id(int address_id) { this.address_id = address_id; }

    public int getUser_id() { return user_id; }
    public void setUser_id(int user_id) { this.user_id = user_id; }

    public String getFull_address() { return full_address; }
    public void setFull_address(String full_address) { this.full_address = full_address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public boolean isIs_default() { return is_default; }
    public void setIs_default(boolean is_default) { this.is_default = is_default; }
}