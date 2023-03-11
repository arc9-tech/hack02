package tech.arc9.gateway.model;

import tech.arc9.user.UserProto;

public class User {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String bio;

//    public User(String name, String email, String id) {
//        this.name = name;
//        this.email = email;
//        this.id = id;
//    }

    public User(UserProto.User proto) {
        setId(proto.getId());
        setEmail(proto.getEmail());
        setFirstName(proto.getFirstName());
        setLastName(proto.getLastName());
        setGender(proto.getGender());
        setBio(proto.getBio());
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
