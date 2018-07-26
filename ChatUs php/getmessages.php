<?php
	$con = mysql_connect("127.0.0.1","root","nmbna811992");
	mysql_select_db("press",$con);
	
	if(isset($_GET['uname']))
		{

$username = $_GET['uname'];

$query = mysql_query("SELECT DISTINCT from_users FROM notifications WHERE to_users='{$username}' AND to_message='1'");


    $response["posts"]   = array();

while($rows = mysql_fetch_array($query)){

		
    
		$post			= array();
        $from = $rows['from_users'];
		
		
		
			$post["uname"] = $from;

        
        //update our repsonse JSON data
        array_push($response["posts"], $post);
   // }
    
    // echoing JSON response
    
   
    
    


}
echo json_encode($response);

}


  ?>