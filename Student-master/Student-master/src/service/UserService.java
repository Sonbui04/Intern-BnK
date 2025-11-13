package service;

import dao.UserDAO;
import dao.RoleDAO;
import model.User;
import model.Role;
import util.PasswordHasher;
import java.sql.SQLException;
import java.util.List;

public class UserService {
    private UserDAO userDAO;
    private RoleDAO roleDAO;

    public UserService() {
        this.userDAO = new UserDAO();
        this.roleDAO = new RoleDAO();
    }

    public User authenticate(String username, String plainPassword) throws SQLException {
        User user = userDAO.getUserByUsername(username);
        if (user != null && PasswordHasher.checkPassword(plainPassword, user.getPasswordHash())) {
            return user;
        }
        return null;
    }

    public boolean registerUser(String username, String plainPassword, String fullName, String email, String roleName) throws SQLException {
        String hashedPassword = PasswordHasher.hashPassword(plainPassword);
        Role role = roleDAO.getRoleByName(roleName);
        if (role == null) {
            System.err.println("Lỗi: Vai trò '" + roleName + "' không tồn tại.");
            return false;
        }
        User newUser = new User(username, hashedPassword, fullName, email, role.getId());
        return userDAO.addUser(newUser);
    }

    public boolean updateUser(User user) throws SQLException {
        return userDAO.updateUser(user);
    }

    public boolean deleteUser(int id) throws SQLException {
        return userDAO.deleteUser(id);
    }

    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAllUsers();
    }

    public List<Role> getAllRoles() throws SQLException {
        return roleDAO.getAllRoles();
    }

    // THÊM PHƯƠNG THỨC NÀY
    public User getUserById(int id) throws SQLException {
        return userDAO.getUserById(id);
    }
}