<?php
include '../db.php';
header('Content-Type: application/json');

$action = $_GET['action'] ?? '';

//Tìm kiếm sản phẩm
if ($action == 'search') {
    $keyword = $_GET['query'] ?? '';
    $stmt = $pdo->prepare("SELECT product_id, name, price, stock, category_id FROM Products WHERE name LIKE ?");
    $stmt->execute(["%$keyword%"]);
    $products = $stmt->fetchAll();

    echo json_encode(["status" => "success", "data" => $products]);
    exit;
}

//chi tiết sản phẩm
if ($action == 'detail') {
    $id = $_GET['id'] ?? 0;
    $stmt = $pdo->prepare("SELECT p.*, c.name as category_name 
                           FROM Products p 
                           LEFT JOIN Categories c ON p.category_id = c.category_id 
                           WHERE p.product_id = ?");
    $stmt->execute([$id]);
    $product = $stmt->fetch();

    if ($product) {
        echo json_encode(["status" => "success", "data" => $product]);
    } else {
        echo json_encode(["status" => "error", "message" => "Không tìm thấy sản phẩm"]);
    }
    exit;
}

echo json_encode(["status" => "error", "message" => "Thiếu tham số action hợp lệ"]);
