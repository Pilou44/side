package com.wechantloup.side.domain.bean

import android.os.Parcelable
import androidx.room.*
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "toilets")
data class ToiletBean(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("recordid")
    var id: String,

    @Embedded
    @SerializedName("fields")
    var fields: FieldsBean,

    @Ignore
    var isFavorite: Boolean = false,

    @Ignore
    var distanceToMe: Double = Double.NEGATIVE_INFINITY
) : Parcelable {

    constructor(): this("id", FieldsBean(), false, Double.NEGATIVE_INFINITY)

    override fun equals(other: Any?): Boolean {
        return other is ToiletBean && other.id == id
    }

    @Parcelize
    data class FieldsBean (
        @ColumnInfo(name = "arrondissement")
        @SerializedName("arrondissement")
        var borough: Int,

        @ColumnInfo(name = "gestionnaire")
        @SerializedName("gestionnaire")
        var administrator: String,

        @ColumnInfo(name = "horaires_ouverture")
        @SerializedName("horaires_ouverture")
        var openingTime: String,

        @ColumnInfo(name = "nom_de_voie")
        @SerializedName("nom_de_voie")
        var street: String,

        @ColumnInfo(name = "numero_de_voie")
        @SerializedName("numero_de_voie")
        var number: String?,

        @ColumnInfo(name = "geom_x_y")
        @SerializedName("geom_x_y")
        var location: ArrayList<Double>
    ) : Parcelable {
        constructor() : this(0, "", "", "", "", ArrayList())
    }

    fun getPosition(): LatLng {
        return LatLng(fields.location[0], fields.location[1])
    }

    fun getAddress(): String {
        var address = ""
        if (!fields.number.isNullOrEmpty()) {
            address += fields.number + ", "
        }
        address += fields.street
        return address
    }

    fun getAdministrator(): String {
        return fields.administrator
    }

    fun getOpening(): String {
        return fields.openingTime
    }
}