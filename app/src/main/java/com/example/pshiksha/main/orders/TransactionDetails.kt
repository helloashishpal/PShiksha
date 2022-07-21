package com.example.pshiksha.main.orders

import java.io.Serializable

class TransactionDetails : Serializable {
    val transactionId: String
    val transactionTime: String
    val price: Int

    constructor() {
        transactionId = "INVALID ID"
        transactionTime = "INVALID TIME"
        price = -1
    }

    constructor(transactionId: String, transactionTime: String, price: Int) {
        this.transactionId = transactionId
        this.transactionTime = transactionTime
        this.price = price
    }
}