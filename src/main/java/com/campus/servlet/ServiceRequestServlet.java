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

@WebServlet("/ServiceRequestServlet")
public class ServiceRequestServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        int    requestId      = Integer.parseInt(request.getParameter("request_id"));
        int    userId         = Integer.parseInt(request.getParameter("user_id"));
        String serviceName    = request.getParameter("service_name");
        String requestDetails = request.getParameter("request_details");
        String status         = request.getParameter("status");

        String sql = "INSERT INTO service_request (request_id, user_id, service_name, request_details, status) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, requestId);
            ps.setInt(2, userId);
            ps.setString(3, serviceName);
            ps.setString(4, requestDetails);
            ps.setString(5, status);

            int rows = ps.executeUpdate();

            out.println("<h2>Service Request Submission</h2>");
            if (rows > 0) {
                out.println("<p style='color:green;'>Service request submitted successfully! "
                        + "Request ID: " + requestId + "</p>");
            } else {
                out.println("<p style='color:red;'>Submission failed. Please try again.</p>");
            }

        } catch (SQLException e) {
            out.println("<p style='color:red;'>Database error: " + e.getMessage() + "</p>");
        }

        out.println("<br><a href='index.html'>Back to Home</a>");
    }
}
