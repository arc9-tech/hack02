package tech.arc9.user.db.mysql.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import tech.arc9.user.db.mysql.entity.UserEntity;

import java.util.List;

public interface UserRepository extends CrudRepository<UserEntity, String> {


    List<UserEntity> findAll(Pageable pageable);
}
