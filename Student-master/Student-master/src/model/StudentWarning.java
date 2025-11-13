package model;

import java.time.LocalDate; // Sử dụng LocalDate cho ngày tháng
import java.sql.Date;       // Dùng cho việc chuyển đổi sang java.sql.Date

public class StudentWarning {
    // Không có ID tự tăng cho bảng này vì PK là (sinhvien_id, canhcao_id, ngay_canh_cao)
    private String sinhVienId; // Khóa ngoại đến SinhVien
    private Integer canhCaoId; // Khóa ngoại đến CanhCao
    private LocalDate ngayCanhCao; // Ngày cảnh cáo
    private String hocKy;        // Học kỳ
    private String lyDo;         // Lý do cụ thể

    // Các đối tượng để tiện hiển thị và thao tác
    private Student student; // Đối tượng sinh viên
    private CanhCao canhCao; // Đối tượng loại cảnh cáo

    public StudentWarning() {
    }

    // Constructor đầy đủ
    public StudentWarning(String sinhVienId, Integer canhCaoId, LocalDate ngayCanhCao, String hocKy, String lyDo) {
        this.sinhVienId = sinhVienId;
        this.canhCaoId = canhCaoId;
        this.ngayCanhCao = ngayCanhCao;
        this.hocKy = hocKy;
        this.lyDo = lyDo;
    }

    // Constructor đầy đủ với đối tượng liên kết
    public StudentWarning(String sinhVienId, Integer canhCaoId, LocalDate ngayCanhCao, String hocKy, String lyDo, Student student, CanhCao canhCao) {
        this.sinhVienId = sinhVienId;
        this.canhCaoId = canhCaoId;
        this.ngayCanhCao = ngayCanhCao;
        this.hocKy = hocKy;
        this.lyDo = lyDo;
        this.student = student;
        this.canhCao = canhCao;
    }

    // --- Getters and Setters ---
    public String getSinhVienId() { return sinhVienId; }
    public void setSinhVienId(String sinhVienId) { this.sinhVienId = sinhVienId; }

    public Integer getCanhCaoId() { return canhCaoId; }
    public void setCanhCaoId(Integer canhCaoId) { this.canhCaoId = canhCaoId; }

    public LocalDate getNgayCanhCao() { return ngayCanhCao; }
    public void setNgayCanhCao(LocalDate ngayCanhCao) { this.ngayCanhCao = ngayCanhCao; }

    public String getHocKy() { return hocKy; }
    public void setHocKy(String hocKy) { this.hocKy = hocKy; }

    public String getLyDo() { return lyDo; }
    public void setLyDo(String lyDo) { this.lyDo = lyDo; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public CanhCao getCanhCao() { return canhCao; }
    public void setCanhCao(CanhCao canhCao) { this.canhCao = canhCao; }
}