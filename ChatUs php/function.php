<?php
  session_start();

	function loggedin(){

  if(isset($_SESSION['check'])&&($_SESSION['check'] )> 0){
	return true;
  }
  else{
	return false;
  }
}
?>