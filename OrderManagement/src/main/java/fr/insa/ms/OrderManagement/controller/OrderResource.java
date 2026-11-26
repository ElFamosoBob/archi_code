package fr.insa.ms.OrderManagement.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import fr.insa.ms.OrderManagement.model.Customer;
import fr.insa.ms.OrderManagement.model.Order;

@RestController
public class OrderResource {
	
	@Value("${db.uri}")
	private String dbUri;
	
	@Value("${db.name}")
	private String dbName;
	
	@Value("${db.login}")
	private String dbLogin;
	
	@Value("${db.pwd}")
	private String dbMdp;
	
	@Value("${server.port}")
	private String serverPort;
	
	@Autowired
	private RestTemplate restTemplate;

	
	private final Map<Integer, Order> orders = new HashMap<>();
	private int nextId=1;
	
	public OrderResource() {
		Order order1 = new Order(1, "En cours", 2, 3);

	    orders.put(1, order1);//because we bypass the standard addOrder way, the id of this fake orders is 0 (which is not a problem, 0 is not used as an id) 
	}
	
	@GetMapping("/orders")
	public Map <Integer, Order> getOrders() {
		System.out.println("Connexion à la BDD "+ dbName + " dbLogin : " + dbLogin + " dbPwd : " + dbMdp  + " serverPort : " + serverPort + " uri : " + dbUri);
		return orders;
	}
	
	@GetMapping("/orders/{id}")
	public Order getOrder(@PathVariable int id) {
		return orders.get(id);
	}
	
	@PostMapping("/orders")
	public Order addOrder(@RequestBody Order order) {
		
		Customer customer = restTemplate.getForObject("http://CustomerManagement/customers/"+order.getCustomerId(), Customer.class);
		
		if (customer == null) {
			order.setStatus("rejete");
		}else {
			Boolean available = restTemplate.getForObject("http://ArticleManagement/articles/"+order.getArticleId()+"/available/"+order.getQuantity(), Boolean.class);
			
			if (available == true) {
				order.setStatus("acceptee");
			}else {
				order.setStatus("en_attente_stock");
			}
		}
		
		order.setId(nextId++);
		orders.put(order.getId(), order);
		return order;
	}
	
	
	@PutMapping("orders/{id}")
	public Order updateOrder(@PathVariable int id, @RequestBody Order order) {
		order.setId(id);
		orders.put(id, order);
		return order;
	}
	
	@DeleteMapping("/orders/{id}")
	public void deleteOrder (@PathVariable int id) {
		orders.remove(id);
	}
	
	@GetMapping("/orders/status/{status}")
	public List<Order> getOrdersByStatus(@PathVariable String status){
		List<Order> result = new ArrayList<>();
		for (Order o : orders.values()) {
			if (o.getStatus().equals(status)) {
				result.add(o);
			}
		}
		return result;
	}
	
}

