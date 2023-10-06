package com.root32.onboarding.service;

import com.root32.dto.Location;

public interface PincodeService {
	Location fetchAddressByPin(String pin);
}
