package model;

public class Student {
    private String id;
    private String name; // Đây là tên đầy đủ, không phải fullName
    private String gender;
    private String dob; // Kiểu String cho ngày sinh
    private String address;
    private String phone;
    private String email;
    private Integer lopId;
    private Lop lop;

    public Student() {
    }

    public Student(String id, String name, String gender, String dob, String address, String phone, String email, Integer lopId, Lop lop) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.dob = dob;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.lopId = lopId;
        this.lop = lop;
    }

    public Student(String id, String name, String gender, String dob, String address, String phone, String email, Integer lopId) {
        this(id, name, gender, dob, address, phone, email, lopId, null);
    }

    // Constructor cho trường hợp không có lopId ngay lập tức (ví dụ: từ form)
    public Student(String id, String name, String gender, String dob, String address, String phone, String email) {
        this(id, name, gender, dob, address, phone, email, null, null);
    }


    // --- Getters and Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; } // Phương thức này là getFullName() mà bạn đang tìm
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getDob() { return dob; } // Trả về String
    public void setDob(String dob) { this.dob = dob; } // Nhận String

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getLopId() { return lopId; }
    public void setLopId(Integer lopId) { this.lopId = lopId; }

    public Lop getLop() { return lop; }
    public void setLop(Lop lop) { this.lop = lop; }

 
    public String toString() {
        return id + " - " + name;
    }
}