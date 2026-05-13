package com.example.myapplication.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.du_lieu.DanhMuc
import com.example.myapplication.du_lieu.SanPham
import com.example.myapplication.du_lieu.Banner
import com.example.myapplication.mang.KhachHangRetrofit
import kotlinx.coroutines.launch

class SanPhamViewModel : ViewModel() {

    private val _danhMucs = mutableStateOf<List<DanhMuc>>(emptyList())
    val danhMucs: State<List<DanhMuc>> = _danhMucs

    private val _sanPhams = mutableStateOf<List<SanPham>>(emptyList())
    val sanPhams: State<List<SanPham>> = _sanPhams
    
    private val _banners = mutableStateOf<List<Banner>>(emptyList())
    val banners: State<List<Banner>> = _banners
    
    private var allProducts = listOf<SanPham>()

    private val _dangTai = mutableStateOf(false)
    val dangTai: State<Boolean> = _dangTai

    private val _searchText = mutableStateOf("")
    val searchText: State<String> = _searchText

    init {
        taiDuLieu()
        taiBanner()
    }

    fun taiDuLieu() {
        viewModelScope.launch {
            _dangTai.value = true
            try {
                _danhMucs.value = KhachHangRetrofit.layGiaoDienApi.layDanhMuc()
                allProducts = KhachHangRetrofit.layGiaoDienApi.laySanPham()
                _sanPhams.value = allProducts
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _dangTai.value = false
            }
        }
    }
    
    fun taiBanner() {
        viewModelScope.launch {
            try {
                _banners.value = KhachHangRetrofit.layGiaoDienApi.layBanner()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
        _sanPhams.value = if (text.isEmpty()) {
            allProducts
        } else {
            allProducts.filter { 
                it.tenSanPham.contains(text, ignoreCase = true) || 
                it.thuongHieu?.contains(text, ignoreCase = true) == true 
            }
        }
    }

    fun laySanPhamTheoDanhMuc(danhMucId: Int?) {
        viewModelScope.launch {
            _dangTai.value = true
            _searchText.value = ""
            try {
                val sp = KhachHangRetrofit.layGiaoDienApi.laySanPham(danhMucId)
                _sanPhams.value = sp
                if (danhMucId == null) allProducts = sp
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _dangTai.value = false
            }
        }
    }
}
