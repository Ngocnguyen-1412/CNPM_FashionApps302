<?php
include "database.php";
$id         = (int)$_POST['id'];
$tensanpham = $_POST['tensanpham'];
$gia        = (float)$_POST['gia'];
$mota       = $_POST['mota'] ?? '';
$soluong    = (int)$_POST['soluong'];
$giamgia    = (int)($_POST['giamgia'] ?? 0);
$danhmuc_id = (int)$_POST['danhmuc_id'];
$thuonghieu = $_POST['thuonghieu'] ?? '';
$chatlieu   = $_POST['chatlieu'] ?? '';
$trangthai  = (int)($_POST['trangthai'] ?? 1);

$sql = "UPDATE sanpham SET tensanpham='$tensanpham', gia=$gia, mota='$mota', soluong=$soluong,
        giamgia=$giamgia, danhmuc_id=$danhmuc_id, thuonghieu='$thuonghieu', chatlieu='$chatlieu', trangthai=$trangthai
        WHERE id=$id";

if ($conn->query($sql)) {
    echo json_encode(["status" => "success"]);
} else {
    echo json_encode(["status" => "error", "message" => $conn->error]);
}
?>
