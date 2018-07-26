<?php
	
$con = mysql_connect("127.0.0.1","root","nmbna811992");
mysql_select_db("press",$con);

if(isset($_GET['uname']))
		{

$username = $_GET['uname'];

$query = mysql_query("SELECT DISTINCT user_one,user_two FROM frnds WHERE user_one='{$username}' OR user_two='{$username}'");


    $response["posts"]   = array();

while($rows = mysql_fetch_array($query)){

		
    //foreach ($rows as $row) {
		$post			= array();
        $user_one = $rows['user_one'];
		$user_two = $rows['user_two'];
		//$to_message = $rows['to_message'];
		
		/*echo var_dump(is_string($username));
		echo var_dump(is_string($user_one));
		echo var_dump($username == $user_one);*/
		if($username == $user_one)
		{
			$post["uname"] = $user_two;
			//$post["nmess"] = $to_message;
			
				
			
		}
		else
		{
			$post["uname"] = $user_one;
			//$post["nmess"] = $to_message;
			
		}
		
		
        
        
        //update our repsonse JSON data
        array_push($response["posts"], $post);
   // }
    
    // echoing JSON response
    
   
    
    


}
echo json_encode($response);

}


  ?>