<?php
$con = mysql_connect("127.0.0.1","root","nmbna811992");
	mysql_select_db("press",$con);
	
	$username = $_GET['uname'];
	$ufrom = $_GET['ufrom'];
	
	
	mysql_query("UPDATE notifications SET to_message='0' WHERE to_users = '{$username}' AND from_users = '{$ufrom}'");
		
		

?>