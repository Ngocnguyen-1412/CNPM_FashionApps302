<?php
include "database.php";
$tieude   = $_POST['tieude'];
$hinhanh  = $_POST['hinhanh'] ?? '';
$trangthai = (int)($_POST['trangthai'] ?? 1);

$sql = "INSERT INTO banner (tieude, hinhanh, trangthai) VALUES ('$tieude', '$hinhanh', $trangthai)";
if ($conn->query($sql)) {
    echo json_encode(["status" => "success", "id" => (int)$conn->insert_id]);
} else {
    echo json_encode(["status" => "error", "message" => $conn->error]);
}
?>
