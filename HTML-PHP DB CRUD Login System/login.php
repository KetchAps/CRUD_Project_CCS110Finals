<?php
$email = $_POST['email'];
$password = $_POST['password'];

if (!empty($email) && !empty($password)) {
    $host = "localhost";
    $dbUsername = "root";
    $dbPassword = "";
    $dbName = "testing";

    //create connection
    $conn = new mysqli($host, $dbUsername, $dbPassword, $dbName);
    if (mysqli_connect_error()) {
        die('Connect Error('. mysqli_connect_error().')'. mysqli_connect_error());
    } else {
        $SELECT = "SELECT username, password FROM tbltesting WHERE email = ? Limit 1";
        $stmt = $conn->prepare($SELECT);
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $stmt->store_result();
        $stmt->bind_result($username, $storedPassword);
        $rnum = $stmt->num_rows;

        if ($rnum == 1) {
            $stmt->fetch();

            // Compare the submitted password directly against the stored password
            if ($password === $storedPassword) {
                echo "Welcome back, " . htmlspecialchars($username) . "! Login successful.";
                // In a real app you'd start a session here, e.g.:
                // session_start();
                // $_SESSION['username'] = $username;
            } else {
                echo "Incorrect email or password.";
            }
        } else {
            echo "Incorrect email or password.";
        }

        $stmt->close();
        $conn->close();
    }
} else {
    echo "all fields are required";
    die();
}
