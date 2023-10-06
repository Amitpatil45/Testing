package com.root32.configsvc.util;

public class OTPGenerator {
	public static String generate() {
		int randomPin = (int) (Math.random() * 900000) + 100000;
		String otp = String.valueOf(randomPin);
		return otp;
	}

	public static void main(String args[]) {
		String otpSting = generate();
		System.out.println("OTP : " + otpSting);
	}
}