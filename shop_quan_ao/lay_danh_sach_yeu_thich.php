<?php
include "database.php";

$nguoidung_id = $_GET['nguoidung_id'];

// Truy vấn kết hợp bảng yeuthich và sanpham
$query = "SELECT sanpham.* FROM yeuthich 
          JOIN sanpham ON yeuthich.sanpham_id = sanpham.id 
          WHERE yeuthich.nguoidung_id = $nguoidung_id";

$data = mysqli_query($conn, $query);
$result = array();

while ($row = mysqli_fetch_assoc($data)) {
    // Ép kiểu dữ liệu để Android không bị lỗi
    $row['id'] = (int)$row['id'];
    $row['gia'] = (double)$row['gia'];
    $row['giamgia'] = (int)$row['giamgia'];
    $row['luotxem'] = (int)$row['luotxem'];
    $result[] = $row;
}

echo json_encode($result);
?>