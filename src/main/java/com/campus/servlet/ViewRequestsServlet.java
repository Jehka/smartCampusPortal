package com.campus.servlet;

import com.campus.model.ServiceRequest;
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
import java.util.ArrayList;

@WebServlet("/ViewRequestsServlet")
public class ViewRequestsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        ArrayList<ServiceRequest> requestList = new ArrayList<>();

        String sql = "SELECT request_id, user_id, service_name, request_details, status FROM service_request";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ServiceRequest sr = new ServiceRequest(
                        rs.getInt("request_id"),
                        rs.getInt("user_id"),
                        rs.getString("service_name"),
                        rs.getString("request_details"),
                        rs.getString("status")
                );
                requestList.add(sr);
            }

        } catch (SQLException e) {
            out.println("<p style='color:red;'>Database error: " + e.getMessage() + "</p>");
            out.println("<a href='index.html'>Back to Home</a>");
            return;
        }

        out.println("<h2>All Service Requests</h2>");
        out.println("<table border='1' cellpadding='8' cellspacing='0'>");
        out.println("<tr><th>Request ID</th><th>User ID</th><th>Service</th>"
                + "<th>Details</th><th>Status</th></tr>");

        for (ServiceRequest sr : requestList) {
            out.println("<tr>");
            out.println("<td>" + sr.getRequestId()      + "</td>");
            out.println("<td>" + sr.getUserId()          + "</td>");
            out.println("<td>" + sr.getServiceName()     + "</td>");
            out.println("<td>" + sr.getRequestDetails()  + "</td>");
            out.println("<td>" + sr.getStatus()          + "</td>");
            out.println("</tr>");
        }

        out.println("</table>");
        out.println("<p>Total records in ArrayList: " + requestList.size() + "</p>");
        out.println("<br><a href='index.html'>Back to Home</a>");
    }
}
