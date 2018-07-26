<?php

$con = mysql_connect("127.0.0.1","root","nmbna811992");
	mysql_select_db("press",$con);


	if(isset($_GET['uname']))
		{

$username = $_GET['uname'];

$query = mysql_query("SELECT `from` FROM `frnd_req` WHERE `to`='{$username}'");


    $response["posts"]   = array();

while($rows = mysql_fetch_array($query)){

		
    //foreach ($rows as $row) {
		$post			= array();
        $from = $rows['from'];
		//$count++;
		$post["uname"] = $from;
		//$post["reqnum"] = $count;
		
		
        
        
        //update our repsonse JSON data
        array_push($response["posts"], $post);
   // }
    
    // echoing JSON response
    
   
    
    


}
echo json_encode($response);

}


  ?>