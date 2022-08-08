package com.pshiksha4.pshiksha.main.services

import java.io.Serializable

class ServiceItem(
    val title: String,
    val description: String,
    val imageResId: Int,
    val onClickActivity: Class<*>,
    val price: Int
) : Serializable