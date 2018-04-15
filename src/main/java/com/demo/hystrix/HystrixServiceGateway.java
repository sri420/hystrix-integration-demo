package com.demo.hystrix;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface HystrixServiceGateway {

	@Gateway(requestChannel = "get.request.channel", replyChannel = "reply.channel")
	String getMessage(String name);

}