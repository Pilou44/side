package com.wechantloup.side.domain.bean

import androidx.room.*
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

@Entity(tableName = "toilets")
data class ToiletsBean(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("recordid")
    var id: String,

    @Embedded
    @SerializedName("fields")
    var fields: FieldsBean
) {
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
    )

    fun getPosition(): LatLng {
        return LatLng(fields.location[0], fields.location[1])
    }

    fun getAddress(): String {
        var address = ""
        fields.number?.let { address += fields.number + ", " }
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