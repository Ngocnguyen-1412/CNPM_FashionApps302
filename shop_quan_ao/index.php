<?php
include 'database.php';

$danh_muc_id = isset($_GET['danhmuc_id']) ? $_GET['danhmuc_id'] : null;

if ($danh_muc_id) {
    $sql = "SELECT * FROM sanpham WHERE danhmuc_id = $danh_muc_id AND trangthai = 1";
} else {
    $sql = "SELECT * FROM sanpham WHERE trangthai = 1 ORDER BY ngaytao DESC";
}

$result = mysqli_query($conn, $sql);
$products = array();

while($row = mysqli_fetch_assoc($result)) {
    // Chuyển kiểu dữ liệu cho đúng định dạng số
    $row['id'] = (int)$row['id'];
    $row['gia'] = (float)$row['gia'];
    $row['soluong'] = (int)$row['soluong'];
    $row['giamgia'] = (int)$row['giamgia'];
    $products[] = $row;
}

echo json_encode($products);
?>
