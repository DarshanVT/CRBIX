package paymentGateway;


import paymentGateway.Cart;

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

@WebServlet("/PaymentSummaryServlet")
public class PaymentSummaryServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if(session == null){
            resp.sendRedirect(req.getContextPath() + "/CRBIX/cart.html"); return;
        }
        Cart cart = (Cart) session.getAttribute("cart");
        if(cart == null || cart.isEmpty()){
            resp.sendRedirect(req.getContextPath() + "/CRBIX/cart.html"); return;
        }
        req.setAttribute("cart", cart);
        req.getRequestDispatcher("/WEB-INF/jsp/payment_summary.jsp").forward(req, resp);
    }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // If summary page posts to proceed -> forward to payment
        resp.sendRedirect(req.getContextPath() + "/PaymentServlet");
    }
}

