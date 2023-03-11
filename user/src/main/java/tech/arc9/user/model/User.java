package tech.arc9.user.model;

import tech.arc9.user.UserProto;
import tech.arc9.user.db.mysql.entity.UserEntity;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String bio;

    public User(UserProto.User proto) {
        setId(proto.getId());
        setEmail(proto.getEmail());
        setFirstName(proto.getFirstName());
        setLastName(proto.getLastName());
        setGender(proto.getGender());
        setBio(proto.getBio());
    }

    public User(UserEntity entity) {
        setId(entity.id);
        setEmail(entity.email);
        setFirstName(entity.firstName);
        setLastName(entity.lastName);
        setGender(entity.gender);
        setBio(entity.bio);
    }

    public UserProto.User toProto() {
        return UserProto.User.newBuilder()
                .setId(id == null ? "" : id)
                .setEmail(email == null ? "" :email)
                .setFirstName(firstName == null ? "" : firstName)
                .setLastName(lastName == null ? "" : lastName)
                .setGender(gender == null ? "" : gender)
                .setBio(bio == null ? "" : bio)
                .build();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
