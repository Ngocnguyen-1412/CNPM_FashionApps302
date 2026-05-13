<?php
include "database.php";

$id = $_GET['id'];

if (isset($id)) {
    $query = "SELECT * FROM sanpham WHERE id = $id";
    $data = mysqli_query($conn, $query);

    $row = mysqli_fetch_assoc($data);
    if ($row) {
        // Ép kiểu các giá trị cần thiết
        $row['id'] = (int)$row['id'];
        $row['gia'] = (double)$row['gia'];
        $row['giamgia'] = (int)$row['giamgia'];
        $row['luotxem'] = (int)$row['luotxem'];
        $row['danhmuc_id'] = (int)$row['danhmuc_id'];
        
        echo json_encode($row);
    } else {
        echo json_encode(null);
    }
}
?>
