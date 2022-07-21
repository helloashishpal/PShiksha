package com.example.pshiksha.main.orders

import java.io.Serializable

class OrderDetails : Serializable {
    var userUid: String
    var orderId: String
    var serviceName: String
    var serviceTransaction: TransactionDetails
    var serviceDescription: String
    var serviceStatus: ServiceStatus

    constructor() {
        userUid = "NOT AVAILABLE"
        orderId = "NOT AVAILABLE"
        serviceName = "NOT AVAILABLE"
        serviceTransaction = TransactionDetails()
        serviceDescription = "NOT AVAILABLE"
        serviceStatus = ServiceStatus.PENDING
    }

    constructor(
        userUid: String,
        orderId: String,
        serviceName: String,
        transactionDetails: TransactionDetails,
        serviceDescription: String,
        serviceStatus: ServiceStatus
    ) {
        this.userUid = userUid
        this.orderId = orderId
        this.serviceName = serviceName
        serviceTransaction = transactionDetails
        this.serviceDescription = serviceDescription
        this.serviceStatus = serviceStatus
    }

    enum class ServiceStatus {
        PENDING, COMPLETED
    }
}