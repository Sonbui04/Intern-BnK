package controller;

import model.CanhCao;
import service.CanhCaoService;
import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CanhCaoController {
    private CanhCaoService service;

    public CanhCaoController() {
        try {
            service = new CanhCaoService();
        } catch (RuntimeException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Không thể kết nối đến CSDL cho chức năng Cảnh cáo: " + e.getMessage(), "Lỗi Khởi Tạo", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("Lỗi khởi tạo CanhCaoController", e);
        }
    }

    public List<CanhCao> getAllCanhCaos() {
        try {
            return service.getAllCanhCaos();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải danh sách cảnh cáo: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    public boolean addCanhCao(CanhCao canhCao) {
        try {
            boolean result = service.addCanhCao(canhCao);
            if (!result) {
                JOptionPane.showMessageDialog(null, "Thêm loại cảnh cáo thất bại! (Có thể tên cảnh cáo đã tồn tại hoặc lỗi khác)", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            String errorMessage = "Lỗi khi thêm loại cảnh cáo: " + e.getMessage();
            if (e.getSQLState() != null && e.getSQLState().startsWith("23")) { // SQLState 23xxx cho lỗi ràng buộc toàn vẹn (ví dụ: trùng khóa)
                errorMessage = "Tên loại cảnh cáo '" + canhCao.getTenCanhCao() + "' đã tồn tại. Vui lòng nhập tên khác.";
            }
            JOptionPane.showMessageDialog(null, errorMessage, "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean updateCanhCao(CanhCao canhCao) {
        try {
            boolean result = service.updateCanhCao(canhCao);
            if (!result) {
                JOptionPane.showMessageDialog(null, "Cập nhật loại cảnh cáo thất bại! (Không tìm thấy hoặc lỗi khác)", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            String errorMessage = "Lỗi khi cập nhật loại cảnh cáo: " + e.getMessage();
            if (e.getSQLState() != null && e.getSQLState().startsWith("23")) {
                errorMessage = "Tên loại cảnh cáo '" + canhCao.getTenCanhCao() + "' đã tồn tại. Vui lòng nhập tên khác.";
            }
            JOptionPane.showMessageDialog(null, errorMessage, "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean deleteCanhCao(int id) {
        int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa loại cảnh cáo này không? (ID: " + id + ")", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean result = service.deleteCanhCao(id);
                if (result) {
                    JOptionPane.showMessageDialog(null, "Xóa loại cảnh cáo thành công!");
                } else {
                    JOptionPane.showMessageDialog(null, "Xóa loại cảnh cáo thất bại! (Không tồn tại hoặc có ràng buộc dữ liệu)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
                return result;
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi khi xóa loại cảnh cáo: " + e.getMessage() + "\n(Có thể loại cảnh cáo này đang được sử dụng bởi các sinh viên)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }

    public List<CanhCao> searchCanhCaos(String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return service.getAllCanhCaos();
            }
            return service.searchCanhCaos(keyword);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tìm kiếm loại cảnh cáo: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }
}