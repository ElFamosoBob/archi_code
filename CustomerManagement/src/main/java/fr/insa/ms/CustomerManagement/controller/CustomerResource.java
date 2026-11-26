package fr.insa.ms.CustomerManagement.controller;

import java.util.HashMap;
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

import fr.insa.ms.CustomerManagement.model.Customer;

@RestController
public class CustomerResource {
	
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
	
	
	private final Map<Integer, Customer> customers = new HashMap<>();
	private int nextId=1;
	
	public CustomerResource() {
		addCustomer(new Customer("Marjorie","Grzeskowiak"));
		addCustomer(new Customer("Daniela","Dragomirescu"));
	}
	
	@GetMapping("/customers")
	public Map <Integer, Customer> getCustomers() {
		System.out.println("Connexion à la BDD "+ dbName + " dbLogin : " + dbLogin + " dbPwd : " + dbMdp  + " serverPort : " + serverPort + " uri : " + dbUri);
		return customers;
	}
	
	@GetMapping("/customers/{id}")
	public Customer getCustomer(@PathVariable int id) {
		return customers.get(id);
	}
	
	@PostMapping("/customers")
	public Customer addCustomer(@RequestBody Customer customer) {
		customer.setId(nextId++);
		customers.put(customer.getId(), customer);
		return customer;
	}
	
	
	@PutMapping("customers/{id}")
	public Customer updateCustomer(@PathVariable int id, @RequestBody Customer customer) {
		customer.setId(id);
		customers.put(id, customer);
		return customer;
	}
	
	@DeleteMapping("/customers/{id}")
	public void deleteCustomer (@PathVariable int id) {
		customers.remove(id);
	}
	
}

