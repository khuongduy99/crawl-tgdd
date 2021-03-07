package com.spring.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.model.Link;
import com.spring.model.Product;


@Controller
public class BasicController {
	private static String URL_WEB_TGDD = "https://www.thegioididong.com/";
	
	@RequestMapping("/")
	  public String index(Model model) {
		model.addAttribute("menuList", getAllMenu());
	    return "index";
	  }
	
	@RequestMapping("/{nsx}")
	  public String getListBrand(Model model, @PathVariable String nsx) {
		model.addAttribute("menuList", getAllMenu());
		if(nsx.equals("phu-kien")) {
			model.addAttribute("typeList", getAllPhuKien(nsx));
		} else if(nsx.equals("dong-ho")) {
			model.addAttribute("typeList", getAllTypeDongho("dong-ho-deo-tay"));
		} else if(nsx.equals("may-tinh-de-ban")) {
			return "redirect:/sp/may-tinh-de-ban";
		} else {
			model.addAttribute("brandList", getAllBrand(nsx));
		}
		model.addAttribute("menuActive", "/" +nsx);
	    return "index";
	  }
	
	@RequestMapping("/sp/{nsx}")
	  public String getListProduct(Model model, @PathVariable String nsx) {
		model.addAttribute("productList", getAllProduct(nsx));
		model.addAttribute("menuList", getAllMenu());
		model.addAttribute("linkOnWebTgdd", nsx);
	    return "sanpham";
	  }
	
	@RequestMapping("/phukien/{pk}")
	  public String getListPhukien(Model model, @PathVariable String pk) {
		if(pk.equals("phu-kien")) return "redirect:/sp/phu-kien";
		model.addAttribute("typeList", getAllPhuKien("phu-kien"));
		model.addAttribute("brandList", getAllBrandPhukien(pk));
		model.addAttribute("menuList", getAllMenu());
		model.addAttribute("typeActive", pk);
	    return "index";
	  }
	
	@RequestMapping("/dongho/{dh}")
	  public String getListDongho(Model model, @PathVariable String dh) {
		model.addAttribute("typeList", getAllTypeDongho("dong-ho-deo-tay"));
		model.addAttribute("brandList", getAllBrand(dh));
		model.addAttribute("menuList", getAllMenu());
		model.addAttribute("typeActive", dh);
	    return "index";
	  }
	
