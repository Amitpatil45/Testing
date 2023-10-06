package com.root32.configsvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.root32.entity.CompanyParam;
import com.root32.entity.Org;

//public interface CompanyConfigRepository extends JpaRepository<T, ID> {

//TODO - Define the table structure for all the configurations, so that UI for MetaData can be created.
@Repository
public interface CompanyConfigRepository extends JpaRepository<CompanyParam, Long> {

	public int PASSWORD_EXPIRY_DAYS = 365;
	public final int MAX_UNSUCCESSFUL_ATTEMPTS = 5;
	public final boolean LOCK_AFTER_MAX_UNSUCCESSFUL_ATTEMPTS = true;
	public static final int OTP_EXPIRY_MINUTES = 2;

	public CompanyParam findByOrg(Org org);
}
