package com.root32.dentalproductservice.util;

import org.springframework.stereotype.Component;

import com.root32.configsvc.service.ConfigService;
import com.root32.entity.Org;

@Component
public class EntityNumberUtil {

	private final ConfigService configService;

	public EntityNumberUtil(ConfigService configService) {
		this.configService = configService;
	}

	public String generateSaleReferenceNumber(Org userOrg) {
		String prefix = "SALE";
		String uniqueID = configService.getCompanyConfig().generateSaleReferenceNumber(userOrg);
		String number = prefix + " - " + uniqueID;
		return number;
	}
}
