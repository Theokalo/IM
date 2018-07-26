<?php
	
$con = mysql_connect("127.0.0.1","root","nmbna811992");
mysql_select_db("press",$con);

if(isset($_GET['search']))
		{

$search = $_GET['search'];

$query = mysql_query("SELECT username FROM users WHERE username REGEXP '{$search}' ");

if (mysql_num_rows($query) > 0) {
    $response["success"] = 1;
    $response["message"] = "Post Available!";
	//die(json_encode($response));
    $response["posts"]   = array();

while($rows = mysql_fetch_array($query,MYSQL_ASSOC)){


    //foreach ($rows as $row) {
        $post             = array();
		
        $post["username"] = $rows['username'];
       
        
        
        //update our repsonse JSON data
        array_push($response["posts"], $post);
    //}
    
    // echoing JSON response
    
   
    
    


}
echo json_encode($response);
} else {
    $response["success"] = 0;
    $response["message"] = "No Post Available!";
    die(json_encode($response));
}
}


  ?>

