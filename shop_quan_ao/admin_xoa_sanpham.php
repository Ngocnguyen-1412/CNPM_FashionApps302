<?php
include "database.php";
$id = (int)$_POST['id'];
// Ẩn sản phẩm thay vì xóa cứng để giữ lịch sử đơn hàng
$sql = "UPDATE sanpham SET trangthai = 0 WHERE id = $id";
if ($conn->query($sql)) {
    echo json_encode(["status" => "success"]);
} else {
    echo json_encode(["status" => "error", "message" => $conn->error]);
}
?>
