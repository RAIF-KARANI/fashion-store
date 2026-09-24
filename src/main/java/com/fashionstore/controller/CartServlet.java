package com.fashionstore.controller;

import com.fashionstore.dao.CartDAO;
import com.fashionstore.dao.impl.CartDAOImpl;
import com.fashionstore.model.Cart;
import com.fashionstore.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private CartDAO cartDAO = new CartDAOImpl();
    private Gson gson       = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(true);
        JsonObject jsonResponse = new JsonObject();
        
        List<Cart> cartItems = new ArrayList<>();
        int itemCount = 0;

        if (session.getAttribute("loggedUser") != null) {
            User loggedUser = (User) session.getAttribute("loggedUser");
            cartItems = cartDAO.getCartByUserId(loggedUser.getUserId());
            itemCount = cartDAO.getCartItemCount(loggedUser.getUserId());
            jsonResponse.addProperty("isLoggedIn", true);
        } else {
            jsonResponse.addProperty("isLoggedIn", false);
            List<Cart> guestCart = (List<Cart>) session.getAttribute("guestCart");
            if (guestCart != null) {
                cartItems = guestCart;
                for (Cart c : guestCart) {
                    itemCount += c.getQuantity();
                }
            }
        }

        jsonResponse.addProperty("success", true);
        jsonResponse.add("cartItems", gson.toJsonTree(cartItems));
        jsonResponse.addProperty("itemCount", itemCount);

        out.print(jsonResponse);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String action = request.getParameter("action");
        if ("remove".equals(action)) {
            handleRemove(request, response, out);
            return;
        }

        HttpSession session = request.getSession(true);
        JsonObject jsonResponse = new JsonObject();
        
        int variantId = Integer.parseInt(request.getParameter("variantId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        if (session.getAttribute("loggedUser") != null) {
            User loggedUser = (User) session.getAttribute("loggedUser");
            if (cartDAO.isVariantInCart(loggedUser.getUserId(), variantId)) {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Item already in cart!");
                out.print(jsonResponse);
                return;
            }
            Cart cart = new Cart();
            cart.setUserId(loggedUser.getUserId());
            cart.setVariantId(variantId);
            cart.setQuantity(quantity);
            boolean added = cartDAO.addToCart(cart);
            jsonResponse.addProperty("success", added);
            jsonResponse.addProperty("message", added ? "Item added to cart!" : "Failed to add!");
        } else {
            // Guest - redirect to login
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("requireLogin", true);
            jsonResponse.addProperty("message", "Please login to add items to cart!");
        }
        
        out.print(jsonResponse);
        out.flush();
    }

    private void handleRemove(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        int cartId = Integer.parseInt(request.getParameter("cartId"));
        HttpSession session = request.getSession(true);
        JsonObject jsonResponse = new JsonObject();
        boolean removed = false;

        if (session.getAttribute("loggedUser") != null) {
            removed = cartDAO.removeFromCart(cartId);
        } else {
            List<Cart> guestCart = (List<Cart>) session.getAttribute("guestCart");
            if (guestCart != null) {
                for (int i = 0; i < guestCart.size(); i++) {
                    if (guestCart.get(i).getCartId() == cartId) {
                        guestCart.remove(i);
                        removed = true;
                        break;
                    }
                }
                session.setAttribute("guestCart", guestCart);
            }
        }

        jsonResponse.addProperty("success", removed);
        jsonResponse.addProperty("message", removed ? "Item removed!" : "Failed!");
        out.print(jsonResponse);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        int cartId   = Integer.parseInt(request.getParameter("cartId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        boolean updated = cartDAO.updateCartQuantity(cartId, quantity);
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("success", updated);
        jsonResponse.addProperty("message", updated ? "Cart updated!" : "Failed!");

        out.print(jsonResponse);
        out.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        handleRemove(request, response, out);
        out.flush();
    }
}