<?php
include "database.php";
$tensanpham = $_POST['tensanpham'];
$gia        = (float)$_POST['gia'];
$mota       = $_POST['mota'] ?? '';
$soluong    = (int)$_POST['soluong'];
$giamgia    = (int)($_POST['giamgia'] ?? 0);
$danhmuc_id = (int)$_POST['danhmuc_id'];
$thuonghieu = $_POST['thuonghieu'] ?? '';
$chatlieu   = $_POST['chatlieu'] ?? '';
$hinhanh1   = $_POST['hinhanh1'] ?? '';
$hinhanh2   = $_POST['hinhanh2'] ?? '';
$hinhanh3   = $_POST['hinhanh3'] ?? '';

$sql = "INSERT INTO sanpham (tensanpham, gia, mota, soluong, giamgia, danhmuc_id, thuonghieu, chatlieu, hinhanh1, hinhanh2, hinhanh3, trangthai)
        VALUES ('$tensanpham', $gia, '$mota', $soluong, $giamgia, $danhmuc_id, '$thuonghieu', '$chatlieu', '$hinhanh1', '$hinhanh2', '$hinhanh3', 1)";

if ($conn->query($sql)) {
    echo json_encode(["status" => "success", "id" => (int)$conn->insert_id]);
} else {
    echo json_encode(["status" => "error", "message" => $conn->error]);
}
?>
