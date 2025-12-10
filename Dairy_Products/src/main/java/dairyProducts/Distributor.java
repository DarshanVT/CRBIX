package dairyProducts;

import java.util.ArrayList;

public class Distributor {
	
	private ArrayList<Product> stock = new ArrayList<Product>();
	
	public void buyFromFarmer(Product p) {
		stock.add(p);
	}
	public ArrayList<Product> getStocks(){
		return stock;	
	}
}