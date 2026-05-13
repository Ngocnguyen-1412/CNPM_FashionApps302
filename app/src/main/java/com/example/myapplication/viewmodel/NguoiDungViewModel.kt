package com.example.myapplication.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.myapplication.du_lieu.NguoiDung

class NguoiDungViewModel : ViewModel() {
    private val _nguoiDungHienTai = mutableStateOf<NguoiDung?>(null)
    val nguoiDungHienTai: State<NguoiDung?> = _nguoiDungHienTai

    fun thietLapNguoiDung(nguoiDung: NguoiDung) {
        _nguoiDungHienTai.value = nguoiDung
    }

    fun dangXuat() {
        _nguoiDungHienTai.value = null
    }
}
