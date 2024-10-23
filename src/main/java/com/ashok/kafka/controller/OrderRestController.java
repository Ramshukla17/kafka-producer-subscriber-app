package com.ashok.kafka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ashok.kafka.constants.AppConstant;
import com.ashok.kafka.model.Order;

@RestController
public class OrderRestController {

	@Autowired
	private KafkaTemplate<String, Order> kafkaTemplate;
	
	@PostMapping("/order")
	public String placeOrder(@RequestBody Order order) {
		
		kafkaTemplate.send(AppConstant.TOPIC, order);
		return "Order Placed";
	}
}
