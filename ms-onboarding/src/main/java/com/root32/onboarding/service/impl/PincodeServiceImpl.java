package com.root32.onboarding.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.root32.dto.Location;
import com.root32.dto.PostOffice;
import com.root32.entity.Pincode;
import com.root32.onboarding.config.PincodeRestTemplate;
import com.root32.onboarding.repository.PincodeRepository;
import com.root32.onboarding.service.PincodeService;
@Service
public class PincodeServiceImpl implements PincodeService {
	@Autowired
	private PincodeRestTemplate pincodeRestTemplate;

	@Autowired
	private PincodeRepository pincodeRepository;

	@Override
	public Location fetchAddressByPin(String pin) {
		Location location = new Location();
		Pincode pincodeDB = pincodeRepository.findByPin(pin);

		if (pincodeDB == null) {
			Pincode pincode = new Pincode();
			List<PostOffice> pinAddresses = pincodeRestTemplate.getAddressByPiccode(pin);
			if (!CollectionUtils.isEmpty(pinAddresses)) {

				PostOffice pinAddress = pinAddresses.get(0);

				location.setPin(pinAddress.getPincode());
				location.setCountry(pinAddress.getCountry());
				location.setState(pinAddress.getState());
				location.setCity(pinAddress.getBlock());
				location.setDistrict(pinAddress.getDistrict());
				location.setDivision(pinAddress.getDivision());

				List<String> names = pinAddresses.stream().map(PostOffice::getName).collect(Collectors.toList());

				List<String> blocks = pinAddresses.stream().map(PostOffice::getBlock).collect(Collectors.toList());

				location.setAreas(names);
				location.setBlocks(blocks);

				BeanUtils.copyProperties(location, pincode);
				pincodeRepository.save(pincode);
			}
		} else {
			BeanUtils.copyProperties(pincodeDB, location);
		}
		return location;
	}

}
