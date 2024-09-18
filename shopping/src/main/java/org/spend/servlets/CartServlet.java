package org.spend.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.spend.CartSummaryHtmlGenerator;
import org.spend.manager.CartManager;
import org.spend.model.CartItem;
import org.spend.persistence.DatabaseManager;

public class CartServlet extends HttpServlet {

    private static final String EMAIL = "email";
    private static final String IMG = "imgAddress";
    private static final String ITEM_NAME = "itemName";
    private static final String PRICE = "itemPrice";
    private static final String LOGIN_HTML = "/shopping/login.html";
    private static final String CATALOG_HTML = "/shopping/catalog.html";

    private CartManager cartManager;
    private DatabaseManager db;

    @Override
    public void init() throws ServletException {
        super.init();
        this.db = new DatabaseManager();
        this.cartManager = new CartManager(db.getUserCarts());
    }

    @Override
    public void destroy() {
        db.writeUserCarts(cartManager.getUserCarts());
        super.destroy();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String email = session.getAttribute(EMAIL).toString();
            if (email != null) {
                Map<CartItem, Integer> userCart = cartManager.getUserCart(email);
                PrintWriter out = response.getWriter();
                if (userCart == null || userCart.isEmpty()) {
                    out.println(CartSummaryHtmlGenerator.getEmptyCartPage());
                } else {
                    out.println(CartSummaryHtmlGenerator.getCartSummaryPage(userCart));
                }
            } else {
                response.sendRedirect(LOGIN_HTML);
            }
        } else {
            response.sendRedirect(LOGIN_HTML);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String email = session.getAttribute(EMAIL).toString();
            if (email != null) {
                String imgAddress = request.getParameter(IMG);
                String itemName = request.getParameter(ITEM_NAME);
                double price = Double.parseDouble(request.getParameter(PRICE));

                CartItem cartItem = CartItem.builder()
                    .imgAddress(imgAddress)
                    .name(itemName)
                    .price(price)
                    .build();

                cartManager.addToCart(email, cartItem);
                response.sendRedirect(CATALOG_HTML);
            }
        } else {
            response.sendRedirect(LOGIN_HTML);
            return;
        }
    }

}
