<?php
$con = mysql_connect("127.0.0.1","root","nmbna811992");
mysql_select_db("press",$con);

	$ufrom =$_POST['ufrom'];
	$uto =$_POST['uto'];
	
	
	$check = mysql_query("SELECT * FROM notifications WHERE from_users = '{$ufrom}' AND to_users = '{$uto}'");
		if(mysql_num_rows($check) > 0){
			while($rows = mysql_fetch_array($check)){
			$from = $rows['from_users'];
			$to = $rows['to_users'];
			if($ufrom == $from && $uto == $to)
			{
				mysql_query("UPDATE notifications SET to_message='1'WHERE from_users = '{$ufrom}' AND to_users= '{$uto}'");
			}
			else{
				mysql_query("INSERT INTO notifications (from_users,to_users,to_message) VALUES ('{$ufrom}','{$uto}','1')");
			}
			}
			}
		else{
				mysql_query("INSERT INTO notifications (from_users,to_users,to_message) VALUES ('{$ufrom}','{$uto}','1')");
			}
			
?>