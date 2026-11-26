package fr.insa.ms.ArticleManagement.controller;

import java.util.Arrays;
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

import fr.insa.ms.ArticleManagement.model.Article;
import fr.insa.ms.ArticleManagement.model.Order;

@RestController
public class ArticleResource {
	
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
	
	private final Map<Integer, Article> articles = new HashMap<>();
	private int nextId=1;
	
	public ArticleResource() {
		addArticle(new Article(6, "table"));
		addArticle(new Article(4, "fenêtre"));
	}
	
	@GetMapping("/articles")
	public Map <Integer, Article> getArticles() {
		System.out.println("Connexion à la BDD "+ dbName + " dbLogin : " + dbLogin + " dbPwd : " + dbMdp  + " serverPort : " + serverPort + " uri : " + dbUri);
		return articles;
	}
	
	@GetMapping("/articles/{id}")
	public Article getArticle(@PathVariable int id) {
		return articles.get(id);
	}
	
	@PostMapping("/articles")
	public Article addArticle(@RequestBody Article article) {
		article.setId(nextId++);
		articles.put(article.getId(), article);
		return article;
	}
	
	
	@PutMapping("articles/{id}")
	public Article updateArticle(@PathVariable int id, @RequestBody Article article) {
		article.setId(id);
		articles.put(id, article);
		return article;
	}
	
	@DeleteMapping("/articles/{id}")
	public void deleteArticle (@PathVariable int id) {
		articles.remove(id);
	}
	
	
	@GetMapping("/articles/{id}/available/{quantity}")
	public boolean isAvailable(@PathVariable int id, @PathVariable int quantity) {
		Article a = articles.get(id);
		if (a!=null) {
			return (a.getQuantity()>=quantity);
		}else {
			System.out.println("error, no article found for this id");
			return false;
		}
	}
	
	@GetMapping("/articles/pending_orders")// pour pouvoir avoir accès aux commandes en attente d'approvisionnement
	public List<Order> getPendingOrders(){

		Order[] pending = restTemplate.getForObject("http://OrderManagement/orders/status/en_attente_stock", Order[].class);
		
		return Arrays.asList(pending);
	}
}

