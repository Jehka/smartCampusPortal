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
import java.util.HashMap;
import java.util.Map;

@WebServlet("/UserRequestSummaryServlet")
public class UserRequestSummaryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        HashMap<Integer, Integer> userRequestCount = new HashMap<>();

        String sql = "SELECT user_id FROM service_request";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int uid = rs.getInt("user_id");
                userRequestCount.put(uid, userRequestCount.getOrDefault(uid, 0) + 1);
            }

        } catch (SQLException e) {
            out.println("<p style='color:red;'>Database error: " + e.getMessage() + "</p>");
            out.println("<a href='index.html'>Back to Home</a>");
            return;
        }

        out.println("<h2>User Request Summary</h2>");
        out.println("<table border='1' cellpadding='8' cellspacing='0'>");
        out.println("<tr><th>User ID</th><th>Number of Requests</th></tr>");

        for (Map.Entry<Integer, Integer> entry : userRequestCount.entrySet()) {
            out.println("<tr><td>" + entry.getKey() + "</td><td>" + entry.getValue() + "</td></tr>");
        }

        out.println("</table>");
        out.println("<p>Total users in HashMap: " + userRequestCount.size() + "</p>");
        out.println("<br><a href='index.html'>Back to Home</a>");
    }
}
