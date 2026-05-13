<?php
include "database.php";

$id = $_POST['id'];
$diachi = $_POST['diachi'];

$query = "UPDATE nguoidung SET diachi = '$diachi' WHERE id = $id";

if (mysqli_query($conn, $query)) {
    echo json_encode(array("status" => "success", "message" => "Cập nhật thành công"));
} else {
    echo json_encode(array("status" => "error", "message" => mysqli_error($conn)));
}
?>