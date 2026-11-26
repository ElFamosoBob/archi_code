package fr.insa.ms.ArticleManagement.model;

public class Order {
	private int id;
	private int quantity;
	private String status; // status possible : rejete, acceptee, en_attente_stock
	private int customerId;
	private int articleId;
	
	public Order(int quantity, String status, int customerId, int articleId) {
		this.quantity = quantity;
		this.status = status;
		this.customerId = customerId;
		this.articleId = articleId;
	}
	
	public Order() {
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public int getArticleId() {
		return articleId;
	}
	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}
}


