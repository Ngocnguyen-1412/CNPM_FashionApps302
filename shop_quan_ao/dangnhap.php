<?php
error_reporting(E_ALL);
ini_set('display_errors', 0); // Tắt hiển thị lỗi ra output
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

$host     = "localhost";
$dbuser   = "root";
$password = "";
$database = "shop_quanao";

$conn = mysqli_connect($host, $dbuser, $password, $database);

if (!$conn) {
    echo json_encode([
        "status"  => "error",
        "message" => "DB_FAIL: " . mysqli_connect_error()
    ]);
    exit();
}

mysqli_set_charset($conn, "utf8");

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $email   = mysqli_real_escape_string($conn, $_POST['email'] ?? '');
    $matkhau = $_POST['matkhau'] ?? '';

    if (empty($email) || empty($matkhau)) {
        echo json_encode(["status" => "error", "message" => "Thiếu email hoặc mật khẩu"]);
        exit();
    }

    $sql    = "SELECT * FROM nguoidung WHERE email = '$email'";
    $result = mysqli_query($conn, $sql);

    if ($result && mysqli_num_rows($result) > 0) {
        $user = mysqli_fetch_assoc($result);
        $matkhauDB = $user['matkhau'];

        // Hỗ trợ cả plain text (dữ liệu cũ) lẫn password_hash (đăng ký mới)
        $hopLe = false;
        if (password_verify($matkhau, $matkhauDB)) {
            $hopLe = true;
        } elseif ($matkhau === $matkhauDB) {
            $hopLe = true;
        }

        if ($hopLe) {
            unset($user['matkhau']);
            $user['id'] = (int)$user['id'];
            echo json_encode($user);
        } else {
            http_response_code(401);
            echo json_encode(["status" => "error", "message" => "Sai mật khẩu"]);
        }
    } else {
        http_response_code(404);
        echo json_encode(["status" => "error", "message" => "Email không tồn tại"]);
    }
} else {
    http_response_code(405);
    echo json_encode(["status" => "error", "message" => "Phương thức không hợp lệ"]);
}
?>
