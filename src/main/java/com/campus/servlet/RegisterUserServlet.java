package com.campus.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/RegisterUserServlet")
public class RegisterUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        int userId = Integer.parseInt(request.getParameter("user_id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String role = request.getParameter("role");
        String sql = "INSERT INTO users (user_id, name, email, role) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setString(4, role);
            int rows = ps.executeUpdate();
            out.println("<h2>User Registration</h2>");
            if (rows > 0) out.println("<p style='color:green;'>User registered! ID: " + userId + "</p>");
            else out.println("<p style='color:red;'>Registration failed.</p>");
        } catch (SQLException e) {
            out.println("<p style='color:red;'>DB error: " + e.getMessage() + "</p>");
        }
        out.println("<br><a href='index.html'>Back to Home</a>");
    }
}