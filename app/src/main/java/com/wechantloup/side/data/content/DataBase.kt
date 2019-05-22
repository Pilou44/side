package com.wechantloup.side.data.content

import androidx.room.*
import com.wechantloup.side.domain.bean.FavoriteBean
import com.wechantloup.side.domain.bean.ToiletsBean
import io.reactivex.Observable

@Database(entities = [ToiletsBean::class, FavoriteBean::class], version = 1, exportSchema = false)
@TypeConverters(DataBase.LocationConverter::class)
abstract class DataBase : RoomDatabase() {

    abstract fun toiletsDao(): ToiletsDao
    abstract fun favoritesDao(): FavoriteDao

    @Dao
    interface ToiletsDao {

        @Query("SELECT * FROM toilets")
        fun getAll():List<ToiletsBean>

        @Insert
        fun insertAll(resources: List<ToiletsBean>)

        @Query("DELETE FROM toilets")
        fun deleteAll(): Int

    }

    @Dao
    interface FavoriteDao {

        @Query("SELECT * FROM favorites")
        fun getAll(): List<FavoriteBean>

        @Insert
        fun insert(resource: FavoriteBean)

        @Delete
        fun delete(resource: FavoriteBean)

    }

    class LocationConverter {
        @TypeConverter
        fun fromStringToArrayListDouble(value: String): ArrayList<Double> {
            val index = value.indexOf(";")
            val sub1 = value.substring(0, index)
            val sub2 = value.substring(index + 1)
            val list = ArrayList<Double>()
            list.add(sub1.toDouble())
            list.add(sub2.toDouble())
            return list
        }

        @TypeConverter
        fun fromArrayListDoubleToString(list: ArrayList<Double>): String {
            return list[0].toString() + ";" + list[1].toString()
        }
    }
}