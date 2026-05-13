package com.example.myapplication.mang

import com.example.myapplication.du_lieu.*
import com.google.gson.annotations.SerializedName
import retrofit2.http.*



interface GiaoDienApi {
    @GET("index.php")
    suspend fun laySanPham(@Query("danhmuc_id") danhMucId: Int? = null): List<SanPham>

    @GET("chitiet_sanpham.php")
    suspend fun layChiTietSanPham(@Query("id") id: Int): SanPham

    @GET("lay_bien_the.php")
    suspend fun layBienTheSanPham(@Query("sanpham_id") sanPhamId: Int): List<BienTheSanPham>

    @GET("lay_banner.php")
    suspend fun layBanner(): List<Banner>

    @FormUrlEncoded
    @POST("dangnhap.php")
    suspend fun dangNhap(
        @Field("email") email: String,
        @Field("matkhau") matkhau: String
    ): NguoiDung

    @GET("lay_danhmuc.php")
    suspend fun layDanhMuc(): List<DanhMuc>

    @FormUrlEncoded
    @POST("dangky.php")
    suspend fun dangKy(
        @Field("hoten") hoten: String,
        @Field("email") email: String,
        @Field("sodienthoai") sodienthoai: String,
        @Field("matkhau") matkhau: String
    ): Map<String, String>

    @POST("thanhtoan.php")
    suspend fun thanhToan(@Body donHang: Map<String, Any>): Map<String, String>

    @GET("lay_don_hang_nguoi_dung.php")
    suspend fun layDonHangNguoiDung(
        @Query("nguoidung_id") nguoiDungId: Int,
        @Query("trangthai") trangThai: String? = null
    ): List<DonHang>

    @FormUrlEncoded
    @POST("mua_ngay.php")
    suspend fun muaNgay(
        @Field("nguoidung_id") nguoiDungId: Int,
        @Field("sanpham_id") sanPhamId: Int,
        @Field("size") size: String,
        @Field("mausac") mauSac: String,
        @Field("soluong") soLuong: Int,
        @Field("tongtien") tongTien: Double,
        @Field("hoten") hoTen: String,
        @Field("sodienthoai") soDienThoai: String,
        @Field("diachi") diaChi: String,
        @Field("phuongthucthanhtoan") phuongThuc: String
    ): Map<String, String>

    // Wishlist
    @FormUrlEncoded
    @POST("yeuthich.php")
    suspend fun capNhatYeuThich(
        @Field("nguoidung_id") nguoiDungId: Int,
        @Field("sanpham_id") sanPhamId: Int,
        @Field("hanhdong") hanhDong: String 
    ): Map<String, String>

    @GET("kiemtra_yeuthich.php")
    suspend fun kiemTraYeuThich(
        @Query("nguoidung_id") nguoiDungId: Int,
        @Query("sanpham_id") sanPhamId: Int
    ): Map<String, Boolean>

    @GET("lay_danh_sach_yeu_thich.php")
    suspend fun layDanhSachYeuThich(@Query("nguoidung_id") nguoiDungId: Int): List<SanPham>

    // --- GIỎ HÀNG (giohang.php) ---
    @FormUrlEncoded
    @POST("giohang.php")
    suspend fun themVaoGioHang(
        @Field("action") action: String,
        @Field("nguoidung_id") nguoiDungId: String,
        @Field("sanpham_id") sanPhamId: String,
        @Field("soluong") soLuong: String,
        @Field("size") size: String,
        @Field("mausac") mauSac: String
    ): Map<String, String>

    @GET("giohang.php")
    suspend fun layGioHang(
        @Query("action") action: String,
        @Query("nguoidung_id") nguoiDungId: String
    ): List<ItemGioHangResponse>

    @FormUrlEncoded
    @POST("giohang.php")
    suspend fun xoaKhoiGioHang(
        @Field("action") action: String,
        @Field("nguoidung_id") nguoiDungId: String,
        @Field("sanpham_id") sanPhamId: String,
        @Field("size") size: String,
        @Field("mausac") mauSac: String
    ): Map<String, String>

    @FormUrlEncoded
    @POST("giohang.php")
    suspend fun capNhatSoLuongGioHang(
        @Field("action") action: String,
        @Field("nguoidung_id") nguoiDungId: String,
        @Field("sanpham_id") sanPhamId: String,
        @Field("soluong") soLuong: String,
        @Field("size") size: String,
        @Field("mausac") mauSac: String
    ): Map<String, String>

    // --- ĐỊA CHỈ ---
    @FormUrlEncoded
    @POST("cap_nhat_diachi.php")
    suspend fun capNhatDiaChi(
        @Field("id") id: Int,
        @Field("diachi") diachi: String
    ): Map<String, String>

    // --- ADMIN: THỐNG KÊ & ĐƠN HÀNG ---
    @GET("admin_thong_ke.php")
    suspend fun layThongKeAdmin(): ThongKeAdmin

    @GET("admin_lay_tat_ca_don_hang.php")
    suspend fun layTatCaDonHang(): List<DonHang>

