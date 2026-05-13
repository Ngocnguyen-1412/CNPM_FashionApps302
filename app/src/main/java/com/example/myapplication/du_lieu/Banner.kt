package com.example.myapplication.du_lieu

import com.google.gson.annotations.SerializedName

data class Banner(
    @SerializedName("id") val id: Int,
    @SerializedName("tieude") val tieuDe: String,
    @SerializedName("hinhanh") val hinhAnh: String,
    @SerializedName("trangthai") val trangThai: Int
)
