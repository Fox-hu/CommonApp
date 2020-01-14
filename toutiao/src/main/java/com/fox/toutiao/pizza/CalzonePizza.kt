package com.fox.toutiao.pizza


import com.fox.annotation.Factory
import com.fox.annotation.Meal

/**
 * Created by fox.hu on 2018/10/18.
 */

@Factory(id = "CalzonePizza", type = Meal::class)
class CalzonePizza : Meal {
    override fun getPrice(): Float {
        return 8.5f
    }
}
