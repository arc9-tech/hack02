package tech.arc9.user.db.mysql.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.arc9.user.db.mysql.entity.UserEntity;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    Page<UserEntity> findAll(Pageable pageable);
}
