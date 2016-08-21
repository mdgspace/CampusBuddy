<?php
error_reporting(E_ALL);

	// this is for weebhook verification
	if($_REQUEST['hub_mode'] === 'subscribe')
	{
		$challenge = $_REQUEST['hub_challenge'];
		$verify_token = $_REQUEST['hub_verify_token'];
		if ($verify_token === 'campusbuddy') {
			die($challenge);
		}
	}
	else
	{
		$myfile = fopen("newfile.txt", "a");
		fwrite($myfile, $_REQUEST['hub_mode']);
		fclose($myfile);
	}
	// $message = array("message" => " FCM PUSH NOTIFICATION TEST MESSAGE");
	// $message_status = send_notification('fuck', $message);
	// echo $message_status;

	function send_notification ($topic, $message)
	{
		$url = 'https://fcm.googleapis.com/fcm/send';

		$fields = array(
			'to' => '/topics/'.$topic,
			'data' => $message
			);

		$headers = array(
			'Authorization:key = '.FCM_API_ACCESS_KEY,
				'Content-Type: application/json'
			);
		$ch = curl_init();
       curl_setopt($ch, CURLOPT_URL, $url);
       curl_setopt($ch, CURLOPT_POST, true);
       curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
       curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
       curl_setopt ($ch, CURLOPT_SSL_VERIFYHOST, 0);  
       curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
       curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
       $result = curl_exec($ch);           
       if ($result === FALSE) {
           die('Curl failed: ' . curl_error($ch));
       }
       curl_close($ch);
       return $result;

	}

	// $conn = mysqli_connect("localhost","1013086","mohitsinha","1013086");
	// $sql = " Select Token From users";
	// $result = mysqli_query($conn,$sql);
	// $tokens = array();
	// if(mysqli_num_rows($result) > 0 ){
	// 	while ($row = mysqli_fetch_assoc($result)) {
	// 		$tokens[] = $row["Token"];
	// 	}
	// }
?>
