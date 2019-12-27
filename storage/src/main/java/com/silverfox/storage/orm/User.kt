package com.silverfox.storage.orm

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A basic class representing an entity that is a row in a one-column database table.
 *
 * @ Entity - You must annotate the class as an entity and supply a table name if not class name.
 * @ PrimaryKey - You must identify the primary key.
 * @ ColumnInfo - You must supply the column name if it is different from the variable name.
 *
 * See the documentation for the full rich set of annotations.
 * https://developer.android.com/topic/libraries/architecture/room.html
 */

@Entity(tableName = "tbl_user")
data class User(
        @PrimaryKey(autoGenerate = true)
        val uid: Int,
        @ColumnInfo(name = "first_name")
        val firstName: String,
        @ColumnInfo(name = "second_name")
        val secondName: String
)
