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

@WebServlet("/DeleteRequestServlet")
public class DeleteRequestServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        int requestId = Integer.parseInt(request.getParameter("request_id"));

        String sql = "DELETE FROM service_request WHERE request_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, requestId);

            int rows = ps.executeUpdate();

            out.println("<h2>Delete Request</h2>");
            out.println("<p>Number of records deleted: <strong>" + rows + "</strong></p>");
            if (rows > 0) {
                out.println("<p style='color:green;'>Request ID " + requestId + " deleted successfully.</p>");
            } else {
                out.println("<p style='color:red;'>No request found with ID: " + requestId + "</p>");
            }

        } catch (SQLException e) {
            out.println("<p style='color:red;'>Database error: " + e.getMessage() + "</p>");
        }

        out.println("<br><a href='index.html'>Back to Home</a>");
    }
}
