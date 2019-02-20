package cart;

public class ShoppingCart {
	int i=0;
	Product[] cart = new Product[5];
	//int name;

	void addProduct(Product p) {

		cart[i++] = p;
	}

	void checkOut() {
		for (int j = 0; j <= 4; j++) {
			System.out.println("product bought:" + cart[j].name);
		}

	}

}
