package com.example.test.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.test.domain.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "update usr u set u.status = ? where u.username = ?", nativeQuery = true)
    int updateUsrSetStatusForUsername(Boolean status, String username);

    @Transactional
    int deleteByUsername(String username);
}
