package service;

import dao.LopDAO;
import model.Lop;
import java.sql.SQLException;
import java.util.List;

public class LopService {
    private LopDAO lopDAO;

    public LopService() {
        this.lopDAO = new LopDAO();
    }

    public boolean addLop(Lop lop) throws SQLException {
        return lopDAO.addLop(lop);
    }

    public boolean updateLop(Lop lop) throws SQLException {
        return lopDAO.updateLop(lop);
    }

    // SỬA LỖI: Thay đổi đối số thành String maLop
    public boolean deleteLop(String maLop) throws SQLException {
        return lopDAO.deleteLop(maLop);
    }

    public List<Lop> getAllLops() throws SQLException {
        return lopDAO.getAllLops();
    }

    public List<Lop> searchLops(String keyword) throws SQLException {
        return lopDAO.searchLops(keyword);
    }

    public Lop getLopById(String maLop) throws SQLException {
        return lopDAO.getLopById(maLop);
    }
}