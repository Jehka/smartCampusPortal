package com.campus.model;

public class ServiceRequest {
    private int requestId;
    private int userId;
    private String serviceName;
    private String requestDetails;
    private String status;

    public ServiceRequest(int requestId, int userId, String serviceName, String requestDetails, String status) {
        this.requestId = requestId;
        this.userId = userId;
        this.serviceName = serviceName;
        this.requestDetails = requestDetails;
        this.status = status;
    }

    public int getRequestId()         { return requestId; }
    public int getUserId()            { return userId; }
    public String getServiceName()    { return serviceName; }
    public String getRequestDetails() { return requestDetails; }
    public String getStatus()         { return status; }
}