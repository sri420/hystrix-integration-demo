package com.demo.hystrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@SpringBootApplication
@RestController
@ImportResource("classpath:/META-INF/spring/integration/hystrix-outbound-config.xml")
@EnableHystrix
public class HystrixIntegrationApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(HystrixIntegrationApplication.class);

	@Autowired
	HystrixServiceGateway serviceGateway;

	public static void main(String[] args) {
		SpringApplication.run(HystrixIntegrationApplication.class, args);
	}

	@GetMapping("/callService/{name}")
	public String sendViaGateway(@PathVariable("name") String name) throws Exception {
		LOGGER.info("Entering with name: " + name);
		LOGGER.info(" Call to hystrixService with name:" + name);
		String responseMsg = getByName(name);
		LOGGER.info("Obtained response from hystrixService: " + responseMsg);
		LOGGER.info("Leaving");
		return responseMsg;
	}

	public String getByName(String name) throws Exception {

		LOGGER.info("Entering");
		
		LOGGER.info("Received name:" + name);

		LOGGER.info("Checking for Valid name");
		if (null != name && name.equalsIgnoreCase("khan")) {
			LOGGER.error("Exiting as, name is not valid.");
			throw new Exception("name is not valid.");
		}
		LOGGER.info("name is Valid. About to Make Call to Gateway...");

		// Call Gateway
		String responseMsg = gatewayCallGetMessage(name);
		LOGGER.info("Obtained response:" + responseMsg + " From Gateway");
		LOGGER.info("Leaving");
		return responseMsg;

	}

	// Wrap Gateway Call With Hystrix FallbackMethod
	@HystrixCommand(fallbackMethod = "getMockData")
	public String gatewayCallGetMessage(String name) {
		LOGGER.info("Entering");
		String responseMsg = null;

		LOGGER.info("Wrapping  Call To serviceGateway in Hystrix Command.");
		responseMsg = serviceGateway.getMessage(name);
		LOGGER.info("Obtained Response:" + responseMsg + " From Gateway");
		LOGGER.info("Leaving");

		return responseMsg;
	}

	// fallback method
	public String getMockData(String name) {
		LOGGER.info("Entering");
		LOGGER.info("Inside Fallback method as Service is Down.");
		String responseMsg;
		if (null != name && name.equalsIgnoreCase("john")) {
			responseMsg = "Michael";
		} else {
			responseMsg = name;
		}
		LOGGER.info("Leaving");
		return "Mock Data:" + responseMsg;
	}

}
