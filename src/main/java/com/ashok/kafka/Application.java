package com.ashok.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

import com.ashok.kafka.constant.AppConstant;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	 @KafkaListener(topics = AppConstant.TOPIC, groupId = "group_ashok_order")
	    public void subscribeMsg(String order) {
	        System.out.println("*** Msg Received from Kafka *** :: ");
	        System.out.println(order);
	    }
}
