package tech.arc9.gateway.model;

import tech.arc9.user.UserProto;

public class User {
    private String name;
    private String email;
    private String id;

    public User(String name, String email, String id) {
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public User(UserProto.User proto) {
        this.email = proto.getEmail();
        this.id = proto.getId();
        this.name = proto.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
