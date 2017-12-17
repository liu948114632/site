package com.liu.deal;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class DealApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DealApplication.class, args);
	}

	//新建队列
	@Bean
	public Queue hello() {
		return new Queue("hello");
	}

	//新建交换机， 名称为exchange。  配置交换机后，需要在rabbit的配置界面加上该exchange
	@Bean
	public TopicExchange exchange() {
		return new TopicExchange("orders_exchange");
	}

	//配置交换机的路由方式  以orders 开头的都 接受。 绑定队列，是通过hello  来传送的
	@Bean
	Binding bindingExchangeMessages(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with("orders.*");//*表示一个词,#表示零个或多个词
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
