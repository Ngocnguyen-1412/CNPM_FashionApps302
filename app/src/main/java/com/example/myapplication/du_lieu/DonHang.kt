package com.example.myapplication.du_lieu

import com.google.gson.annotations.SerializedName

data class DonHang(
    @SerializedName("id") val id: Int,
    @SerializedName("nguoidung_id") val nguoiDungId: Int,
    @SerializedName("ngaydat") val ngayDat: String? = null,
    @SerializedName("tongtien") val tongTien: Double,
    @SerializedName("trangthai") val trangThai: String,
    @SerializedName("diachi") val diaChi: String? = null,
    @SerializedName("sodienthoai") val soDienThoai: String? = null,
    @SerializedName("phuongthuc_thanhtoan") val phuongThucThanhToan: String? = null,
    @SerializedName("hoten") val hoTen: String? = null
)

data class StatusResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String? = null
)

data class BieuDoCot(
    @SerializedName("nhan") val nhan: String,
    @SerializedName("gia_tri") val giaTri: Float
)

data class BieuDoTron(
    @SerializedName("ten") val ten: String,
    @SerializedName("so_luong") val soLuong: Double,  // dùng Double để chứa cả tiền lẫn số lượng
    @SerializedName("phan_tram") val phanTram: Float
)

data class ThongKeAdmin(
    @SerializedName("tong_doanh_thu") val tongDoanhThu: Double,
    @SerializedName("tong_don_hang") val tongDonHang: Int,
    @SerializedName("tong_san_pham") val tongSanPham: Int,
    @SerializedName("tong_nguoi_dung") val tongNguoiDung: Int,
    @SerializedName("don_hang_moi") val donHangMoi: List<DonHang>,
    @SerializedName("du_lieu_bieu_do_cot") val duLieuBieuDoCot: List<BieuDoCot> = emptyList(),
    @SerializedName("du_lieu_bieu_do_tron") val duLieuBieuDoTron: List<BieuDoTron> = emptyList(),
    @SerializedName("tong_doanh_thu_7ngay") val tongDoanhThu7Ngay: Double = 0.0
)
