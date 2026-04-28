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

@WebServlet("/UpdateStatusServlet")
public class UpdateStatusServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        int    requestId = Integer.parseInt(request.getParameter("request_id"));
        String newStatus = request.getParameter("new_status");

        String sql = "UPDATE service_request SET status = ? WHERE request_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt(2, requestId);
            int rows = ps.executeUpdate();

            out.println("<h2>Update Request Status</h2>");
            if (rows > 0)
                out.println("<p style='color:green;'>Request ID " + requestId
                        + " status updated to: <strong>" + newStatus + "</strong></p>");
            else
                out.println("<p style='color:red;'>No request found with ID: " + requestId + "</p>");

        } catch (SQLException e) {
            out.println("<p style='color:red;'>DB error: " + e.getMessage() + "</p>");
        }
        out.println("<br><a href='index.html'>Back to Home</a>");
    }
}