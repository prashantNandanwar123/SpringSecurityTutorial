package com.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.security.entity.Role;
import com.security.entity.User;

import java.util.Optional;

import javax.transaction.Transactional;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(String name);
	
	@Transactional
	@Query(value = "SELECT * FROM [ShoppingCart].[dbo].[role] WHERE username = ?1", nativeQuery = true)
	Optional<Role> findByUsernameOrEmail(String username, String email);
}