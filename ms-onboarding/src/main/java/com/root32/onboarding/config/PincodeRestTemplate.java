package com.root32.onboarding.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.root32.dto.PincodeResponse;
import com.root32.dto.PostOffice;

@Component
public class PincodeRestTemplate {

	@Autowired
	private RestTemplate restTemplate;

	public List<PostOffice> getAddressByPiccode(String pincode) {

		final String uri = "https://api.postalpincode.in/pincode/" + pincode;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<PincodeResponse[]> pincodeResponseEntity = restTemplate.getForEntity(uri,
				PincodeResponse[].class);

		HttpStatus statusCode = pincodeResponseEntity.getStatusCode();

		if (statusCode.equals(HttpStatus.OK)) {

			PincodeResponse[] pincodeRes = pincodeResponseEntity.getBody();
			String status = pincodeRes[0].getStatus();
			if ("Success".equals(status)) {
				List<PostOffice> postOffice = pincodeRes[0].getPostOffice();
				return postOffice;
			} else {
				return null;
			}
		}
		return null;
	}
	
	
	
	
	
}
