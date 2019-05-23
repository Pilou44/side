package com.wechantloup.side.domain.bean

import com.google.gson.annotations.SerializedName

data class ToiletsListBean(
    @SerializedName("nhits")
    var count: Int,

    @SerializedName("records")
    var list: List<ToiletBean>
)