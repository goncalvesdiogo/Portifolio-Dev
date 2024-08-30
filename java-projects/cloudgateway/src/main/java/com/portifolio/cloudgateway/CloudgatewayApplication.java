package com.portifolio.cloudgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication
public class CloudgatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudgatewayApplication.class, args);
	}

	@Bean
	public RouteLocator getRoute(RouteLocatorBuilder builder){
		return builder
				.routes()
					.route(r ->  r.path("/service-stock/**").uri("lb://servicestock"))
					.route(r ->  r.path("/api-bff/**").uri("lb://apibff"))
				.build();
	}

}
