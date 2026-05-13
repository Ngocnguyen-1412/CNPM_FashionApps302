<?php
include "database.php";

// Lấy các banner đang hoạt động
$query = "SELECT * FROM banner WHERE trangthai = 1 ORDER BY id DESC";
$result = mysqli_query($conn, $query);

$banners = array();
while ($row = mysqli_fetch_assoc($result)) {
    $banners[] = $row;
}

header('Content-Type: application/json');
echo json_encode($banners);
?>