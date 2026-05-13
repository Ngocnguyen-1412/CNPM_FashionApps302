<?php
include 'database.php';

$sql = "SELECT * FROM danhmuc";
$result = mysqli_query($conn, $sql);
$data = array();

while($row = mysqli_fetch_assoc($result)) {
    $row['id'] = (int)$row['id'];
    $data[] = $row;
}

echo json_encode($data);
?>