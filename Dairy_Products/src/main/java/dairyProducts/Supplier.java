package dairyProducts;

import java.util.ArrayList;

public class Supplier {
	
	private ArrayList<Product> suppliedProducts = new ArrayList<Product>();
	
	public void supplyToShop(Product p) {
		suppliedProducts.add(p);
	}
	public ArrayList<Product> getSuppliedProducts(){
		return suppliedProducts;
	}
}
