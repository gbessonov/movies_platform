package io.github.gbessonov.movies_platform.users.controllers;

import io.github.gbessonov.movies_platform.users.model.User;
import io.github.gbessonov.movies_platform.users.model.UsersResponse;

import java.util.ArrayList;
import java.util.List;

public class UsersListResponse implements UsersResponse {
    private List<User> users;

    public static UsersListResponse fromList(List<User> users) {
        return new UsersListResponse(users);
    }

    protected UsersListResponse(List<User> users) {
        this.users = users != null ? users : new ArrayList<>();
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int getSize() {
        return users.size();
    }
}
