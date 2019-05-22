package com.wechantloup.side.domain.bean

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

data class ToiletsBean(
    @SerializedName("recordid")
    var id: String,

    @SerializedName("fields")
    var fields: FieldsBean
) {
    data class FieldsBean (
        @SerializedName("arrondissement")
        var borough: Int,

        @SerializedName("gestionnaire")
        var administrator: String,

        @SerializedName("horaires_ouverture")
        var openingTime: String,

        @SerializedName("nom_de_voie")
        var street: String,

        @SerializedName("numero_de_voie")
        var number: String,

        @SerializedName("geom_x_y")
        var location: ArrayList<Double>
    )

    fun getPosition(): LatLng {
        return LatLng(fields.location[0], fields.location[1])
    }
}