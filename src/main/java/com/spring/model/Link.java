package com.spring.model;

public class Link {
	private String href;
	private String title;
	
	
	public Link() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public Link(String href, String title) {
		super();
		this.href = href;
		this.title = title;
	}


	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
