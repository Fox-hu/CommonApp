package com.fox.toutiao.pizza


import com.fox.annotation.Factory
import com.fox.annotation.Meal

/**
 * Created by fox.hu on 2018/10/18.
 */

@Factory(id = "TiramisuPizza", type = Meal::class)
class TiramisuPizza : Meal {
    override fun getPrice(): Float {
        return 4.5f
    }
}
