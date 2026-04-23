package com.neuro.session;

public class UserSession {

    private static int userId;
    private static String username;

    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int id) {
        userId = id;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String name) {
        username = name;
    }

    public static void clear() {
        userId = 0;
        username = null;
    }
}