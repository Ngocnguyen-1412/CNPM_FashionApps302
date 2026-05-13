<?php
include "database.php";$id = $_POST['id'];
$hoten = $_POST['hoten'];
$email = $_POST['email'];
$sdt = $_POST['sodienthoai'];
$vaitro = $_POST['vaitro'];

$sql = "UPDATE nguoidung SET hoten='$hoten', email='$email', sodienthoai='$sdt', vaitro='$vaitro' WHERE id=$id";
if ($conn->query($sql)) {
    echo json_encode(["status" => "success"]);
} else {
    echo json_encode(["status" => "error", "message" => $conn->error]);
}
?>