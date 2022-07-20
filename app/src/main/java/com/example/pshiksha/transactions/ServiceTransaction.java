package com.example.pshiksha.transactions;

import java.io.Serializable;

public class ServiceTransaction implements Serializable {
    private final String transactionId;
    private final String transactionTime;
    private final Integer price;

    public ServiceTransaction() {
        transactionId = "INVALID ID";
        transactionTime = "INVALID TIME";
        price = -1;
    }

    public ServiceTransaction(String transactionId, String transactionTime, Integer price) {
        this.transactionId = transactionId;
        this.transactionTime = transactionTime;
        this.price = price;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public Integer getPrice() {
        return price;
    }
}
