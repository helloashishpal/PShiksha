package com.example.pshiksha.services

import java.io.Serializable

class ServiceItem(
    val title: String,
    val description: String,
    val imageResId: Int,
    val onClickActivity: Class<*>,
    val price: Int
) : Serializable