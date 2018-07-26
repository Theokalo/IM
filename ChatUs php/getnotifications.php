<?php
$con = mysql_connect("127.0.0.1","root","nmbna811992");
mysql_select_db("press",$con);

	$ufrom =$_POST['ufrom'];
	
	$check = mysql_query("SELECT from_users,to_users FROM notifications WHERE to_users = '{$ufrom}' AND to_message='1'");
		$response["notify"]   = array();
		while($rows = mysql_fetch_array($check)){
			$from = $rows['from_users'];
			$post["notify_users"] = $from;
		
				
				array_push($response["notify"], $post);
				
				}
				echo json_encode($response);
?>