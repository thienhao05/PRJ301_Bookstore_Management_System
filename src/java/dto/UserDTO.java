package dto;

public class UserDTO {
    private int    userId;
    private String username;      // map từ cột full_name
    private String password;
    private String email;
    private String phone;         // thêm mới
    private int    roleId;
    private boolean status;

    public UserDTO() {}

    public UserDTO(int userId, String username, String password,
                   String email, String phone, int roleId, boolean status) {
        this.userId   = userId;
        this.username = username;
        this.password = password;
        this.email    = email;
        this.phone    = phone;
        this.roleId   = roleId;
        this.status   = status;
    }

    // Alias để JSP dùng .fullName không bị null
    public String getFullName() { return username; }

    public int     getUserId()              { return userId; }
    public void    setUserId(int userId)    { this.userId = userId; }

    public String  getUsername()            { return username; }
    public void    setUsername(String u)    { this.username = u; }

    public String  getPassword()            { return password; }
    public void    setPassword(String p)    { this.password = p; }

    public String  getEmail()               { return email; }
    public void    setEmail(String e)       { this.email = e; }

    public String  getPhone()               { return phone; }
    public void    setPhone(String phone)   { this.phone = phone; }

    public int     getRoleId()              { return roleId; }
    public void    setRoleId(int roleId)    { this.roleId = roleId; }

    public boolean isStatus()               { return status; }
    public void    setStatus(boolean s)     { this.status = s; }
}