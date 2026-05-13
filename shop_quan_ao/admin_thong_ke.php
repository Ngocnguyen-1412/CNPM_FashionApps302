<?php
header('Content-Type: application/json; charset=utf-8');
include "database.php";

$response = array();

// 1. Tổng doanh thu — chỉ tính đơn hoanthanh
$sql_revenue = "SELECT SUM(tongtien) as total FROM donhang WHERE trangthai = 'hoanthanh'";
$res_rev = $conn->query($sql_revenue);
$row_rev = $res_rev->fetch_assoc();
$response['tong_doanh_thu'] = (float)($row_rev['total'] ?? 0);

// 2. Tổng đơn hàng, Sản phẩm, Người dùng
$response['tong_don_hang']  = (int)$conn->query("SELECT COUNT(*) FROM donhang")->fetch_row()[0];
$response['tong_san_pham']  = (int)$conn->query("SELECT COUNT(*) FROM sanpham WHERE trangthai = 1")->fetch_row()[0];
$response['tong_nguoi_dung']= (int)$conn->query("SELECT COUNT(*) FROM nguoidung WHERE vaitro = 'khachhang'")->fetch_row()[0];

// 3. Đơn hàng mới nhất (5 đơn gần nhất)
$sql_new = "SELECT d.id, d.nguoidung_id, d.ngaytao as ngaydat, d.tongtien,
                   d.trangthai, d.diachi, d.sodienthoai,
                   d.phuongthucthanhtoan as phuongthuc_thanhtoan, n.hoten
            FROM donhang d
            JOIN nguoidung n ON d.nguoidung_id = n.id
            ORDER BY d.id DESC LIMIT 5";
$res_new = $conn->query($sql_new);
$new_orders = array();
while ($row = $res_new->fetch_assoc()) {
    $row['id']           = (int)$row['id'];
    $row['nguoidung_id'] = (int)$row['nguoidung_id'];
    $row['tongtien']     = (float)$row['tongtien'];
    $new_orders[] = $row;
}
$response['don_hang_moi'] = $new_orders;

// 4. Biểu đồ TRÒN — doanh thu 7 ngày gần nhất (đơn hoanthanh)
$sql_pie7 = "SELECT DATE_FORMAT(ngaytao, '%d/%m') as ten,
                    SUM(tongtien) as so_luong,
                    0 as phan_tram
             FROM donhang
             WHERE trangthai = 'hoanthanh'
               AND ngaytao >= DATE_SUB(CURDATE(), INTERVAL 6 DAY)
             GROUP BY DATE(ngaytao)
             ORDER BY DATE(ngaytao) ASC";
$res_pie7 = $conn->query($sql_pie7);
$pie7_data = array();
$tong_7ngay = 0;
if ($res_pie7) {
    while ($row = $res_pie7->fetch_assoc()) {
        $row['so_luong'] = (float)$row['so_luong'];
        $tong_7ngay += $row['so_luong'];
        $pie7_data[] = $row;
    }
}
// Tính phần trăm
foreach ($pie7_data as &$item) {
    $item['phan_tram'] = $tong_7ngay > 0 ? round(($item['so_luong'] / $tong_7ngay) * 100, 1) : 0;
}
unset($item);
$response['du_lieu_bieu_do_tron'] = $pie7_data;
$response['tong_doanh_thu_7ngay'] = (float)$tong_7ngay;

// 5. Biểu đồ CỘT — trạng thái đơn hàng (giữ lại cho biểu đồ cột)
$sql_bar = "SELECT trangthai as nhan, COUNT(*) as gia_tri
            FROM donhang
            GROUP BY trangthai
            ORDER BY FIELD(trangthai,'choduyet','daxacnhan','danggiaohang','hoanthanh','dahuy')";
$res_bar = $conn->query($sql_bar);
$bar_data = array();
if ($res_bar) {
    while ($row = $res_bar->fetch_assoc()) {
        $row['gia_tri'] = (float)$row['gia_tri'];
        $bar_data[] = $row;
    }
}
$response['du_lieu_bieu_do_cot'] = $bar_data;

echo json_encode($response);
?>
