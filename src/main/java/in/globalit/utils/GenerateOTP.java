package in.globalit.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class GenerateOTP {

	public String generateOTP() {
		return RandomStringUtils.randomAlphanumeric(6);
	}

}
