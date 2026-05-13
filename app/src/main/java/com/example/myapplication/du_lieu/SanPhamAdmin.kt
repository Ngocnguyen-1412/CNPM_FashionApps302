package com.example.myapplication.du_lieu

import com.google.gson.annotations.SerializedName

data class SanPhamAdmin(
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
    @SerializedName("chatlieu") val chatLieu: String?,
    @SerializedName("giamgia") val giamGia: Int,
    @SerializedName("luotxem") val luotXem: Int = 0,
    @SerializedName("trangthai") val trangThai: Int = 1,
    @SerializedName("tendanhmuc") val tenDanhMuc: String? = null
)

data class ChiTietDonHangItem(
    @SerializedName("id") val id: Int,
    @SerializedName("donhang_id") val donHangId: Int,
    @SerializedName("sanpham_id") val sanPhamId: Int,
    @SerializedName("soluong") val soLuong: Int,
    @SerializedName("gia") val gia: Double,
    @SerializedName("tensanpham") val tenSanPham: String?,
    @SerializedName("hinhanh1") val hinhAnh: String?,
    @SerializedName("thuonghieu") val thuongHieu: String?
)