    @FormUrlEncoded
    @POST("admin_cap_nhat_trang_thai_don_hang.php")
    suspend fun capNhatTrangThaiDonHang(
        @Field("donhang_id") donHangId: Int,
        @Field("trangthai") trangThai: String
    ): Map<String, String>

    @GET("admin_chitiet_donhang.php")
    suspend fun layChiTietDonHang(@Query("donhang_id") donHangId: Int): List<ChiTietDonHangItem>

    // --- ADMIN: NGƯỜI DÙNG ---
    @GET("admin_lay_nguoidung.php")
    suspend fun layTatCaNguoiDung(): List<NguoiDung>

    @FormUrlEncoded
    @POST("admin_them_nguoidung.php")
    suspend fun themNguoiDung(
        @Field("hoten") hoten: String,
        @Field("email") email: String,
        @Field("sodienthoai") sodienthoai: String,
        @Field("matkhau") matkhau: String,
        @Field("vaitro") vaitro: String
    ): Map<String, String>

    @FormUrlEncoded
    @POST("admin_sua_nguoidung.php")
    suspend fun suaNguoiDung(
        @Field("id") id: Int,
        @Field("hoten") hoten: String,
        @Field("email") email: String,
        @Field("sodienthoai") sodienthoai: String,
        @Field("vaitro") vaitro: String
    ): Map<String, String>

    @FormUrlEncoded
    @POST("admin_xoa_nguoidung.php")
    suspend fun xoaNguoiDung(@Field("id") id: Int): Map<String, String>

    // --- ADMIN: SẢN PHẨM ---
    @GET("admin_lay_sanpham.php")
    suspend fun adminLaySanPham(): List<SanPhamAdmin>

    @FormUrlEncoded
    @POST("admin_them_sanpham.php")
    suspend fun adminThemSanPham(
        @Field("tensanpham") tenSanPham: String,
        @Field("gia") gia: Double,
        @Field("mota") moTa: String,
        @Field("soluong") soLuong: Int,
        @Field("giamgia") giamGia: Int,
        @Field("danhmuc_id") danhMucId: Int,
        @Field("thuonghieu") thuongHieu: String,
        @Field("chatlieu") chatLieu: String,
        @Field("hinhanh1") hinhAnh1: String,
        @Field("hinhanh2") hinhAnh2: String,
        @Field("hinhanh3") hinhAnh3: String
    ): Map<String, String>

    @FormUrlEncoded
    @POST("admin_sua_sanpham.php")
    suspend fun adminSuaSanPham(
        @Field("id") id: Int,
        @Field("tensanpham") tenSanPham: String,
        @Field("gia") gia: Double,
        @Field("mota") moTa: String,
        @Field("soluong") soLuong: Int,
        @Field("giamgia") giamGia: Int,
        @Field("danhmuc_id") danhMucId: Int,
        @Field("thuonghieu") thuongHieu: String,
        @Field("chatlieu") chatLieu: String,
        @Field("trangthai") trangThai: Int
    ): Map<String, String>

    @FormUrlEncoded
    @POST("admin_xoa_sanpham.php")
    suspend fun adminXoaSanPham(@Field("id") id: Int): Map<String, String>

    // --- ADMIN: DANH MỤC ---
    @FormUrlEncoded
    @POST("admin_them_danhmuc.php")
    suspend fun adminThemDanhMuc(
        @Field("tendanhmuc") tenDanhMuc: String,
        @Field("hinhanh") hinhAnh: String
    ): Map<String, String>

    @FormUrlEncoded
    @POST("admin_sua_danhmuc.php")
    suspend fun adminSuaDanhMuc(
        @Field("id") id: Int,
        @Field("tendanhmuc") tenDanhMuc: String,
        @Field("hinhanh") hinhAnh: String
    ): Map<String, String>

    @FormUrlEncoded
    @POST("admin_xoa_danhmuc.php")
    suspend fun adminXoaDanhMuc(@Field("id") id: Int): Map<String, String>

    // --- ADMIN: BANNER ---
    @GET("admin_lay_banner.php")
    suspend fun adminLayBanner(): List<Banner>

    @FormUrlEncoded
    @POST("admin_them_banner.php")
    suspend fun adminThemBanner(
        @Field("tieude") tieuDe: String,
        @Field("hinhanh") hinhAnh: String,
        @Field("trangthai") trangThai: Int
    ): Map<String, String>

    @FormUrlEncoded
    @POST("admin_sua_banner.php")
    suspend fun adminSuaBanner(
        @Field("id") id: Int,
        @Field("tieude") tieuDe: String,
        @Field("hinhanh") hinhAnh: String,
        @Field("trangthai") trangThai: Int
    ): Map<String, String>

    @FormUrlEncoded
    @POST("admin_xoa_banner.php")
    suspend fun adminXoaBanner(@Field("id") id: Int): Map<String, String>
}

data class ItemGioHangResponse(
    @SerializedName("sanpham") val sanPham: SanPham,
    @SerializedName("soluong") val soLuong: Int,
    @SerializedName("size") val size: String,
    @SerializedName("mausac") val mauSac: String
)
