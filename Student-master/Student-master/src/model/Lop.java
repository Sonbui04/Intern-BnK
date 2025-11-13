// File: src/model/Lop.java

package model;

public class Lop {
    private Integer id; // Thêm trường ID kiểu Integer
    private String maLop;
    private String tenLop;
    private Integer namHoc;

    public Lop() {
    }

    // ĐẢM BẢO CONSTRUCTOR NÀY TỒN TẠI VÀ CHÍNH XÁC
    public Lop(Integer id, String maLop, String tenLop, Integer namHoc) {
        this.id = id;
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.namHoc = namHoc;
    }

    // Các constructor khác (đã thêm trước đó)
    public Lop(String maLop, String tenLop, Integer namHoc) {
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.namHoc = namHoc;
        this.id = null;
    }

    public Lop(String maLop, String tenLop) {
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.id = null;
        this.namHoc = null;
    }

    // --- Getters and Setters ---
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public String getTenLop() {
        return tenLop;
    }

    public void setTenLop(String tenLop) {
        this.tenLop = tenLop;
    }

    public Integer getNamHoc() {
        return namHoc;
    }

    public void setNamHoc(Integer namHoc) {
        this.namHoc = namHoc;
    }

    @Override
    public String toString() {
        return tenLop + " (" + maLop + ")";
    }
}