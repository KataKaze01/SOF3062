package com.poly.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserSessionService {

    private final Set<String> onlineUsers = new HashSet<>();

    public Set<String> addUser(String username) {
        onlineUsers.add(username);
        return onlineUsers;
    }

    public Set<String> removeUser(String username) {
        onlineUsers.remove(username);
        return onlineUsers;
    }

    public Set<String> getOnlineUsers() {
        return onlineUsers;
    }
}
