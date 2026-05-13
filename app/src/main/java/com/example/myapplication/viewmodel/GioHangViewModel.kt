package com.example.myapplication.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.du_lieu.SanPham
import com.example.myapplication.mang.KhachHangRetrofit
import kotlinx.coroutines.launch

data class ItemGioHang(
    val sanPham: SanPham,
    var soLuong: Int,
    val size: String,
    val mauSac: String
)

class GioHangViewModel : ViewModel() {
    private val _danhSachGioHang = mutableStateListOf<ItemGioHang>()
    val danhSachGioHang: List<ItemGioHang> = _danhSachGioHang

    private val _dangTai = mutableStateOf(false)
    val dangTai: State<Boolean> = _dangTai

    fun layGioHang(userId: Int) {
        if (userId == 0) return
        viewModelScope.launch {
            _dangTai.value = true
            try {
                val response = KhachHangRetrofit.layGiaoDienApi.layGioHang(
                    action = "view",
                    nguoiDungId = userId.toString()
                )
                _danhSachGioHang.clear()
                _danhSachGioHang.addAll(response.map { 
                    ItemGioHang(it.sanPham, it.soLuong, it.size, it.mauSac) 
                })
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _dangTai.value = false
            }
        }
    }

    fun themVaoGio(userId: Int, sanPhamId: Int, size: String, mauSac: String) {
        viewModelScope.launch {
            try {
                KhachHangRetrofit.layGiaoDienApi.themVaoGioHang(
                    action = "add",
                    nguoiDungId = userId.toString(),
                    sanPhamId = sanPhamId.toString(),
                    soLuong = "1",
                    size = size,
                    mauSac = mauSac
                )
                layGioHang(userId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun xoaKhoiGio(userId: Int, sanPhamId: Int, size: String, mauSac: String) {
        viewModelScope.launch {
            try {
                KhachHangRetrofit.layGiaoDienApi.xoaKhoiGioHang(
                    action = "delete",
                    nguoiDungId = userId.toString(),
                    sanPhamId = sanPhamId.toString(),
                    size = size,
                    mauSac = mauSac
                )
                layGioHang(userId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun capNhatSoLuong(userId: Int, sanPhamId: Int, soLuongMoi: Int, size: String, mauSac: String) {
        if (soLuongMoi <= 0) {
            xoaKhoiGio(userId, sanPhamId, size, mauSac)
            return
        }
        viewModelScope.launch {
            try {
                KhachHangRetrofit.layGiaoDienApi.capNhatSoLuongGioHang(
                    action = "update",
                    nguoiDungId = userId.toString(),
                    sanPhamId = sanPhamId.toString(),
                    soLuong = soLuongMoi.toString(),
                    size = size,
                    mauSac = mauSac
                )
                layGioHang(userId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun tinhTongTien(): Double {
        return _danhSachGioHang.sumOf { 
            val giaSauGiam = it.sanPham.gia * (1 - it.sanPham.giamGia / 100.0)
            giaSauGiam * it.soLuong 
        }
    }
    
    fun xoaHet() {
        _danhSachGioHang.clear()
    }
}
