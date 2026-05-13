package com.example.myapplication.du_lieu

import com.google.gson.annotations.SerializedName

data class SanPham(
    @SerializedName("id") val id: Int,
    @SerializedName("danhmuc_id") val danhMucId: Int?,
    @SerializedName("tensanpham") val tenSanPham: String,
    @SerializedName("mota") val moTa: String?,
    @SerializedName("gia") val gia: Double,
    @SerializedName("soluong") val soLuong: Int,
    @SerializedName("hinhanh1") val hinhanh1: String?,
    @SerializedName("hinhanh2") val hinhanh2: String?,
    @SerializedName("hinhanh3") val hinhanh3: String?,
    @SerializedName("thuonghieu") val thuongHieu: String?,
    @SerializedName("chatlieu") val chatlieu: String?,
    @SerializedName("giamgia") val giamGia: Int,
    @SerializedName("luotxem") val luotXem: Int = 0
)

data class BienTheSanPham(
    @SerializedName("id") val id: Int,
    @SerializedName("sanpham_id") val sanPhamId: Int,
    @SerializedName("size") val size: String?,
    @SerializedName("mausac") val mauSac: String?,
    @SerializedName("soluong") val soLuong: Int
)
