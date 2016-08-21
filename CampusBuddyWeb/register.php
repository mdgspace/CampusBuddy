<?php

error_reporting(E_ALL);

if(isset($_GET["Token"])){

	$token= $_GET["Token"];

	$conn = mysqli_connect("localhost","1013086","mohitsinha","1013086");

	$query = "INSERT INTO users(Token) Values ('$token') ON DUPLICATE KEY UPDATE Token= '$token';";

	mysqli_query($conn,$query);

	mysqli_close($conn);
	
}

?>