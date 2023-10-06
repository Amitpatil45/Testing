package com.root32.configsvc.manager;

import org.springframework.stereotype.Component;

import com.root32.configsvc.repository.PlatformParamRepository;

@Component
public class PlatformParamConfig {
	private PlatformParamRepository platformParamRepository;


	public PlatformParamConfig(PlatformParamRepository platformParamRepository) {
		this.platformParamRepository = platformParamRepository;
	}
}
