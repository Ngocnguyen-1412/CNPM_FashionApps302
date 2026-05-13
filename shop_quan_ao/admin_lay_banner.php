<?php
include "database.php";
$sql = "SELECT * FROM banner ORDER BY id DESC";
$result = $conn->query($sql);
$data = array();
while($row = $result->fetch_assoc()){
    $row['id'] = (int)$row['id'];
    $row['trangthai'] = (int)$row['trangthai'];
    $data[] = $row;
}
echo json_encode($data);
?>
