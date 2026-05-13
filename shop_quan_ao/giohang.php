<?php
// Tắt thông báo lỗi để không làm hỏng chuỗi JSON
error_reporting(0);
ini_set('display_errors', 0);
session_start();
include "database.php";

$action = isset($_REQUEST['action']) ? $_REQUEST['action'] : '';
$userId = isset($_REQUEST['nguoidung_id']) ? $_REQUEST['nguoidung_id'] : '';

if (!empty($action)) {
    header("Content-Type: application/json; charset=UTF-8");
    if (empty($userId)) {
        echo json_encode(["status" => "error", "message" => "Thiếu ID người dùng"]);
        exit;
    }

    switch($action) {
        case 'view':
            $sql = "SELECT g.*, s.tensanpham, s.gia, s.hinhanh1, s.giamgia, s.thuonghieu 
                    FROM giohang g JOIN sanpham s ON g.sanpham_id = s.id 
                    WHERE g.nguoidung_id = '$userId'";
            $result = $conn->query($sql);
            $items = [];
            while($row = $result->fetch_assoc()) {
                $items[] = [
                    "sanpham" => [
                        "id" => (int)$row['sanpham_id'],
                        "tensanpham" => $row['tensanpham'],
                        "gia" => (double)$row['gia'],
                        "hinhanh1" => $row['hinhanh1'],
                        "giamgia" => (int)$row['giamgia'],
                        "thuonghieu" => $row['thuonghieu']
                    ],
                    "soluong" => (int)$row['soluong'],
                    "size" => $row['size'],
                    "mausac" => $row['mausac']
                ];
            }
            echo json_encode($items);
            break;

        case 'add':
            $productId = $_POST['sanpham_id'];
            $soluong = isset($_POST['soluong']) ? (int)$_POST['soluong'] : 1;
            $size = $_POST['size'] ?? '';
            $mausac = $_POST['mausac'] ?? '';
            $check = "SELECT id FROM giohang WHERE nguoidung_id='$userId' AND sanpham_id='$productId' AND size='$size' AND mausac='$mausac'";
            $res = $conn->query($check);
            if ($res && $res->num_rows > 0) {
                $sql = "UPDATE giohang SET soluong = soluong + $soluong WHERE nguoidung_id='$userId' AND sanpham_id='$productId' AND size='$size' AND mausac='$mausac'";
            } else {
                $sql = "INSERT INTO giohang (nguoidung_id, sanpham_id, soluong, size, mausac) VALUES ('$userId', '$productId', '$soluong', '$size', '$mausac')";
            }
            echo $conn->query($sql) ? json_encode(["status" => "success"]) : json_encode(["status" => "error"]);
            break;

        case 'update':
            $productId = $_POST['sanpham_id'];
            $soluong = (int)$_POST['soluong'];
            $size = $_POST['size'];
            $mausac = $_POST['mausac'];
            $sql = "UPDATE giohang SET soluong = '$soluong' WHERE nguoidung_id='$userId' AND sanpham_id='$productId' AND size='$size' AND mausac='$mausac'";
            echo $conn->query($sql) ? json_encode(["status" => "success"]) : json_encode(["status" => "error"]);
            break;

        case 'delete':
            $productId = $_POST['sanpham_id'];
            $size = $_POST['size'] ?? '';
            $mausac = $_POST['mausac'] ?? '';
            $sql = "DELETE FROM giohang WHERE nguoidung_id='$userId' AND sanpham_id='$productId' AND size='$size' AND mausac='$mausac'";
            echo $conn->query($sql) ? json_encode(["status" => "success"]) : json_encode(["status" => "error"]);
            break;
    }
    $conn->close();
    exit;
}
?>
