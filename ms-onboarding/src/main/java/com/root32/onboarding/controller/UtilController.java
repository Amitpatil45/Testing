package com.root32.onboarding.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.root32.dto.Location;
import com.root32.onboarding.service.PincodeService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/util")
public class UtilController {

	@Autowired
	private PincodeService pincodeService;

	@GetMapping(value = "/pin/{pin}")
	public ResponseEntity<Location> fetchAddressByPin(@PathVariable String pin) {

		Location fetchAddressByPin = pincodeService.fetchAddressByPin(pin);

		if (fetchAddressByPin.getPin() != null) {
			return new ResponseEntity<Location>(fetchAddressByPin, HttpStatus.OK);
		}
		return new ResponseEntity<Location>(fetchAddressByPin, HttpStatus.BAD_REQUEST);
	}
}
