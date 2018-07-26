<?php
	$con = mysql_connect("127.0.0.1","root","nmbna811992");
	mysql_select_db("press",$con);
	
	$name =$_POST['name'];
	$pass1 =$_POST['pass1'];
	$pass2 =$_POST['pass2'];
	$email1 =$_POST['email1'];
	$email2 =$_POST['email2'];
	$firstname =$_POST['firstname'];
	$lastname =$_POST['lastname'];
	$pass1 = md5($pass1);
	
	
		
	
		$check = mysql_query("SELECT * FROM users WHERE username='{$name}'");
			if(mysql_num_rows($check) > 0){
				$response["success"] = 0;
				$response["message"] = "That Username already exist!";
				die(json_encode($response));
				}
				
		else{
		mysql_query("INSERT INTO users (name ,lname ,username, email, password) VALUES ( '{$firstname}', '{$lastname}', '{$name}' ,'{$email1}' ,'{$pass1}')");
		
		$response["success"] = 1;
				$response["message"] = "Username Successfully Added!";
				die(json_encode($response));
				
			}
	
	
?>