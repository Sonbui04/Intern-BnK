package model;

public class User {
    private Integer id;
    private String username;
    private String passwordHash; // Sẽ lưu trữ mật khẩu đã hash
    private String fullName;
    private String email;
    private Integer roleId; // Khóa ngoại tới Role
    private Role role;       // Đối tượng Role để tiện truy cập thông tin vai trò

    public User() {
    }

    // Constructor đầy đủ
    public User(Integer id, String username, String passwordHash, String fullName, String email, Integer roleId, Role role) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.email = email;
        this.roleId = roleId;
        this.role = role;
    }

    // Constructor cơ bản (khi tạo user mới, hash sẽ được tạo sau)
    public User(String username, String passwordHash, String fullName, String email, Integer roleId) {
        this(null, username, passwordHash, fullName, email, roleId, null);
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}