<?php
$con = mysql_connect("127.0.0.1","root","nmbna811992");
	mysql_select_db("press",$con);
	
	$ufrom =$_POST['ufrom'];
	$uto =$_POST['uto'];
	$emess =$_POST['emess'];
	
	
	mysql_query("INSERT INTO chat (from_user ,to_user ,message, time) VALUES ( '{$ufrom}', '{$uto}', '{$emess}', NOW())");
	
		$response["success"] = 1;
				$response["message"] = "Message Successfully Send!";
				die(json_encode($response));
	

?>