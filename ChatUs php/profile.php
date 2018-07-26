<?php
	$con = mysql_connect("127.0.0.1","root","nmbna811992");
	mysql_select_db("press",$con);
	include 'function.php';
	
	
	if (loggedin()){
	$name = $_SESSION['username'];
	$response["success"] = 1;
    $response["message"] = $_SESSION['username'];
	die(json_encode($response));
	}
	else
	{
		$response["success"] = 0;
	    $response["message"] = "You must log in";
		die(json_encode($response));
	}
?>