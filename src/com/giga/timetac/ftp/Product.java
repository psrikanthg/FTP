package com.giga.timetac.ftp;

import java.util.ArrayList;
import java.util.List;

public class Product {
	int id;
	String name;
	float price;
	
	public Product(int id, String name, float price) {
		
		this.id = id;
		this.name =name;
		this.price = price;
	}

	
	public static void main(String args[]) {
		
		List<Product> productList= new ArrayList<>();
		
		
		
		productList.add(new Product(1,"HP Laptop", 25000f));
		productList.add(new Product(1,"Dell Laptop", 30000f));
		productList.add(new Product(1,"Lenevo Laptop", 28000f));
		productList.add(new Product(1,"Soney Laptop", 28000f));
		productList.add(new Product(1,"Apple Laptop", 90000f));
		
		productList.add(new Product(1,"Samsung Phone", 2500f));
		productList.add(new Product(1,"Apple ", 3000f));
		productList.add(new Product(1,"Lenevo Phone", 28000f));
		productList.add(new Product(1,"Soney Phone", 28000f));
		productList.add(new Product(1,"Apple I6", 90000f));
		
		List<Float> productPriceList = new ArrayList<>();
		
		for(Product product: productList) {
			
			if(product.price <30000 ) {
				productPriceList.add(product.price);
			}
		}
		
		System.out.println(productPriceList);
		
	}
	
}
