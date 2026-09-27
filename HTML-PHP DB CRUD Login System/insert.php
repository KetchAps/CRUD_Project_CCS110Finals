<?php
$username = $_POST['username'];
$password = $_POST['password'];
$email = $_POST['email'];

if (!empty($username) && !empty($password) && !empty($email)) {
    $host = "localhost";
    $dbUsername = "root";
    $dbPassword = "";
    $dbName = "testing";   

    //create connection
    $conn = new mysqli($host, $dbUsername, $dbPassword, $dbName);
    if (mysqli_connect_error()) {
        die('Connect Error('. mysqli_connect_error().')'. mysqli_connect_error());
    } else {
        $SELECT = "SELECT email from tbltesting WHERE email = ? Limit 1";
        $INSERT = "INSERT Into tbltesting ( username, password, email) values(?, ?, ?)";
        //prepare statement
        $stmt = $conn->prepare($SELECT);
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $stmt->bind_result($email);
        $stmt->store_result();
        $rnum = $stmt->num_rows;

        if ($rnum==0) {
            $stmt-> close();

            $stmt = $conn->prepare($INSERT);
            $stmt->bind_param("sss", $username, $password, $email);
            $stmt->execute();
            echo "New record inserted sucessfully";
        } else {
            echo "Someone already register using this email";
        }
        $stmt->close();
        $conn->close();
    }
} else {
    echo "all field are required";
    die();
}