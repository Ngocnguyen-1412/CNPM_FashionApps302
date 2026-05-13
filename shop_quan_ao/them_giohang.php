<?php
include "database.php";

$nguoidung_id = $_POST['nguoidung_id'];
$sanpham_id = $_POST['sanpham_id'];
$soluong = $_POST['soluong'];

// 1. Kiểm tra xem người dùng đã có giỏ hàng chưa (dựa trên bảng giohang bạn gửi)
$check_cart = "SELECT id FROM giohang WHERE nguoidung_id = $nguoidung_id";
$res_cart = mysqli_query($conn, $check_cart);

if (mysqli_num_rows($res_cart) == 0) {
    // Nếu chưa có thì tạo mới
    mysqli_query($conn, "INSERT INTO giohang (nguoidung_id) VALUES ($nguoidung_id)");
    $giohang_id = mysqli_insert_id($conn);
} else {
    $row_cart = mysqli_fetch_assoc($res_cart);
    $giohang_id = $row_cart['id'];
}

// 2. Kiểm tra bảng chitiet_giohang (nếu bạn chưa có bảng này, hãy tạo nó trong SQL)
// CREATE TABLE chitiet_giohang (id INT AUTO_INCREMENT PRIMARY KEY, giohang_id INT, sanpham_id INT, soluong INT);

$check_item = "SELECT id, soluong FROM chitiet_giohang WHERE giohang_id = $giohang_id AND sanpham_id = $sanpham_id";
$res_item = mysqli_query($conn, $check_item);

if (mysqli_num_rows($res_item) > 0) {
    // Nếu đã có sản phẩm này thì tăng số lượng
    $row_item = mysqli_fetch_assoc($res_item);
    $new_qty = $row_item['soluong'] + $soluong;
    $sql = "UPDATE chitiet_giohang SET soluong = $new_qty WHERE id = " . $row_item['id'];
} else {
    // Nếu chưa có thì thêm mới
    $sql = "INSERT INTO chitiet_giohang (giohang_id, sanpham_id, soluong) VALUES ($giohang_id, $sanpham_id, $soluong)";
}

if (mysqli_query($conn, $sql)) {
    echo json_encode(array("status" => "success", "message" => "Đã lưu vào SQL"));
} else {
    echo json_encode(array("status" => "error", "message" => mysqli_error($conn)));
}
?>