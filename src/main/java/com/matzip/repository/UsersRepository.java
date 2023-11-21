package com.matzip.repository;

import com.matzip.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, String> {
    Users findByUserid(String userid);
    Page<Users> findByUseridContainingOrUsernameContainingOrUserphoneContaining(String userid, String username, String userphone, Pageable pageable);


}
