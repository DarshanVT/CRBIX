package paymentGateway;



import paymentGateway.Cart;
import paymentGateway.Item;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {
    // In-memory catalog using image names I saw in your ZIP
    private Map<String,Item> createCatalog(){
        Map<String,Item> c = new HashMap<>();
        c.put("asian",  new Item("asian","Asian Shoe",   new BigDecimal("1799.00"), "images/asian.jpg"));
        c.put("bacca",  new Item("bacca","Bacca Shoe",   new BigDecimal("1499.00"), "images/bacca.jpg"));
        c.put("bruton", new Item("bruton","Bruton Shoe",  new BigDecimal("2199.00"), "images/bruton.jpg"));
        c.put("campus", new Item("campus","Campus Shoe",  new BigDecimal("1899.00"), "images/campus.jpg"));
        c.put("doctor", new Item("doctor","Doctor Shoe",  new BigDecimal("1599.00"), "images/doctor.jpg"));
        c.put("langer", new Item("langer","Langer Shoe",  new BigDecimal("1999.00"), "images/langer.jpg"));
        return c;
    }

    private int parseQty(String s){
        try { return Integer.parseInt(s); } catch(Exception ex){ return 1; }
    }

    protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);
        Cart cart = (Cart) session.getAttribute("cart");
        if(cart == null){
            cart = new Cart();
            cart.setCatalog(createCatalog());
            session.setAttribute("cart", cart);
        }

        String action = req.getParameter("action");
        if(action == null) action = "view";

        if("add".equals(action)){
            String id = req.getParameter("id");
            int qty = parseQty(req.getParameter("qty"));
            if(id != null) cart.addItem(id, qty);
            resp.sendRedirect(req.getContextPath() + "/CRBIX/cart.html"); // show cart page (static)
            return;
        } else if("update".equals(action)){
            String id = req.getParameter("id");
            int qty = parseQty(req.getParameter("qty"));
            if(id != null) cart.updateItem(id, qty);
            resp.sendRedirect(req.getContextPath() + "/CRBIX/cart.html");
            return;
        } else if("remove".equals(action)){
            String id = req.getParameter("id");
            if(id != null) cart.removeItem(id);
            resp.sendRedirect(req.getContextPath() + "/CRBIX/cart.html");
            return;
        } else if("proceed".equals(action)){
            // go to payment summary servlet
            resp.sendRedirect(req.getContextPath() + "/PaymentSummaryServlet");
            return;
        } else {
            resp.sendRedirect(req.getContextPath() + "/CRBIX/cart.html");
            return;
        }
    }

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { process(req, resp); }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { process(req, resp); }
}
