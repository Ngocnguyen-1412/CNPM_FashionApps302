
<?php
header('Content-Type: application/json; charset=utf-8');
include "database.php";

$nguoidung_id = $_POST['nguoidung_id'];
$sanpham_id   = $_POST['sanpham_id'];
$size         = $_POST['size'];
$mausac       = $_POST['mausac'];
$soluong      = $_POST['soluong'];
$tongtien     = $_POST['tongtien'];
$hoten        = $_POST['hoten'];
$sdt          = $_POST['sodienthoai'];
$diachi       = $_POST['diachi'];
$phuongthuc   = $_POST['phuongthucthanhtoan'];

// Bắt đầu Transaction để đảm bảo tính toàn vẹn dữ liệu
$conn->begin_transaction();

try {
    // 1. Lưu vào bảng donhang
    $sql_donhang = "INSERT INTO donhang (nguoidung_id, tongtien, hoten, sodienthoai, diachi, phuongthucthanhtoan, trangthai) 
                    VALUES ('$nguoidung_id', '$tongtien', '$hoten', '$sdt', '$diachi', '$phuongthuc', 'choduyet')";
    
    if (!$conn->query($sql_donhang)) throw new Exception("Lỗi tạo đơn hàng: " . $conn->error);
    
    $donhang_id = $conn->insert_id;

    // 2. Lưu vào bảng chitiet_donhang (nếu bạn có bảng này)
    $sql_chitiet = "INSERT INTO chitiet_donhang (donhang_id, sanpham_id, soluong, gia, size, mausac) 
                    VALUES ('$donhang_id', '$sanpham_id', '$soluong', '$tongtien', '$size', '$mausac')";
    $conn->query($sql_chitiet);

    // 3. Trừ số lượng trong bảng bien_the_sanpham (theo size và màu)
    $sql_update_variant = "UPDATE bien_the_sanpham 
                           SET soluong = soluong - $soluong 
                           WHERE sanpham_id = '$sanpham_id' AND size = '$size' AND mausac = '$mausac' AND soluong >= $soluong";
    $conn->query($sql_update_variant);
    
    if ($conn->affected_rows == 0) {
        throw new Exception("Sản phẩm phiên bản này đã hết hàng!");
    }

    // 4. Trừ tổng số lượng trong bảng sanpham
    $sql_update_product = "UPDATE sanpham SET soluong = soluong - $soluong WHERE id = '$sanpham_id'";
    $conn->query($sql_update_product);

    // Hoàn tất
    $conn->commit();
    echo json_encode(["status" => "success", "message" => "Đặt hàng thành công!"]);

} catch (Exception $e) {
    // Nếu có lỗi thì hủy các thao tác trên
    $conn->rollback();
    echo json_encode(["status" => "error", "message" => $e->getMessage()]);
}
?>
