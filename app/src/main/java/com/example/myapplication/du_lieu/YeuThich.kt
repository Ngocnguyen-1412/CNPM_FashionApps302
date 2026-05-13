package com.example.myapplication.du_lieu

import com.google.gson.annotations.SerializedName

data class YeuThich(
    @SerializedName("id") val id: Int,
    @SerializedName("nguoidung_id") val nguoiDungId: Int,
    @SerializedName("sanpham_id") val sanPhamId: Int
)
