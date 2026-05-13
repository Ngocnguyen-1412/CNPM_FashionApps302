<?php
include 'database.php';

if($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Kiểm tra xem có nhận được dữ liệu không
    $hoten = isset($_POST['hoten']) ? $_POST['hoten'] : '';
    $email = isset($_POST['email']) ? $_POST['email'] : '';
    $sodienthoai = isset($_POST['sodienthoai']) ? $_POST['sodienthoai'] : '';
    $matkhau = isset($_POST['matkhau']) ? $_POST['matkhau'] : '';

    if(empty($email) || empty($matkhau)){
        echo json_encode(["status" => "error", "message" => "Thiếu thông tin"]);
        exit();
    }

    $pass_ma_hoa = password_hash($matkhau, PASSWORD_DEFAULT);

    $checkEmail = "SELECT id FROM nguoidung WHERE email = '$email'";
    $resultCheck = mysqli_query($conn, $checkEmail);
    
    if(mysqli_num_rows($resultCheck) > 0) {
        echo json_encode(["status" => "error", "message" => "Email này đã được sử dụng"]);
    } else {
        $sql = "INSERT INTO nguoidung (hoten, email, sodienthoai, matkhau) 
                VALUES ('$hoten', '$email', '$sodienthoai', '$pass_ma_hoa')";
        
        if(mysqli_query($conn, $sql)) {
            echo json_encode(["status" => "success", "message" => "Đăng ký thành công!"]);
        } else {
            echo json_encode(["status" => "error", "message" => "Lỗi truy vấn SQL"]);
        }
    }
}