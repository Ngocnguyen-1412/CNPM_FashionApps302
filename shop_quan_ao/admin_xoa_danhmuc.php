<?php
include "database.php";
$id = (int)$_POST['id'];
$sql = "DELETE FROM danhmuc WHERE id=$id";
if ($conn->query($sql)) {
    echo json_encode(["status" => "success"]);
} else {
    echo json_encode(["status" => "error", "message" => $conn->error]);
}
?>
