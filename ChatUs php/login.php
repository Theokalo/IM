<?php
	$con = mysql_connect("127.0.0.1","root","nmbna811992");
	mysql_select_db("press",$con);
	include 'function.php';
	
	$username =$_POST['username'];
	$password =$_POST['password'];
	$password =md5($password);
	
	$check = mysql_query("SELECT * FROM users WHERE username='{$username}' AND password ='{$password}'");
	if(mysql_num_rows($check) > 0){
	mysql_query("UPDATE users SET actived='1' WHERE username= '{$username}' AND password='{$password}'");
	
		$_SESSION['check'] = true;
		$_SESSION['username']= $username;
		$_SESSION['password']= $password;
		
		$response["success"] = 1;
        $response["message"] = "Login successful!";
		die(json_encode($response));
	}
	else
	{
		$response["success"] = 0;
	    $response["message"] = "Invalid Credentials!";
		die(json_encode($response));
	}
?>