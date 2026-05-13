<?php
include "database.php";
$donhang_id = (int)$_GET['donhang_id'];

$sql = "SELECT cd.*, s.tensanpham, s.hinhanh1, s.thuonghieu
        FROM chitiet_donhang cd
        JOIN sanpham s ON cd.sanpham_id = s.id
        WHERE cd.donhang_id = $donhang_id";

$result = $conn->query($sql);
$data = array();
if ($result) {
    while($row = $result->fetch_assoc()){
        $row['id'] = (int)$row['id'];
        $row['donhang_id'] = (int)$row['donhang_id'];
        $row['sanpham_id'] = (int)$row['sanpham_id'];
        $row['soluong'] = (int)$row['soluong'];
        $row['gia'] = (float)$row['gia'];
        $data[] = $row;
    }
}
echo json_encode($data);
?>
