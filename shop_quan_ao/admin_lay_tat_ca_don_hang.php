<?php
include "database.php";
$sql = "SELECT d.*, n.hoten FROM donhang d JOIN nguoidung n ON d.nguoidung_id = n.id ORDER BY d.id DESC";
$result = $conn->query($sql);
$data = array();
while($row = $result->fetch_assoc()){
    $data[] = $row;
}
echo json_encode($data);
?>
