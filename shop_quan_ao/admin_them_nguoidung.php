<?php
include "database.php";
$hoten = $_POST['hoten'];
$email = $_POST['email'];
$sdt = $_POST['sodienthoai'];
$mk = password_hash($_POST['matkhau'], PASSWORD_DEFAULT);
$vaitro = $_POST['vaitro'];

$sql = "INSERT INTO nguoidung (hoten, email, sodienthoai, matkhau, vaitro) VALUES ('$hoten', '$email', '$sdt', '$mk', '$vaitro')";
if ($conn->query($sql)) {
    echo json_encode(["status" => "success"]);
} else {
    echo json_encode(["status" => "error", "message" => $conn->error]);
}
?>