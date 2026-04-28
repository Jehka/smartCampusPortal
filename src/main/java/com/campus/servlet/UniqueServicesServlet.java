package com.campus.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

@WebServlet("/UniqueServicesServlet")
public class UniqueServicesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        HashSet<String> uniqueServices = new HashSet<>();

        String sql = "SELECT service_name FROM service_request";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                uniqueServices.add(rs.getString("service_name"));
            }

        } catch (SQLException e) {
            out.println("<p style='color:red;'>Database error: " + e.getMessage() + "</p>");
            out.println("<a href='index.html'>Back to Home</a>");
            return;
        }

        out.println("<h2>Unique Campus Services</h2>");
        out.println("<ul>");
        for (String service : uniqueServices) {
            out.println("<li>" + service + "</li>");
        }
        out.println("</ul>");
        out.println("<p>Total unique services (HashSet size): " + uniqueServices.size() + "</p>");
        out.println("<br><a href='index.html'>Back to Home</a>");
    }
}
