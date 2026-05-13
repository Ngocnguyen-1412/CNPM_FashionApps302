<?php
// Tắt hiển thị lỗi để không làm hỏng chuỗi JSON trả về cho App
error_reporting(0); 
header("Content-Type: application/json; charset=UTF-8");
include "database.php";

$nguoiDungId = isset($_GET['nguoidung_id']) ? $_GET['nguoidung_id'] : "";
$trangThai = isset($_GET['trangthai']) ? $_GET['trangthai'] : "";

if (empty($nguoiDungId)) {
    echo json_encode([]);
    exit;
}

$sql = "SELECT * FROM donhang WHERE nguoidung_id = '$nguoiDungId'";

// Lọc theo đúng mã Enum trong DB của bạn: 'choduyet','daxacnhan','danggiaohang','hoanthanh','dahuy'
if (!empty($trangThai) && $trangThai != "tat_ca") {
    $sql .= " AND trangthai = '$trangThai'";
}

$sql .= " ORDER BY id DESC";

$result = $conn->query($sql);
$donHangs = [];

if ($result && $result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $donHangs[] = [
            "id" => (int)$row['id'],
            "nguoidung_id" => (int)$row['nguoidung_id'],
            // Kiểm tra nếu không có cột ngaydat thì để trống, tránh gây lỗi Warning
            "ngaydat" => isset($row['ngaydat']) ? $row['ngaydat'] : "Vừa xong", 
            "tongtien" => (double)$row['tongtien'],
            "trangthai" => $row['trangthai'],
            "diachi" => $row['diachi'],
            "sodienthoai" => $row['sodienthoai'],
            "phuongthuc_thanhtoan" => isset($row['phuongthucthanhtoan']) ? $row['phuongthucthanhtoan'] : "cod",
            "hoten" => isset($row['hoten']) ? $row['hoten'] : ""
        ];
    }
}

echo json_encode($donHangs);
$conn->close();
?>