package tech.arc9.user.db.mysql.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.Instant;
@Entity
@Table(name = UserEntity.TABLE_NAME)
public class UserEntity implements Serializable {
    public static final String TABLE_NAME = "USER";
    @Id
    public String id;
    public String email;
    public String firstName;
    public String lastName;
    public String gender;
    public String bio;
    public Instant createTime;
    public Instant lastUpdateTime;

}
