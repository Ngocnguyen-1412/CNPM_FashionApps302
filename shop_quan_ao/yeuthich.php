<?php
include "database.php";

$nguoidung_id = $_POST['nguoidung_id'];
$sanpham_id = $_POST['sanpham_id'];
$hanhdong = $_POST['hanhdong']; // "them" hoặc "xoa"

if ($hanhdong == "them") {
    $query = "INSERT INTO yeuthich (nguoidung_id, sanpham_id) VALUES ($nguoidung_id, $sanpham_id)";
} else {
    $query = "DELETE FROM yeuthich WHERE nguoidung_id = $nguoidung_id AND sanpham_id = $sanpham_id";
}

if (mysqli_query($conn, $query)) {
    echo json_encode(array("status" => "success", "message" => "Thành công"));
} else {
    echo json_encode(array("status" => "error", "message" => "Lỗi: " . mysqli_error($conn)));
}
?>