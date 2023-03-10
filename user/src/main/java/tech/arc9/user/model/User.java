package tech.arc9.user.model;

import tech.arc9.user.UserProto;
import tech.arc9.user.db.mysql.entity.UserEntity;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String name;
    private String email;

    public User(UserProto.User proto) {
        setId(proto.getId());
        setName(proto.getName());
        setEmail(proto.getEmail());
    }

    public User(UserEntity entity) {
        setId(entity.id);
        setName(entity.name);
        setEmail(entity.email);
    }

    public UserProto.User toProto() {
        return UserProto.User.newBuilder().setEmail(email == null ? "" :email)
                .setId(id == null ? "" : id)
                .setName(name == null ? "" : name).build();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
