<?php
include '../db.php';
header('Content-Type: application/json');

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $input = json_decode(file_get_contents('php://input'), true);
    $user_id = $input['user_id'] ?? null;
    $product_id = $input['product_id'] ?? null;
    $quantity = $input['quantity'] ?? 1;

    if (!$user_id || !$product_id) {
        echo json_encode(["status" => "error", "message" => "Thiếu thông tin user_id hoặc product_id"]);
        exit;
    }

    try {
        $pdo->beginTransaction();
        $stmt_price = $pdo->prepare("SELECT price FROM Products WHERE product_id = ?");
        $stmt_price->execute([$product_id]);
        $product = $stmt_price->fetch();

        if (!$product) {
            throw new Exception("Sản phẩm không tồn tại");
        }
        $price = $product['price'];
        $check_cart = $pdo->prepare("SELECT order_id FROM Orders WHERE user_id = ? AND status = 'Cart'");
        $check_cart->execute([$user_id]);
        $cart_order = $check_cart->fetch();

        if (!$cart_order) {
            $stmt_new_order = $pdo->prepare("INSERT INTO Orders (user_id, status, total_amount) VALUES (?, 'Cart', 0)");
            $stmt_new_order->execute([$user_id]);
            $order_id = $pdo->lastInsertId();
        } else {
            $order_id = $cart_order['order_id'];
        }

        $check_item = $pdo->prepare("SELECT item_id, quantity FROM Order_Items WHERE order_id = ? AND product_id = ?");
        $check_item->execute([$order_id, $product_id]);
        $existing_item = $check_item->fetch();
        if ($existing_item) {
            $stmt_update_item = $pdo->prepare("UPDATE Order_Items SET quantity = quantity + ? WHERE item_id = ?");
            $stmt_update_item->execute([$quantity, $existing_item['item_id']]);
        } else {
            $stmt_insert_item = $pdo->prepare("INSERT INTO Order_Items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)");
            $stmt_insert_item->execute([$order_id, $product_id, $quantity, $price]);
        }

        $stmt_update_total = $pdo->prepare("
            UPDATE Orders SET total_amount = (
                SELECT SUM(quantity * price) FROM Order_Items WHERE order_id = ?
            ) WHERE order_id = ?
        ");
        $stmt_update_total->execute([$order_id, $order_id]);

        $pdo->commit();
        echo json_encode(["status" => "success", "message" => "Đã cập nhật giỏ hàng", "order_id" => $order_id]);
    } catch (Exception $e) {
        $pdo->rollBack();
        echo json_encode(["status" => "error", "message" => $e->getMessage()]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Phương thức không được hỗ trợ"]);
}
