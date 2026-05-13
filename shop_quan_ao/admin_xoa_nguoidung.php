<?php
include "database.php";
$id = $_POST['id'];
// Chú ý: Nên kiểm tra xem người dùng có đơn hàng không trước khi xóa
$sql = "DELETE FROM nguoidung WHERE id=$id";
if ($conn->query($sql)) {
    echo json_encode(["status" => "success"]);
} else {
    echo json_encode(["status" => "error", "message" => $conn->error]);
}
?>