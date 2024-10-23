Apacke Kafka

-> It is free open source given by Apche org.
-> It is distributed streaming platform.
-> It is used to process real-time data feeds.
-> It is also called as message broker.
-> Kafka works based on publisher and subcriber model.

Spring boot app + Apache kafka


Step 1 . Download Zookeeper from below URL

		https://zookeeper.apache.org/releases.html

Step 2. Download apache kafka from below URL
		
		https://kafka.apache.org/downloads

Step 3. Set path to Zookeeper in Environment variable upto bin folder.

Note : copy zookeeper.properties and server.properties files from kafka/config folder to kafka/bin /windows folder.

Step 4. Start Zookeeper server using below command from kafka/bin/windows folder.

		Command : zookeeper-server-start.bat zookeeper.properties

Step 5. Start kafka server using below command

		Command : kafka-server-start.bat server.properties

Step 6. Create kafka Topic using below command from kafka/bin/windows folder

		Command : kafka-topics.bat --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic amazon_orders_topic

Step 7. View created Topic using below command 

		Cammand : kafka-topics.bat –-list --bootstrap-server localhost:9092 


Kafka Producer App Development

1.	Dependency required :

<dependencies>
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter</artifactId>
</dependency>
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-test</artifactId>
<scope>test</scope>
</dependency>
<dependency>
<groupId>org.apache.kafka</groupId>
<artifactId>kafka-streams</artifactId>
</dependency>
<dependency>
<groupId>org.springframework.kafka</groupId>
<artifactId>spring-kafka</artifactId>
</dependency>
<dependency>
<groupId>com.fasterxml.jackson.core</groupId>
<artifactId>jackson-databind</artifactId>
</dependency>
<dependency>
<groupId>org.springframework.kafka</groupId>
<artifactId>spring-kafka-test</artifactId>
<scope>test</scope>
</dependency>
</dependencies>
2.	Create kafka constant class : 

public class AppConstant {
public static final String TOPIC = "ashok-topic";
public static final String HOST = "localhost:9092";
}

3.	Create Model Class to represent data : 

package com.ashok.kafka.model;
public class Order {
private String id;
private Double price;
private String email;

public String getId() {
return id;
}
public void setId(String id) {
this.id = id;
}
public Double getPrice() {
return price;
}
public void setPrice(Double price) {
this.price = price;
}
public String getEmail() {
return email;
}
public void setEmail(String email) {
this.email = email;
}
}

4.	Create kafka Producer Config class :

package com.ashok.kafka.config;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import com.ashok.kafka.constants.AppConstant;
import com.ashok.kafka.model.Order;

@Configuration
public class KafkaProducerConfig {

@Bean
public ProducerFactory<String, Order> producerFactory() {
Map<String, Object> configProps = new HashMap<>();
configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConstant.HOST);
configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
return new DefaultKafkaProducerFactory<>(configProps);
}
@Bean
public KafkaTemplate<String, Order> kafkaTemplate(){
return new KafkaTemplate<>(producerFactory());
}
}

5.	Create RestController : 

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

Kafka Subscriber Application :

1.	Dependency required :

<dependencies>
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter</artifactId>
</dependency>
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-test</artifactId>
<scope>test</scope>
</dependency>
<dependency>
<groupId>org.apache.kafka</groupId>
<artifactId>kafka-streams</artifactId>
</dependency>
<dependency>
<groupId>org.springframework.kafka</groupId>
<artifactId>spring-kafka</artifactId>
</dependency>
<dependency>
<groupId>com.fasterxml.jackson.core</groupId>
<artifactId>jackson-databind</artifactId>
</dependency>
<dependency>
<groupId>org.springframework.kafka</groupId>
<artifactId>spring-kafka-test</artifactId>
<scope>test</scope>
</dependency>
</dependencies>

2.	Create kafka constant class : 

public class AppConstant {
public static final String TOPIC = "ashok-topic";
public static final String HOST = "localhost:9092";
}

3.	Create Model Class to represent data : 

package com.ashok.kafka.model;
public class Order {
private String id;
private Double price;
private String email;

public String getId() {
return id;
}
public void setId(String id) {
this.id = id;
}
public Double getPrice() {
return price;
}
public void setPrice(Double price) {
this.price = price;
}
public String getEmail() {
return email;
}
public void setEmail(String email) {
this.email = email;
}
}


4.	Create Subscriber Config Class :

package com.ashok.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.ashok.kafka.constant.AppConstant;
import com.ashok.kafka.model.Order;

@Configuration
public class KafkaSubscriberConfig {

@Bean
public ConsumerFactory<String, Order> consumerFactory() {
Map<String, Object> configProps = new HashMap<>();
configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConstant.HOST);
configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), new JsonDeserializer<>());
}
@Bean
public ConcurrentKafkaListenerContainerFactory<String, Order> kafkaListenerFactory() {
ConcurrentKafkaListenerContainerFactory<String, Order> factory = new ConcurrentKafkaListenerContainerFactory<>();
factory.setConsumerFactory(consumerFactory());
return factory;
}


}

5.	Add below method in Application class :

@KafkaListener(topics = AppConstant.TOPIC, groupId = "group_ashok_order")
public void subscribeMsg(String order) {
System.out.println("*** Msg Recieved from Kafka *** :: ");
System.out.println(order);
}
