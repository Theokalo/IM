<?PHP
$con = mysql_connect("127.0.0.1","root","nmbna811992");
	mysql_select_db("press",$con);


	$username =$_POST['username'];
	$password =$_POST['password'];		
	$password =md5($password);			
				
				mysql_query("UPDATE users SET actived ='0' WHERE username='{$username}' AND password ='{$password}'");



		$response["success"] = 1;
        $response["message"] = "Log-out succesfully!";
		die(json_encode($response));

?>