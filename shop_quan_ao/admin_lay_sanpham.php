<?php
include "database.php";
$sql = "SELECT s.*, d.tendanhmuc FROM sanpham s LEFT JOIN danhmuc d ON s.danhmuc_id = d.id ORDER BY s.id DESC";
$result = $conn->query($sql);
$data = array();
while($row = $result->fetch_assoc()){
    $row['id'] = (int)$row['id'];
    $row['gia'] = (float)$row['gia'];
    $row['soluong'] = (int)$row['soluong'];
    $row['giamgia'] = (int)$row['giamgia'];
    $row['danhmuc_id'] = (int)$row['danhmuc_id'];
    $row['luotxem'] = (int)$row['luotxem'];
    $data[] = $row;
}
echo json_encode($data);
?>
