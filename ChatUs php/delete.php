<?php

$con = mysql_connect("127.0.0.1","root","nmbna811992");
	mysql_select_db("press",$con);


	$username =$_GET['username'];
	$u_name =$_GET['u_name'];
	
	mysql_query("DELETE FROM `frnds` WHERE (`user_one`='{$username}' AND `user_two`='{$u_name}') OR (`user_one`='{$u_name}' AND `user_two`='{$username}')");
	
	$response["success"] = 1;
	$response["message"] = "Delete";
	die(json_encode($response));
	
?>