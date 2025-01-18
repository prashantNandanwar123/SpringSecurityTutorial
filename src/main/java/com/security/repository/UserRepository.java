package com.security.repository;

import java.util.Optional;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.security.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
//	Optional<User> findByEmail(String email);
//
//	Optional<User> findByUsernameOrEmail(String username, String email);
//
//	Optional<User> findByUsername(String username);

	
	@Transactional
	@Query(value="select email from [ShoppingCart].[dbo].[users] where email=?1 ",nativeQuery = true)
	public String getEmail(String email);

	@Transactional
	@Query(value="select username from [ShoppingCart].[dbo].[users] where username=?1 ",nativeQuery = true)
	public String getUsername(String username);

	
	@Transactional
	@Query(value = "SELECT * FROM [ShoppingCart].[dbo].[users] WHERE username = ?1 OR email = ?2", nativeQuery = true)
	User findByUsernameOrEmail(String username, String usernameOrEmail);

}


