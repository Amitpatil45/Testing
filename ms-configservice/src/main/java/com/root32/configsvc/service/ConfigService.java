package com.root32.configsvc.service;

import com.root32.configsvc.manager.CompanyConfig;
import com.root32.configsvc.manager.PlatformParamConfig;

public interface ConfigService {

	public CompanyConfig getCompanyConfig();
	public PlatformParamConfig getPlatformConfig();

}
