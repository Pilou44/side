package com.wechantloup.side.domain.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteBean(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String)