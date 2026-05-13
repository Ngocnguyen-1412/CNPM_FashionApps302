package com.example.myapplication.du_lieu

import com.google.gson.annotations.SerializedName

data class NguoiDung(
    @SerializedName("id") val id: Int,
    @SerializedName("hoten") val hoTen: String,
    @SerializedName("email") val email: String,
    @SerializedName("sodienthoai") val soDienThoai: String?,
    @SerializedName("avatar") val avatar: String?,
    @SerializedName("diachi") val diaChi: String?,
    @SerializedName("vaitro") val vaiTro: String
)
