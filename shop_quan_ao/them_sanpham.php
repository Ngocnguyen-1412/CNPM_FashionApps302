<?php
include 'database.php';

if(isset($_POST['them'])) {

    $tensanpham = $_POST['tensanpham'];
    $gia = $_POST['gia'];
    $mota = $_POST['mota'];

    $hinhanh = $_FILES['hinhanh']['name'];
    $tmp = $_FILES['hinhanh']['tmp_name'];

    move_uploaded_file($tmp,
        '../uploads/'.$hinhanh);

    $sql = "INSERT INTO sanpham(
                tensanpham,
                gia,
                mota,
                hinhanh1
            )
            VALUES(
                '$tensanpham',
                '$gia',
                '$mota',
                '$hinhanh'
            )";

    mysqli_query($conn, $sql);

    echo "Thêm sản phẩm thành công";
}
?>

<form method="POST"
      enctype="multipart/form-data">

    <input type="text"
           name="tensanpham"
           placeholder="Tên sản phẩm">

    <br><br>

    <input type="number"
           name="gia"
           placeholder="Giá">

    <br><br>

    <textarea name="mota"
              placeholder="Mô tả"></textarea>

    <br><br>

    <input type="file"
           name="hinhanh">

    <br><br>

</form>