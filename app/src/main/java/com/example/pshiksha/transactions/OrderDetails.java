package com.example.pshiksha.transactions;

import java.io.Serializable;

public class OrderDetails implements Serializable {
    private final String userUid;
    private final String orderId;
    private final String serviceName;
    private final ServiceTransaction serviceTransaction;

    public OrderDetails() {
        userUid = "INVALID ID";
        orderId = "INVALID ID";
        serviceName = "INVALID NAME";
        serviceTransaction = null;
    }

    public OrderDetails(String userUid, String orderId, String serviceName, ServiceTransaction serviceTransaction) {
        this.userUid = userUid;
        this.orderId = orderId;
        this.serviceName = serviceName;
        this.serviceTransaction = serviceTransaction;
    }

    public String getUserUid() {
        return userUid;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public ServiceTransaction getServiceTransaction() {
        return serviceTransaction;
    }
}
