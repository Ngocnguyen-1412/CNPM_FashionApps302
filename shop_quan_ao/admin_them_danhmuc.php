<?php
include "database.php";
$tendanhmuc = $_POST['tendanhmuc'];
$hinhanh    = $_POST['hinhanh'] ?? '';

$sql = "INSERT INTO danhmuc (tendanhmuc, hinhanh) VALUES ('$tendanhmuc', '$hinhanh')";
if ($conn->query($sql)) {
    echo json_encode(["status" => "success", "id" => (int)$conn->insert_id]);
} else {
    echo json_encode(["status" => "error", "message" => $conn->error]);
}
?>
