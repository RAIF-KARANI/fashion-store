package com.fashionstore.controller;

import com.fashionstore.dao.UserDAO;
import com.fashionstore.dao.impl.UserDAOImpl;
import com.fashionstore.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAOImpl();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        JsonObject jsonResponse = new JsonObject();

        if (session == null || session.getAttribute("loggedUser") == null) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Not logged in");
            out.print(jsonResponse);
            return;
        }

        User loggedUser = (User) session.getAttribute("loggedUser");
        
        // Refresh from DB
        User user = userDAO.getUserById(loggedUser.getUserId());
        
        if (user != null) {
            user.setPassword(""); // don't send password to frontend
            jsonResponse.addProperty("success", true);
            jsonResponse.add("user", gson.toJsonTree(user));
        } else {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "User not found");
        }

        out.print(jsonResponse);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        JsonObject jsonResponse = new JsonObject();

        if (session == null || session.getAttribute("loggedUser") == null) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Please login first");
            out.print(jsonResponse);
            return;
        }

        User loggedUser = (User) session.getAttribute("loggedUser");
        User currentUser = userDAO.getUserById(loggedUser.getUserId());

        if (currentUser == null) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "User not found");
            out.print(jsonResponse);
            return;
        }

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String password = request.getParameter("password");

        if (name != null) currentUser.setName(name);
        if (email != null) currentUser.setEmail(email);
        if (phone != null) currentUser.setPhone(phone);
        if (address != null) currentUser.setAddress(address);
        if (password != null && !password.isEmpty()) currentUser.setPassword(password);

        boolean updated = userDAO.updateUser(currentUser);

        if (updated) {
            session.setAttribute("loggedUser", currentUser);
            jsonResponse.addProperty("success", true);
            jsonResponse.addProperty("message", "Profile updated successfully!");
        } else {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Failed to update profile.");
        }

        out.print(jsonResponse);
        out.flush();
    }
}
