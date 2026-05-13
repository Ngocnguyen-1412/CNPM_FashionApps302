package com.example.myapplication.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.du_lieu.*
import com.example.myapplication.mang.KhachHangRetrofit
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {
    private val api = KhachHangRetrofit.layGiaoDienApi

    // ── Loading & Toast ──────────────────────────────────────────────────────
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _toastMessage = mutableStateOf<String?>(null)
    val toastMessage: State<String?> = _toastMessage

    fun clearToast() { _toastMessage.value = null }

    // ── Thống kê ─────────────────────────────────────────────────────────────
    private val _thongKe = mutableStateOf<ThongKeAdmin?>(null)
    val thongKe: State<ThongKeAdmin?> = _thongKe

    // ── Đơn hàng ─────────────────────────────────────────────────────────────
    private val _tatCaDonHang = mutableStateOf<List<DonHang>>(emptyList())
    val tatCaDonHang: State<List<DonHang>> = _tatCaDonHang

    private val _chiTietDonHang = mutableStateOf<List<ChiTietDonHangItem>>(emptyList())
    val chiTietDonHang: State<List<ChiTietDonHangItem>> = _chiTietDonHang

    // ── Người dùng ───────────────────────────────────────────────────────────
    private val _tatCaNguoiDung = mutableStateOf<List<NguoiDung>>(emptyList())
    val tatCaNguoiDung: State<List<NguoiDung>> = _tatCaNguoiDung

    // ── Sản phẩm ─────────────────────────────────────────────────────────────
    private val _tatCaSanPham = mutableStateOf<List<SanPhamAdmin>>(emptyList())
    val tatCaSanPham: State<List<SanPhamAdmin>> = _tatCaSanPham

    // ── Danh mục ─────────────────────────────────────────────────────────────
    private val _tatCaDanhMuc = mutableStateOf<List<DanhMuc>>(emptyList())
    val tatCaDanhMuc: State<List<DanhMuc>> = _tatCaDanhMuc

    // ── Banner ───────────────────────────────────────────────────────────────
    private val _tatCaBanner = mutableStateOf<List<Banner>>(emptyList())
    val tatCaBanner: State<List<Banner>> = _tatCaBanner

    init { taiTatCa() }

    // ════════════════════════════════════════════════════════════════════════
    // LOAD ALL
    // ════════════════════════════════════════════════════════════════════════
    fun taiTatCa() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _thongKe.value       = api.layThongKeAdmin()
                _tatCaDonHang.value  = api.layTatCaDonHang()
                _tatCaNguoiDung.value = api.layTatCaNguoiDung()
                _tatCaSanPham.value  = api.adminLaySanPham()
                _tatCaDanhMuc.value  = api.layDanhMuc()
                _tatCaBanner.value   = api.adminLayBanner()
            } catch (e: Exception) {
                Log.e("AdminVM", "Lỗi tải dữ liệu: ${e.message}")
                _toastMessage.value = "Lỗi kết nối server"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // ĐƠN HÀNG
    // ════════════════════════════════════════════════════════════════════════
    fun capNhatTrangThaiDonHang(donHangId: Int, trangThaiMoi: String) {
        viewModelScope.launch {
            try {
                val res = api.capNhatTrangThaiDonHang(donHangId, trangThaiMoi)
                if (res["status"] == "success") {
                    _toastMessage.value = "Cập nhật trạng thái thành công"
                    _tatCaDonHang.value = api.layTatCaDonHang()
                    _thongKe.value = api.layThongKeAdmin()
                }
            } catch (e: Exception) {
                _toastMessage.value = "Lỗi cập nhật đơn hàng"
            }
        }
    }

    fun layChiTietDonHang(donHangId: Int) {
        viewModelScope.launch {
            try {
                _chiTietDonHang.value = api.layChiTietDonHang(donHangId)
            } catch (e: Exception) {
                Log.e("AdminVM", "Lỗi chi tiết đơn: ${e.message}")
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // NGƯỜI DÙNG
    // ════════════════════════════════════════════════════════════════════════
    fun themNguoiDung(hoten: String, email: String, sdt: String, mk: String, vaitro: String) {
        viewModelScope.launch {
            try {
                val res = api.themNguoiDung(hoten, email, sdt, mk, vaitro)
                if (res["status"] == "success") {
                    _toastMessage.value = "Thêm người dùng thành công"
                    _tatCaNguoiDung.value = api.layTatCaNguoiDung()
                } else {
                    _toastMessage.value = "Lỗi: ${res["message"]}"
                }
            } catch (e: Exception) { _toastMessage.value = "Lỗi thêm người dùng" }
        }
    }

    fun suaNguoiDung(id: Int, hoten: String, email: String, sdt: String, vaitro: String) {
        viewModelScope.launch {
            try {
                val res = api.suaNguoiDung(id, hoten, email, sdt, vaitro)
                if (res["status"] == "success") {
                    _toastMessage.value = "Cập nhật người dùng thành công"
                    _tatCaNguoiDung.value = api.layTatCaNguoiDung()
                }
            } catch (e: Exception) { _toastMessage.value = "Lỗi sửa người dùng" }
        }
    }

    fun xoaNguoiDung(id: Int) {
        viewModelScope.launch {
            try {
                val res = api.xoaNguoiDung(id)
                if (res["status"] == "success") {
                    _toastMessage.value = "Đã xóa người dùng"
                    _tatCaNguoiDung.value = api.layTatCaNguoiDung()
                }
            } catch (e: Exception) { _toastMessage.value = "Lỗi xóa người dùng" }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // SẢN PHẨM
    // ════════════════════════════════════════════════════════════════════════
    fun themSanPham(
        ten: String, gia: Double, moTa: String, soLuong: Int, giamGia: Int,
        danhMucId: Int, thuongHieu: String, chatLieu: String,
        anh1: String, anh2: String, anh3: String
    ) {
        viewModelScope.launch {
            try {
                val res = api.adminThemSanPham(ten, gia, moTa, soLuong, giamGia, danhMucId, thuongHieu, chatLieu, anh1, anh2, anh3)
                if (res["status"] == "success") {
                    _toastMessage.value = "Thêm sản phẩm thành công"
                    _tatCaSanPham.value = api.adminLaySanPham()
                } else {
                    _toastMessage.value = "Lỗi: ${res["message"]}"
                }
            } catch (e: Exception) { _toastMessage.value = "Lỗi thêm sản phẩm" }
        }
    }

    fun suaSanPham(
        id: Int, ten: String, gia: Double, moTa: String, soLuong: Int, giamGia: Int,
        danhMucId: Int, thuongHieu: String, chatLieu: String, trangThai: Int
    ) {
        viewModelScope.launch {
            try {
                val res = api.adminSuaSanPham(id, ten, gia, moTa, soLuong, giamGia, danhMucId, thuongHieu, chatLieu, trangThai)
                if (res["status"] == "success") {
                    _toastMessage.value = "Cập nhật sản phẩm thành công"
                    _tatCaSanPham.value = api.adminLaySanPham()
                }
            } catch (e: Exception) { _toastMessage.value = "Lỗi sửa sản phẩm" }
        }
    }

    fun xoaSanPham(id: Int) {
        viewModelScope.launch {
            try {
                val res = api.adminXoaSanPham(id)
                if (res["status"] == "success") {
                    _toastMessage.value = "Đã ẩn sản phẩm"
                    _tatCaSanPham.value = api.adminLaySanPham()
                }
            } catch (e: Exception) { _toastMessage.value = "Lỗi xóa sản phẩm" }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // DANH MỤC
    // ════════════════════════════════════════════════════════════════════════
    fun themDanhMuc(ten: String, hinhAnh: String) {
        viewModelScope.launch {
            try {
                val res = api.adminThemDanhMuc(ten, hinhAnh)
                if (res["status"] == "success") {
                    _toastMessage.value = "Thêm danh mục thành công"
                    _tatCaDanhMuc.value = api.layDanhMuc()
                }
            } catch (e: Exception) { _toastMessage.value = "Lỗi thêm danh mục" }
        }
    }

    fun suaDanhMuc(id: Int, ten: String, hinhAnh: String) {
        viewModelScope.launch {
            try {
                val res = api.adminSuaDanhMuc(id, ten, hinhAnh)
                if (res["status"] == "success") {
                    _toastMessage.value = "Cập nhật danh mục thành công"
                    _tatCaDanhMuc.value = api.layDanhMuc()
                }
            } catch (e: Exception) { _toastMessage.value = "Lỗi sửa danh mục" }
        }
    }

    fun xoaDanhMuc(id: Int) {
        viewModelScope.launch {
            try {
                val res = api.adminXoaDanhMuc(id)
                if (res["status"] == "success") {
                    _toastMessage.value = "Đã xóa danh mục"
                    _tatCaDanhMuc.value = api.layDanhMuc()
                }
            } catch (e: Exception) { _toastMessage.value = "Lỗi xóa danh mục" }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // BANNER
    // ════════════════════════════════════════════════════════════════════════
    fun themBanner(tieuDe: String, hinhAnh: String, trangThai: Int) {
        viewModelScope.launch {
            try {
                val res = api.adminThemBanner(tieuDe, hinhAnh, trangThai)
                if (res["status"] == "success") {
                    _toastMessage.value = "Thêm banner thành công"
                    _tatCaBanner.value = api.adminLayBanner()
                }
            } catch (e: Exception) { _toastMessage.value = "Lỗi thêm banner" }
        }
    }

    fun suaBanner(id: Int, tieuDe: String, hinhAnh: String, trangThai: Int) {
        viewModelScope.launch {
            try {
                val res = api.adminSuaBanner(id, tieuDe, hinhAnh, trangThai)
                if (res["status"] == "success") {
                    _toastMessage.value = "Cập nhật banner thành công"
                    _tatCaBanner.value = api.adminLayBanner()
                }
            } catch (e: Exception) { _toastMessage.value = "Lỗi sửa banner" }
        }
    }

    fun xoaBanner(id: Int) {
        viewModelScope.launch {
            try {
                val res = api.adminXoaBanner(id)
                if (res["status"] == "success") {
                    _toastMessage.value = "Đã xóa banner"
                    _tatCaBanner.value = api.adminLayBanner()
                }
            } catch (e: Exception) { _toastMessage.value = "Lỗi xóa banner" }
        }
    }
}
