<?php
include 'database.php';

// API này nhận JSON từ App gửi lên
$data = json_decode(file_get_contents('php://input'), true);

if ($data) {
    $user_id = $data['nguoidung_id'];
    $tongtien = $data['tongtien'];
    $diachi = $data['diachi'];
    $sodienthoai = $data['sodienthoai'];
    $chitiet = $data['chitiet']; // Mảng các sản phẩm

    $sqlOrder = "INSERT INTO donhang (nguoidung_id, tongtien, diachi, sodienthoai, trangthai) 
                 VALUES ($user_id, $tongtien, '$diachi', '$sodienthoai', 'choduyet')";
    
    if(mysqli_query($conn, $sqlOrder)) {
        $donhang_id = mysqli_insert_id($conn);
        
        foreach($chitiet as $item) {
            $sp_id = $item['sanpham_id'];
            $sl = $item['soluong'];
            $gia = $item['gia'];
            $sqlDetail = "INSERT INTO chitiet_donhang (donhang_id, sanpham_id, soluong, gia) 
                          VALUES ($donhang_id, $sp_id, $sl, $gia)";
            mysqli_query($conn, $sqlDetail);
        }
        echo json_encode(["status" => "success", "message" => "Đặt hàng thành công", "order_id" => $donhang_id]);
    } else {
        echo json_encode(["status" => "error", "message" => "Không thể tạo đơn hàng"]);
    }
}
?>