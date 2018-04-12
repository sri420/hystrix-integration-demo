package com.demo.integrationservice;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@MessagingGateway
public interface HystrixServiceGateway {

	
	@Gateway(requestChannel = "get.request.channel", replyChannel = "reply.channel")
	@HystrixCommand(fallbackMethod="getMockdata")
	String getMessage(String name);

	default String getMockData(String name) {
		return "Mock Data:" + name;
	}
}