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
import java.util.LinkedList;

@WebServlet("/RequestQueueServlet")
public class RequestQueueServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        LinkedList<ServiceRequest> pendingQueue = new LinkedList<>();
        String sql = "SELECT request_id, user_id, service_name, request_details, status FROM service_request WHERE status = 'Pending'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                pendingQueue.add(new ServiceRequest(rs.getInt("request_id"), rs.getInt("user_id"),
                        rs.getString("service_name"), rs.getString("request_details"), rs.getString("status")));
            }
        } catch (SQLException e) {
            out.println("<p style='color:red;'>DB error: " + e.getMessage() + "</p>");
            out.println("<a href='index.html'>Back to Home</a>");
            return;
        }
        out.println("<h2>Pending Request Queue</h2>");
        if (pendingQueue.isEmpty()) {
            out.println("<p>No pending requests.</p>");
        } else {
            ServiceRequest next = pendingQueue.peek();
            out.println("<h3 style='color:orange;'>Next to Process: Request ID " + next.getRequestId() + " — " + next.getServiceName() + "</h3>");
            out.println("<table border='1' cellpadding='8'><tr><th>ID</th><th>User</th><th>Service</th><th>Details</th><th>Status</th></tr>");
            for (ServiceRequest sr : pendingQueue) {
                out.println("<tr><td>" + sr.getRequestId() + "</td><td>" + sr.getUserId() + "</td><td>"
                        + sr.getServiceName() + "</td><td>" + sr.getRequestDetails() + "</td><td>" + sr.getStatus() + "</td></tr>");
            }
            out.println("</table><p>Total in LinkedList: " + pendingQueue.size() + "</p>");
        }
        out.println("<br><a href='index.html'>Back to Home</a>");
    }
}