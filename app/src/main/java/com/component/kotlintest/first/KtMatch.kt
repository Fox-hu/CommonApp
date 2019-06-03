package com.component.kotlintest.first

//模式匹配 不太懂 未完成
sealed class Coupon {
    companion object {
        //优惠券的状态值
        val NotFetched = 1
        val Fetched = 2
        val Used = 3
        val Expired = 4
        val UnAvilable = 5

        //优惠券的种类
        val CashType = "CASH"
        val DiscoutType = "DISCOUNT"
        val GiftType = "GIFT"
    }

    class CashCoupon(val id: Long, val type: String, val leastCost: Long, val reduceCost: Long) : Coupon()
    class DiscountCoupon(val id: Long, val type: String, val discount: Int) : Coupon()
    class GiftCoupon(val id: Long, val type: String, val gift: String) : Coupon()

    fun fetched(c: Coupon, user: User): Boolean {
        return true
    }

}

data class User(var name: String, var age: Int)