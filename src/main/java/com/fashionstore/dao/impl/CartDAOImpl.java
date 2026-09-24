package com.fashionstore.dao.impl;

import com.fashionstore.dao.CartDAO;
import com.fashionstore.model.Cart;
import com.fashionstore.util.DBConnection;

import java.sql.*;
import java.util.*;

public class CartDAOImpl implements CartDAO {

    @Override
    public boolean addToCart(Cart cart) {
        String sql = "INSERT INTO cart (user_id, variant_id, quantity) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cart.getUserId());
            ps.setInt(2, cart.getVariantId());
            ps.setInt(3, cart.getQuantity());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error addToCart: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Cart> getCartByUserId(int userId) {
        List<Cart> list = new ArrayList<>();
        String sql = "SELECT c.cart_id, c.user_id, c.variant_id, c.quantity, p.name AS product_name, p.price, p.image_url " +
                     "FROM cart c " +
                     "JOIN product_variants pv ON c.variant_id = pv.variant_id " +
                     "JOIN products p ON pv.product_id = p.product_id " +
                     "WHERE c.user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapCart(rs));
        } catch (SQLException e) {
            System.out.println("Error getCartByUserId: " + e.getMessage());
        }
        return list;
    }

    @Override
    public boolean updateCartQuantity(int cartId, int quantity) {
        String sql = "UPDATE cart SET quantity = ? WHERE cart_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, cartId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updateCartQuantity: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean removeFromCart(int cartId) {
        String sql = "DELETE FROM cart WHERE cart_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error removeFromCart: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean clearCart(int userId) {
        String sql = "DELETE FROM cart WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error clearCart: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isVariantInCart(int userId, int variantId) {
        String sql = "SELECT cart_id FROM cart WHERE user_id = ? AND variant_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, variantId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error isVariantInCart: " + e.getMessage());
            return false;
        }
    }

    @Override
    public int getCartItemCount(int userId) {
        String sql = "SELECT SUM(quantity) FROM cart WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error getCartItemCount: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public Cart getCartItemDetails(int variantId) {
        String sql = "SELECT pv.variant_id, p.name AS product_name, p.price, p.image_url " +
                     "FROM product_variants pv " +
                     "JOIN products p ON pv.product_id = p.product_id " +
                     "WHERE pv.variant_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, variantId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Cart cart = new Cart();
                cart.setVariantId(rs.getInt("variant_id"));
                cart.setProductName(rs.getString("product_name"));
                cart.setPrice(rs.getDouble("price"));
                cart.setImageUrl(rs.getString("image_url"));
                return cart;
            }
        } catch (SQLException e) {
            System.out.println("Error getCartItemDetails: " + e.getMessage());
        }
        return null;
    }

    private Cart mapCart(ResultSet rs) throws SQLException {
        Cart cart = new Cart();
        cart.setCartId(rs.getInt("cart_id"));
        cart.setUserId(rs.getInt("user_id"));
        cart.setVariantId(rs.getInt("variant_id"));
        cart.setQuantity(rs.getInt("quantity"));
        try {
            cart.setProductName(rs.getString("product_name"));
            cart.setPrice(rs.getDouble("price"));
            cart.setImageUrl(rs.getString("image_url"));
        } catch (SQLException e) {
            // Ignore if columns are not present in result set
        }
        return cart;
    }
}