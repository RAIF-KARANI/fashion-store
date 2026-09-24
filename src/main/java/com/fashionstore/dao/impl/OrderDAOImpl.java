package com.fashionstore.dao.impl;

import java.sql.*;
import java.util.*;

import com.fashionstore.dao.OrderDAO;
import com.fashionstore.model.Order;
import com.fashionstore.model.OrderItem;
import com.fashionstore.util.DBConnection;

public class OrderDAOImpl implements OrderDAO {

    private Connection conn = DBConnection.getConnection();

    @Override
    public int placeOrder(Order order, List<OrderItem> items) {
        try {
            conn.setAutoCommit(false);

            String orderSql = "INSERT INTO orders(user_id, total_amount, status) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, order.getUserId());
            ps.setDouble(2, order.getTotalAmount());
            ps.setString(3, order.getStatus());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int orderId = -1;
            if (rs.next()) orderId = rs.getInt(1);

            String itemSql = "INSERT INTO order_items(order_id, product_id, size_id, quantity, price) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ips = conn.prepareStatement(itemSql);

            for (OrderItem item : items) {
                ips.setInt(1, orderId);
                ips.setInt(2, item.getProductId());
                ips.setInt(3, item.getSizeId());
                ips.setInt(4, item.getQuantity());
                ips.setDouble(5, item.getPrice());
                ips.addBatch();
            }

            ips.executeBatch();

            conn.commit();
            return orderId;

        } catch (Exception e) {
            try { conn.rollback(); } catch (Exception ex) {}
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM orders WHERE user_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order o = new Order();
                o.setOrderId(rs.getInt("order_id"));
                o.setUserId(rs.getInt("user_id"));
                o.setTotalAmount(rs.getDouble("total_amount"));
                o.setStatus(rs.getString("status"));
                list.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean updateOrderStatus(int orderId, String status) {
        try {
            String sql = "UPDATE orders SET status=? WHERE order_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean cancelOrder(int orderId) {
        return updateOrderStatus(orderId, "CANCELLED");
    }

    @Override
    public Order getOrderById(int orderId) {
        try {
            String sql = "SELECT * FROM orders WHERE order_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Order o = new Order();
                o.setOrderId(rs.getInt("order_id"));
                o.setUserId(rs.getInt("user_id"));
                o.setTotalAmount(rs.getDouble("total_amount"));
                o.setStatus(rs.getString("status"));
                return o;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<OrderItem> getOrderItems(int orderId) {
        List<OrderItem> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM order_items WHERE order_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrderItem oi = new OrderItem();
                oi.setOrderItemId(rs.getInt("order_item_id"));
                oi.setOrderId(rs.getInt("order_id"));
                oi.setProductId(rs.getInt("product_id"));
                oi.setSizeId(rs.getInt("size_id"));
                oi.setQuantity(rs.getInt("quantity"));
                oi.setPrice(rs.getDouble("price"));
                list.add(oi);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}