	@RequestMapping("/download")
	  public void dowloadFile(@RequestParam String dl,HttpServletRequest request, HttpServletResponse response) {
		
		
		List<Product> list = getAllProduct(dl);
	    for(Product p : list) {
	    	getArticleAndParameterProduct(p.getUrl(), p);
	    }
	    
	    HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Product sheet");
 
        int rownum = 0;
        Cell cell;
        Row row;
        //
        HSSFCellStyle style = createStyleForTitle(workbook);
 
        row = sheet.createRow(rownum);
 
        // Name
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("Name");
        cell.setCellStyle(style);
        // UrlImage
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Url Image");
        cell.setCellStyle(style);
        // Price
        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Price");
        cell.setCellStyle(style);
        // Article
        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Article");
        cell.setCellStyle(style);
        
        //Parameter
        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue("Parameter");
        cell.setCellStyle(style);
 
        // Data
        for (Product model : list) {
            rownum++;
            row = sheet.createRow(rownum);
 
            // EmpNo (A)
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(model.getTitle());
            // EmpName (B)
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(model.getUrlImage());
            // Salary (C)
            cell = row.createCell(2, CellType.STRING);
            cell.setCellValue(model.getPrice());
            // Grade (D)
            cell = row.createCell(3, CellType.STRING);
            cell.setCellValue(model.getArticle());
            // Grade (E)
            cell = row.createCell(4, CellType.STRING);
            cell.setCellValue(model.getParameter());
        }
        Resource resource = new ClassPathResource("product.xls");
        File file = null;
		try {
			file = resource.getFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        file.getParentFile().mkdirs();
 
        FileOutputStream outFile;
		try {
			outFile = new FileOutputStream(file);
			workbook.write(outFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        System.out.println("Created file: " + file.getAbsolutePath());
        
        response.setContentType("application/vnd.ms-excel");
		response.addHeader("Content-Disposition", "attachment; filename=product.xls");
		try {
			Path pFile = Paths.get(resource.getURI());
			Files.copy(pFile, response.getOutputStream());
			response.getOutputStream().flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	  }
	
	private HSSFCellStyle createStyleForTitle(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }
	
	public List<Link> getAllMenu() {
		List<Link> res = null;
		Document document = null;
		try {
			res = new ArrayList<Link>();
			document = Jsoup.connect(URL_WEB_TGDD).get();
			Elements header = document.getElementsByTag("header");
			Elements nav = header.get(0).getElementsByTag("nav");
			Elements link = nav.get(0).getElementsByTag("a");
			for(Element l : link) {
				if(l.getElementsByTag("b").size() != 0) {
					res.add(new Link(l.attr("href"), l.getElementsByTag("b").get(0).html()));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return res;
	}
	
	public List<Link> getAllBrand(String url) {
		List<Link> res = null;
		Document document = null;
		try {
			res = new ArrayList<Link>();
			document = Jsoup.connect(URL_WEB_TGDD + url).get();
			Elements fillter = document.getElementsByClass("filter");
			Elements menu = fillter.get(0).getElementsByClass("manu");
			
			Elements link = menu.get(0).getElementsByTag("a");
			for(Element l : link) {
				if(!l.attr("data-name").equals("")) {
					res.add(new Link("/sp" + l.attr("href"), l.attr("data-name")));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return res;
	}
	
	public List<Link> getAllBrandPhukien(String url) {
		List<Link> res = null;
		Document document = null;
		try {
			res = new ArrayList<Link>();
			document = Jsoup.connect(URL_WEB_TGDD + url).get();
			Elements manuwrapPK = document.getElementsByClass("manuwrapPK");
			
			Elements link = manuwrapPK.get(0).getElementsByTag("a");
			for(Element l : link) {
					res.add(new Link("/sp" + l.attr("href"), l.attr("data-name")));
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return res;
	}
	
	public List<Link> getAllPhuKien(String url) {
		List<Link> res = null;
		Document document = null;
		try {
			res = new ArrayList<Link>();
			document = Jsoup.connect(URL_WEB_TGDD + url).get();
			Elements navaccessories = document.getElementsByClass("navaccessories2019");
			
			Elements link = navaccessories.get(0).getElementsByTag("a");
			for(Element l : link) {
					res.add(new Link("/phukien" + l.attr("href"), l.getElementsByTag("h3").html()));
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return res;
	}
	
	public List<Link> getAllTypeDongho(String url) {
		List<Link> res = null;
		Document document = null;
		try {
			res = new ArrayList<Link>();
			document = Jsoup.connect(URL_WEB_TGDD + url).get();
			Elements find = document.getElementsByClass("find-clock");
			
			Elements link = find.get(0).getElementsByTag("a");
			for(Element l : link) {
					res.add(new Link("/dongho" + l.attr("href"), l.getElementsByTag("strong").html()));
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		res.remove(res.size() - 1);
		res.remove(0);
		return res;
	}
	
	public List<Product> getAllProduct(String url) {
		List<Product> res = null;
		Document document = null;
		try {
			res = new ArrayList<Product>();
			document = Jsoup.connect(URL_WEB_TGDD + url).get();
			Elements item = document.getElementsByClass("item");
			
			for(Element ele : item) {
				Product product = new Product();
				product.setUrl(ele.getElementsByTag("a").get(0).attr("href"));
				String data_original = ele.getElementsByTag("img").get(0).attr("data-original");
				if(data_original.equals("")) {
					product.setUrlImage(ele.getElementsByTag("img").get(0).attr("src"));
				} else {
					product.setUrlImage(data_original);
				}
				
				product.setTitle(ele.getElementsByTag("h3").get(0).html());
				Elements price = ele.getElementsByClass("price");
				
				
				product.setPrice(price.get(0).getElementsByTag("strong").get(0).html());
				res.add(product);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return res;
	}
	
	public Product getArticleAndParameterProduct(String url, Product product) {
		Document document = null;
		try {
			document = Jsoup.connect(URL_WEB_TGDD + url).get();
			 Elements article = document.getElementsByClass("area_article");
			 
			 Elements parameter = document.getElementsByClass("parameter");
			  
			 
			
			 product.setArticle(article.get(0).html().split("<div")[0]);
			 
			 product.setParameter(parameter.get(0).html());
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return product;
	}
	  
	 public static void main(String[] args) throws IOException {
		 BasicController b = new BasicController();
//		 DetailProduct p = b.getOneProduct("/laptop/apple-macbook-pro-2020-m1-myd82saa");
//		 System.out.println(p.getName());
		 
		 
	}
}
