<?php
include "database.php";$sql = "SELECT id, hoten, email, vaitro, sodienthoai, avatar FROM nguoidung ORDER BY id DESC";
$result = $conn->query($sql);
$data = array();
while($row = $result->fetch_assoc()){
    $data[] = $row;
}
echo json_encode($data);
?>