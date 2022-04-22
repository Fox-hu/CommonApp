package com.component.kotlintest.demo.kotlin_demo.koans

//task1
data class MyDate(val year: Int, val month: Int, val dayOfMonth: Int) : Comparable<MyDate> {
    override fun compareTo(other: MyDate) =
        if (this.year.compareTo(other.year) == 0) {
            if (this.month.compareTo(other.month) == 0) {
                this.dayOfMonth.compareTo(other.dayOfMonth)
            } else {
                this.month.compareTo(other.month)
            }
        } else {
            this.year.compareTo(other.year)
        }

    //task3
    operator fun MyDate.rangeTo(other: MyDate) = DateRange(this,other)
}

fun compare(date1: MyDate, date2: MyDate) = date1 < date2

//task2
class DateRange(val start: MyDate, val endInclusive: MyDate){
    operator fun contains(other: MyDate): Boolean =
        other in start..endInclusive
}

fun checkInRange(date: MyDate, first: MyDate, last: MyDate): Boolean {
    return date in DateRange(first, last)
}


fun isLeapDay(date: MyDate): Boolean {

    val (year, month, dayOfMonth) = date

    // 29 February of a leap year
    return year % 4 == 0 && month == 2 && dayOfMonth == 29
}

//task4
class Invokable {
    var numberOfInvocations: Int = 0
        private set
    operator fun invoke(): Invokable {
        numberOfInvocations++
        return this
    }
}

fun invokeTwice(invokable: Invokable) = invokable()()

