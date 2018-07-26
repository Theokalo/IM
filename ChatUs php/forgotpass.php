<?php
$con = mysql_connect("127.0.0.1","root","nmbna811992");
	mysql_select_db("press",$con);
	
	$username =$_POST['username'];
	$email =$_POST['email'];
	$password =$_POST['password'];
	$password =md5($password);

	$check = mysql_query("SELECT * FROM users WHERE username ='{$username}' AND email='{$email}'");
	if(mysql_num_rows($check) > 0){
				
				mysql_query("UPDATE users SET password ='{$password}' WHERE username='{$username}' AND email='{$email}'");
				
				$response["success"] = 1;
				$response["message"] = "Your password change Succesfully";
				die(json_encode($response));
				
				
				
			}
			else{
				$response["success"] = 0;
				$response["message"] = "Your email or Username is wrong";
				die(json_encode($response));
			}
			
?>