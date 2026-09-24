package com.fashionstore.controller;

import com.fashionstore.dao.UserDAO;
import com.fashionstore.dao.impl.UserDAOImpl;
import com.fashionstore.model.User;
import com.fashionstore.dao.CartDAO;
import com.fashionstore.dao.impl.CartDAOImpl;
import com.fashionstore.model.Cart;
import com.google.gson.JsonObject;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter out = response.getWriter();

        String email    = request.getParameter("email");
        String password = request.getParameter("password");

        JsonObject jsonResponse = new JsonObject();
        User user = userDAO.loginUser(email, password);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("loggedUser", user);
            session.setAttribute("userId", user.getUserId());

            // Merge guest cart
            List<Cart> guestCart = (List<Cart>) session.getAttribute("guestCart");
            if (guestCart != null) {
                CartDAO cartDAO = new CartDAOImpl();
                for (Cart guestItem : guestCart) {
                    if (!cartDAO.isVariantInCart(user.getUserId(), guestItem.getVariantId())) {
                        guestItem.setUserId(user.getUserId());
                        cartDAO.addToCart(guestItem);
                    }
                }
                session.removeAttribute("guestCart");
            }

            jsonResponse.addProperty("success", true);
            jsonResponse.addProperty("message", "Login successful!");
            jsonResponse.addProperty("name", user.getName());
            jsonResponse.addProperty("userId", user.getUserId());
        } else {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Invalid email or password!");
        }

        out.print(jsonResponse);
        out.flush();
    }
}