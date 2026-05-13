<?php
$host     = "localhost";
$user     = "root";
$password = "";
$database = "shop_quanao";

$conn = mysqli_connect($host, $user, $password, $database);

if (!$conn) {
    header("Content-Type: application/json; charset=UTF-8");
    echo json_encode(["status" => "error", "message" => "Kết nối DB thất bại"]);
    exit();
}

mysqli_set_charset($conn, "utf8");
