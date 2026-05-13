<?php
include "database.php";
$id        = (int)$_POST['id'];
$tieude    = $_POST['tieude'];
$hinhanh   = $_POST['hinhanh'] ?? '';
$trangthai = (int)($_POST['trangthai'] ?? 1);

$sql = "UPDATE banner SET tieude='$tieude', hinhanh='$hinhanh', trangthai=$trangthai WHERE id=$id";
if ($conn->query($sql)) {
    echo json_encode(["status" => "success"]);
} else {
    echo json_encode(["status" => "error", "message" => $conn->error]);
}
?>
