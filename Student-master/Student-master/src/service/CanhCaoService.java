package service;

import dao.CanhCaoDAO;
import model.CanhCao;
import java.sql.SQLException;
import java.util.List;

public class CanhCaoService {
    private CanhCaoDAO canhCaoDAO;

    public CanhCaoService() {
        this.canhCaoDAO = new CanhCaoDAO();
    }

    public boolean addCanhCao(CanhCao canhCao) throws SQLException {
        return canhCaoDAO.addCanhCao(canhCao);
    }

    public boolean updateCanhCao(CanhCao canhCao) throws SQLException {
        return canhCaoDAO.updateCanhCao(canhCao);
    }

    public boolean deleteCanhCao(int id) throws SQLException {
        return canhCaoDAO.deleteCanhCao(id);
    }

    public List<CanhCao> getAllCanhCaos() throws SQLException {
        return canhCaoDAO.getAllCanhCaos();
    }

    public List<CanhCao> searchCanhCaos(String keyword) throws SQLException {
        return canhCaoDAO.searchCanhCaos(keyword);
    }

    public CanhCao getCanhCaoById(int id) throws SQLException {
        return canhCaoDAO.getCanhCaoById(id);
    }
}