package com.spring.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BasicController {
	@RequestMapping("/")
	  public String index() {
	    return "index";
	  }
	
	@RequestMapping("/menu")
	  public String getMenu(Model model) {
		model.addAttribute("menuList", getAllMenu());
	    return "index";
	  }
	
	public List<String> getAllMenu() {
		List<String> res = null;
		Document document = null;
		try {
			res = new ArrayList<String>();
			document = Jsoup.connect("https://www.thegioididong.com/dtdd").get();
			Elements header = document.getElementsByTag("header");
			Elements nav = header.get(0).getElementsByTag("nav");
			Elements title = nav.get(0).getElementsByTag("b");
			for(Element s : title) {
				res.add(s.html());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return res;
	}
	  
	 public static void main(String[] args) throws IOException {
		
	}
}
