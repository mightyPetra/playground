package org.spend;

import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.spend.model.CartItem;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CartSummaryHtmlGenerator {

	public static String getCartSummaryPage(Map<CartItem, Integer> userCart) {
		StringBuilder sb = new StringBuilder();
		// start of html document
		sb.append("<html><head><title>My Cart</title><link rel=\"stylesheet\" href=\"styles.css\"></head><body>");
		
		// title
		sb.append("<h1>Your Cart!</h1>");
				
		// summary container
		sb.append("<div class=\"cart-summary\">");
		
		// for each cart item
		for(Map.Entry<CartItem, Integer> entry: userCart.entrySet()) {
			CartItem currentItem = entry.getKey();
			int quantity = entry.getValue();
			
			// cart item container
			sb.append("<div class=\"cart-item-summary\">");
			
			// include picture, name, and total price
			sb.append(String.format("<img class=\"summary-image\" src=\"%s\">", currentItem.getImgAddress()));
			sb.append(String.format("<h3>Name: %s Quantity: %d Total Price: %.2f</h3>", currentItem.getName(), quantity, quantity * currentItem.getPrice()));
			
			// close cart item container
			sb.append("</div>");
		}
		// button that links to catalog.html
		sb.append("<form><button class=\"cart-button\" type=\"submit\" formaction=\"/shopping/catalog.html\">Keep Shopping</button></form>");
		
		// close summary container
		sb.append("</div>");
		
		// close html document
		sb.append("</body></html>");
		
		return sb.toString();
	}

	public static String getEmptyCartPage() {
		StringBuilder sb = new StringBuilder();

		sb.append("<html><head><title>My Cart</title><link rel=\"stylesheet\" href=\"styles.css\"></head><body>");

		// title
		sb.append("<h1>Your Cart!</h1>");

		// summary container
		sb.append("<div class=\"cart-summary\">");

		sb.append("<h1>Your cart is empty</h1>");
		sb.append("<p><a href=\"/shopping/catalog.html\">Return to shopping</a></p>");
		sb.append("</div>");

		// close html document
		sb.append("</body></html>");

		return sb.toString();
	}

}