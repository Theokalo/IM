<?php

$con = mysql_connect("127.0.0.1","root","nmbna811992");
	mysql_select_db("press",$con);


	$username =$_GET['username'];
	$u_name =$_GET['u_name'];
	
	mysql_query("DELETE FROM `frnd_req` WHERE `from`='${username}' AND `to`='${u_name}'");
	$response["success"] = 1;
	$response["message"] = "Cansel";
	die(json_encode($response));
	
?>