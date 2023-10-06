package com.root32.configsvc.manager;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.root32.configsvc.repository.CompanyConfigRepository;
import com.root32.entity.CompanyParam;
import com.root32.entity.Org;

@Component
public class CompanyConfig {

	private CompanyConfigRepository companyConfigRepository;

	public CompanyConfig(CompanyConfigRepository companyConfigRepository) {
		this.companyConfigRepository = companyConfigRepository;
	}

	public int getPasswordExpiryDays() {
		return companyConfigRepository.PASSWORD_EXPIRY_DAYS;
	}

	public int getMaxUnsuccessfulAttemps() {
		return companyConfigRepository.MAX_UNSUCCESSFUL_ATTEMPTS;
	}

	public boolean shouldLockAfterMaxUnsuccessfulAttemps() {
		return companyConfigRepository.LOCK_AFTER_MAX_UNSUCCESSFUL_ATTEMPTS;
	}

	public int getOTPExpiryMinutes() {
		return companyConfigRepository.OTP_EXPIRY_MINUTES;
	}

	private CompanyParam fetchCompanyParameter(Org org) {
		CompanyParam companyParam = companyConfigRepository.findByOrg(org);
		if (companyParam == null) {
			companyParam = new CompanyParam();
			companyParam.setOrg(org);
			companyParam.setCreatedDate(new Date());
		}
		return companyParam;
	}
	

	public String generateSaleReferenceNumber(Org org) {
		CompanyParam companyParam = fetchCompanyParam(org);
		Long nextSaleNumber = companyParam.getNextSaleNumber();
		if (nextSaleNumber == null) {
			nextSaleNumber = 0l;
		}
		nextSaleNumber = nextSaleNumber + 1;
		companyParam.setNextSaleNumber(nextSaleNumber);
		companyConfigRepository.save(companyParam);

		return String.valueOf(nextSaleNumber);
	}
	private CompanyParam fetchCompanyParam(Org org) {
		CompanyParam companyParam = companyConfigRepository.findByOrg(org);
		if (companyParam == null) {
			companyParam = new CompanyParam();
			companyParam.setOrg(org);
			companyParam.setCreatedDate(new Date());
		}
		return companyParam;
	}
	
	
	
	
	

}