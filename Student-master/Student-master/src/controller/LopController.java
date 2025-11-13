package controller;

import model.Lop;
import service.LopService;
import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LopController {
    private LopService service;

    public LopController() {
        try {
            service = new LopService();
        } catch (RuntimeException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Không thể kết nối đến CSDL cho chức năng Lớp: " + e.getMessage(), "Lỗi Khởi Tạo", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("Lỗi khởi tạo LopController", e);
        }
    }

    public List<Lop> getAllLops() {
        try {
            return service.getAllLops();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải danh sách lớp: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    public boolean addLop(Lop lop) {
        try {
            // Kiểm tra null cho NamHoc trước khi gọi service
            if (lop.getNamHoc() != null && lop.getNamHoc() < 0) {
                JOptionPane.showMessageDialog(null, "Năm học không hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            boolean result = service.addLop(lop);
            if (!result) {
                 JOptionPane.showMessageDialog(null, "Thêm lớp thất bại! (Có thể mã lớp đã tồn tại hoặc lỗi khác)", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            String errorMessage = "Lỗi khi thêm lớp: " + e.getMessage();
            if (e.getSQLState() != null && e.getSQLState().startsWith("23")) {
                errorMessage = "Mã lớp '" + lop.getMaLop() + "' đã tồn tại. Vui lòng nhập mã khác.";
            }
            JOptionPane.showMessageDialog(null, errorMessage, "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean updateLop(Lop lop) {
        try {
            boolean result = service.updateLop(lop);
            if (!result) {
                JOptionPane.showMessageDialog(null, "Cập nhật lớp thất bại! (Không tìm thấy lớp hoặc lỗi khác)", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            String errorMessage = "Lỗi khi cập nhật lớp: " + e.getMessage();
            if (e.getSQLState() != null && e.getSQLState().startsWith("23")) {
                errorMessage = "Mã lớp '" + lop.getMaLop() + "' đã tồn tại. Vui lòng nhập mã khác.";
            }
            JOptionPane.showMessageDialog(null, errorMessage, "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // SỬA LỖI: Thay đổi đối số thành String maLop
    public boolean deleteLop(String maLop) {
        int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa lớp này không? (Mã: " + maLop + ")", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean result = service.deleteLop(maLop);
                if (result) {
                    JOptionPane.showMessageDialog(null, "Xóa lớp thành công!");
                } else {
                    JOptionPane.showMessageDialog(null, "Xóa lớp thất bại! (Lớp không tồn tại hoặc có ràng buộc dữ liệu)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
                return result;
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi khi xóa lớp: " + e.getMessage() + "\n(Có thể lớp này đang được tham chiếu bởi sinh viên khác)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }

    public List<Lop> searchLops(String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return service.getAllLops();
            }
            return service.searchLops(keyword);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tìm kiếm lớp: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }
}