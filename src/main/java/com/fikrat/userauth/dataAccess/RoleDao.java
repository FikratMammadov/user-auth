package com.fikrat.userauth.dataAccess;

import com.fikrat.userauth.entities.concretes.Role;
import com.fikrat.userauth.entities.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleDao extends JpaRepository<Role,Integer> {
    Optional<Role> findByName(ERole name);
}
