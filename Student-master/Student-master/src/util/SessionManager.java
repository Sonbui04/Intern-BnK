package util;

import model.User;

public class SessionManager {
    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static boolean isAdmin() {
        return currentUser != null && "ADMIN".equals(currentUser.getRole().getRoleName());
    }

    // Chỉ giữ lại isSv() và bỏ isGiaoVien()
    public static boolean isSv() {
        return currentUser != null && "SV".equals(currentUser.getRole().getRoleName());
    }

    public static void logout() {
        currentUser = null;
    }
}