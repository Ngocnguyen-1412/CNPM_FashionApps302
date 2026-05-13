<?php
include "database.php";
$id         = (int)$_POST['id'];
$tendanhmuc = $_POST['tendanhmuc'];
$hinhanh    = $_POST['hinhanh'] ?? '';

$sql = "UPDATE danhmuc SET tendanhmuc='$tendanhmuc', hinhanh='$hinhanh' WHERE id=$id";
if ($conn->query($sql)) {
    echo json_encode(["status" => "success"]);
} else {
    echo json_encode(["status" => "error", "message" => $conn->error]);
}
?>
