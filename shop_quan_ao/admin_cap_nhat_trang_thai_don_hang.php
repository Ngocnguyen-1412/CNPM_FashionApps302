<?php
include "database.php";

$donhang_id = (int)$_POST['donhang_id'];
$trangthai  = $_POST['trangthai'];

// Chỉ cho phép các giá trị hợp lệ theo enum trong DB
$hopLe = ['choduyet', 'daxacnhan', 'danggiaohang', 'hoanthanh', 'dahuy'];
if (!in_array($trangthai, $hopLe)) {
    echo json_encode(["status" => "error", "message" => "Trạng thái không hợp lệ: $trangthai"]);
    exit();
}

// Cập nhật trạng thái đơn hàng
$sql = "UPDATE donhang SET trangthai = '$trangthai' WHERE id = $donhang_id";
if ($conn->query($sql) !== TRUE) {
    echo json_encode(["status" => "error", "message" => $conn->error]);
    exit();
}

// Nếu hoàn thành → tự động cập nhật thanh toán thành dathanhtoan
if ($trangthai === 'hoanthanh') {
    $sqlTT = "UPDATE donhang SET trangthaithanhToan = 'dathanhtoan' WHERE id = $donhang_id";
    $conn->query($sqlTT);
}

echo json_encode(["status" => "success"]);
?>
