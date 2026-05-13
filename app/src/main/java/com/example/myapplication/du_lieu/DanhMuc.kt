package com.example.myapplication.du_lieu

import com.google.gson.annotations.SerializedName

data class DanhMuc(
    @SerializedName("id") val id: Int,
    @SerializedName("tendanhmuc") val tenDanhMuc: String,
    @SerializedName("hinhanh") val hinhAnh: String?
)
