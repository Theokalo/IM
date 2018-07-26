<?php
$con = mysql_connect("127.0.0.1","root","nmbna811992");
mysql_select_db("press",$con);

if(isset($_GET['ufrom']))
		{

$ufrom =$_GET['ufrom'];
$uto =$_GET['uto'];

$query = mysql_query("SELECT time,message,from_user,to_user FROM chat WHERE from_user='{$ufrom}' AND to_user='{$uto}' OR from_user='{$uto}' AND to_user='{$ufrom}'");


    $response["posts"]   = array();

while($rows = mysql_fetch_array($query)){
		$post			= array();
        $from = $rows['from_user'];
		$time = $rows['time'];
		$message = $rows['message'];
		
		
		
			$post["ufrom"] = $from;
			$post["chat"] = $message;
			$post["time"] = $time;
		
		
        //update our repsonse JSON data
        array_push($response["posts"], $post);
   // }
    
    // echoing JSON response
}
echo json_encode($response);

}

?>