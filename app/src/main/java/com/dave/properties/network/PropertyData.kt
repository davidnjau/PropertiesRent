package com.dave.properties.network

data class Properties(
    val id : String?,
    val propertyOccupancyDate: String,
    val propertyVatStatus : String,
    val propertyName: String,
    val propertyLocation: String,
    val propertyDetails: String,
    val propertyLandlordDetails: String,
    val paymentSchedule: String,
    val propertyTenancyPeriod: String,
    val propertyRent: String,
    val propertyRentAmount: Double,
    val incrementalPerc: String,
    val propertyDepositAmount: String,
)
