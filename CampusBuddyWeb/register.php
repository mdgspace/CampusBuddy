<?php

error_reporting(E_ALL);

if(isset($_POST["Token"])){

	$token= $_POST["Token"];
	$myfile = fopen("newfile.txt", "a");
	fwrite($myfile, $token." \t ".date("Y-m-d H:i:s")."\n");
	fclose($myfile);
	echo "Success";
}
else
	die ("No Token Sent");

?>