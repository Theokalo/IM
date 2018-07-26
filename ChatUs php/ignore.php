<?php

$con = mysql_connect("127.0.0.1","root","nmbna811992");
	mysql_select_db("press",$con);


	$username =$_GET['username'];
	$u_name =$_GET['u_name'];
	
	mysql_query("DELETE FROM `frnd_req` WHERE `from`='{$u_name}' AND `to`='{$username}'");
	
	$response["success"] = 1;
	$response["message"] = "Ignore";
	die(json_encode($response));
	
?>