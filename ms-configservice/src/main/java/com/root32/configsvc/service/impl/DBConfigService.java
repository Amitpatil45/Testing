package com.root32.configsvc.service.impl;

import org.springframework.stereotype.Service;

import com.root32.configsvc.manager.CompanyConfig;
import com.root32.configsvc.manager.PlatformParamConfig;
import com.root32.configsvc.service.ConfigService;

@Service
public class DBConfigService implements ConfigService {

	private CompanyConfig companyConfig;
	private PlatformParamConfig platformConfig;

	public DBConfigService(CompanyConfig companyConfig, PlatformParamConfig platformConfig) {
		this.companyConfig = companyConfig;
		this.platformConfig = platformConfig;
	}

	public CompanyConfig getCompanyConfig() {
		return companyConfig;
	}

	@Override
	public PlatformParamConfig getPlatformConfig() {
		return platformConfig;
	}


	
}
