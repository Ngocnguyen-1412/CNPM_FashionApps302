<?php
// Xóa mọi khoảng trắng dư thừa ở đầu file
ob_start();header('Content-Type: application/json; charset=utf-8');

// Thay đổi thông tin nếu bạn dùng ổ D cho XAMPP
include "database.php";

if (isset($_GET['sanpham_id'])) {
    $id = intval($_GET['sanpham_id']);
    $query = "SELECT * FROM bien_the_sanpham WHERE sanpham_id = $id";
    $data = mysqli_query($conn, $query);

    $result = array();
    if ($data) {
        while ($row = mysqli_fetch_assoc($data)) {
            $result[] = array(
                "id" => (int)$row['id'],
                "sanpham_id" => (int)$row['sanpham_id'],
                "size" => $row['size'],
                "mausac" => $row['mausac'], // Viết đúng tên cột mausac trong SQL của bạn
                "soluong" => (int)$row['soluong']
            );
        }
    }
    // Xóa bộ đệm để đảm bảo chỉ có JSON được xuất ra
    ob_end_clean();
    echo json_encode($result);
} else {
    ob_end_clean();
    echo json_encode([]);
}
?>