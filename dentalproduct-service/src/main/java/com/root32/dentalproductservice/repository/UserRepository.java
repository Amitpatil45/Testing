package com.root32.dentalproductservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.root32.dto.UserDto;
import com.root32.entity.Org;
import com.root32.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmailId(String emailId);

	Long countByOrg(Org org);

	User findByContactNumber(String contactNumber);

	Long countByContactNumber(String contactNumber);

	Page<UserDto> findAllByOrg(Org userOrg, Pageable pageable);

	Long countByEmailId(String emailId);

	public Page<UserDto> findBy(Pageable pageable);

	User findByEmailIdAndOrg(String emailId, Org org);

	User findByOrgId(Org org);

}
