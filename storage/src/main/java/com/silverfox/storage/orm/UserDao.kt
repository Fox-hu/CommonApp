package com.silverfox.storage.orm


import androidx.room.*


/**
 * The Room Magic is in this file, where you map a Java method call to an SQL query.
 *
 * When you are using complex data types, such as Date, you have to also supply type converters.
 * To keep this example basic, no types that require type converters are used.
 * See the documentation at
 * https://developer.android.com/topic/libraries/architecture/room.html#type-converters
 */

@Dao
interface UserDao {
    @Query("SELECT * FROM tbl_user")
    fun getAll(): List<User>

    @Query("SELECT * FROM tbl_user WHERE uid IN (:userIds)")
    fun loadAllByIds(vararg userIds: Int): List<User>

    @Query("SELECT * FROM tbl_user WHERE first_name LIKE :first AND second_name LIKE :second")
    fun findByName(first: String, second: String): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg user: User)

    @Delete
    fun delete(user: User)
}
