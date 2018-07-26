<?php

$con = mysql_connect("127.0.0.1","root","nmbna811992");
	mysql_select_db("press",$con);


	$username =$_GET['username'];
	$u_name =$_GET['u_name'];

if($u_name != $username){
					$check_frnd_query = mysql_query("SELECT id FROM frnds WHERE (user_one='{$username}' AND user_two='{$u_name}') OR (user_one='{$u_name}' AND user_two='{$username}')");
					if(mysql_num_rows($check_frnd_query) == 1)
					{
						$response["success"] = 1;
						$response["message"] = "Friends";
						die(json_encode($response));
						echo"Friends | <a href= 'action.php?action=delete&uname=$u_name&submit=$u_name' class='textlink'>Delete Friend</a>";
					}
					else
					{
						$from_query = mysql_query("SELECT `id` FROM `frnd_req` WHERE `from`='{$u_name}' AND `to`='{$username}'")or die(mysql_error());;
						$to_query = mysql_query("SELECT `id` FROM `frnd_req` WHERE `from`='{$username}' AND `to`='${u_name}'")or die(mysql_error());;
						if(mysql_num_rows($from_query) == 1)
						{
							$response["success"] = 2;
							$response["message"] = "accept";
							die(json_encode($response));
							echo "<a href= 'action.php?action=accept&uname=$u_name&submit=$u_name' class='textlink'>Accept</a> | <a href= 'action.php?action=ignore&uname=$u_name&submit=$u_name' class='textlink'>Ignore</a>";
						}
						else if(mysql_num_rows($to_query) == 1)
						{
							$response["success"] = 3;
							$response["message"] = "cansel";
							die(json_encode($response));
							echo "<a href= 'action.php?action=cansel&uname=$u_name&submit=$u_name' class='textlink'>Cansel request</a>";
						}
						else
						{
							$response["success"] = 4;
							$response["message"] = "No Friends";
							die(json_encode($response));
							echo "<a href = 'action.php?action=send&uname=$u_name&submit=$u_name' class='textlink'>send request</a>";
						}
					}
					}
				
				
?>