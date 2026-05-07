<?php
include '../db.php';
header('Content-Type: application/json');

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $input = json_decode(file_get_contents('php://input'), true);
    $user_id = $input['user_id'] ?? null;

    if (!$user_id) {
        echo json_encode(["status" => "error", "message" => "Thiếu thông tin user_id"]);
        exit;
    }

    try {
        $pdo->beginTransaction();
        $stmt_cart = $pdo->prepare("SELECT order_id FROM Orders WHERE user_id = ? AND status = 'Cart'");
        $stmt_cart->execute([$user_id]);
        $cart = $stmt_cart->fetch();

        if (!$cart) {
            throw new Exception("Giỏ hàng trống hoặc không tồn tại");
        }
        $order_id = $cart['order_id'];
        $stmt_items = $pdo->prepare("SELECT product_id, quantity FROM Order_Items WHERE order_id = ?");
        $stmt_items->execute([$order_id]);
        $items = $stmt_items->fetchAll();
        if (empty($items)) {
            throw new Exception("Không có sản phẩm nào trong giỏ hàng");
        }

        $stmt_update_stock = $pdo->prepare("UPDATE Products SET stock = stock - ? WHERE product_id = ? AND stock >= ?");
        foreach ($items as $item) {
            $stmt_update_stock->execute([$item['quantity'], $item['product_id'], $item['quantity']]);
            if ($stmt_update_stock->rowCount() == 0) {
                throw new Exception("Sản phẩm ID " . $item['product_id'] . " đã hết hàng hoặc không đủ số lượng yêu cầu");
            }
        }

        $stmt_update_status = $pdo->prepare("UPDATE Orders SET status = 'Paid', order_date = CURRENT_TIMESTAMP WHERE order_id = ?");
        $stmt_update_status->execute([$order_id]);
        $pdo->commit();
        echo json_encode(["status" => "success", "message" => "Thanh toán thành công", "order_id" => $order_id]);
    } catch (Exception $e) {
        $pdo->rollBack();
        echo json_encode(["status" => "error", "message" => $e->getMessage()]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Phương thức không được hỗ trợ"]);
}
