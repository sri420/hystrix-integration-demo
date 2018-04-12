package com.demo.integrationservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@ImportResource("classpath:/META-INF/spring/integration/hystrix-outbound-config.xml")
@EnableCircuitBreaker
public class HystrixIntegrationApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(HystrixIntegrationApplication.class);

	@Autowired
	@Qualifier("serviceGateway")
	HystrixServiceGateway serviceGateway;

	public static void main(String[] args) {
		SpringApplication.run(HystrixIntegrationApplication.class, args);
	}

	@GetMapping("/callService/{name}")
	public String sendViaGateway(@PathVariable("name") String name) {
		LOGGER.info("Entering with name: " + name);
		LOGGER.info(" Call to serviceGateway with name:" +name);
		String responseMsg = serviceGateway.getMessage(name);
		LOGGER.info("Obtained response from serviceGateway: " + responseMsg);
		LOGGER.info("Leaving");
		return responseMsg;
	}

	
	
}
