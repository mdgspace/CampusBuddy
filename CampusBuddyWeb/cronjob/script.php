<?php
ob_start();
require_once("config.php");
date_default_timezone_set('Asia/Kolkata');

define( 'refreshMins', 10);
$pageList = array 
(
  "Anushruti"     => "272394492879208",
  "ASHRAE"      => "754869404569818",
  "Audio Section"   => "418543801611643",
  "Cinema Club"     => "231275190406200",
  "Cinematic Section" => "100641016663545",
  "Cognizance"    => "217963184943488",
  "EDC"         => "265096099170",
  "Electronics Section" => "503218879766649",
  "Fine Arts Section" => "567441813288417",
  "General Notice Board" => "671125706342859",
  "Group For Interactive Learning" => "146825225353259",
  "IIT Roorkee"     => "415004402015833",
  "IMG"         => "353701311987",
  "MDG IITR"    => "198343570325312",
  "NCC"         => "242919515859218",
  "Photography Section" => "317158211638196",
  "Rhapsody"      => "1410660759172170",
  "Sanskriti Club"  => "420363998145999",
  "SDSLabs"       => "182484805131346",
  "SHARE IITR"    => "292035034247",
  "Team Robocon"    => "257702554250168",
  "Thomso IITR"     => "171774543014513",
  "PAG"       => "537723156291580",
  "IIT Heartbeat"   => "428712690506811",
  "WatchOut NewsAgency"=>"293084260715524",
  "Spic Macay IITR" => "247777145261376",
  "Kshitij"     => "316661941764002",
  "Geek Gazette"    => "141160935919419",
  "Music Section"=> "313076638795867",
  "Choreography and Dance section"=> "1534452020147921",
  "Dramatics Section" => "142847052545369",
  "Student Mentorship Program" => "837956583005626"
    // ,"Test Page"     => "1558962134412332"
  );

function cron () {
  require_once __DIR__ . '/vendor/autoload.php';

  $fb = new Facebook\Facebook([
    'app_id' => FB_APP_ID,
    'app_secret' => FB_APP_SECRET,
    'default_graph_version' => 'v2.7',
    'default_access_token'  => FB_APP_ID.'|'.FB_APP_SECRET
    ]);
  $newPosts = array();
  $batch = array();
  foreach ($GLOBALS['pageList'] as $page) {
    $request = $fb->request('GET', '/'.$page.'/posts');
    array_push($batch, $request);
  }
  try {
    $responses = $fb->sendBatchRequest($batch);
  } catch(Facebook\Exceptions\FacebookResponseException $e) {
      // When Graph returns an error
    echo 'Graph returned an error: ' . $e->getMessage();
    exit;
  } catch(Facebook\Exceptions\FacebookSDKException $e) {
      // When validation fails or other local issues
    echo 'Facebook SDK returned an error: ' . $e->getMessage();
    exit;
  }
  // print_r($responses);
  foreach ($responses as $response) {
    if ($response->isError()) {
      $e = $response->getThrownException();
      echo $e->getMessage()."\n\n";
    } else {
      $result = $response->getDecodedBody();
      foreach ($result['data'] as $post) {
        $created_time = strtotime($post['created_time']);
        if(time() <= ($created_time + (60 * refreshMins)))
        {
          if(!array_key_exists('message',$post))
          {
            if(array_key_exists('story', $post))
              $post['message'] = $post['story'];
            else
              $post['getTitle'] = true;
          }
          array_push($newPosts,$post);
        }
      }
    }
  }
  if(count($newPosts)>0)
  {
    $batch = array();
    foreach ($newPosts as  $post) {
      $request = $fb->request('GET', '/'.$post['id'].'/attachments');
      array_push($batch, $request);
    }
    try {
      $responses = $fb->sendBatchRequest($batch);
    } catch(Facebook\Exceptions\FacebookResponseException $e) {
        // When Graph returns an error
      echo 'Graph returned an error: ' . $e->getMessage();
      exit;
    } catch(Facebook\Exceptions\FacebookSDKException $e) {
        // When validation fails or other local issues
      echo 'Facebook SDK returned an error: ' . $e->getMessage();
      exit;
    }
    $i = 0;
    foreach ($responses as $response) {
      if ($response->isError()) {
        $e = $response->getThrownException();
        echo $e->getMessage()."\n\n";
      } else {
        $result = $response->getDecodedBody();
        foreach ($result['data'] as $data) {
          if(array_key_exists('getTitle', $newPosts[$i])) {
            if(array_key_exists('title', $data))
              $newPosts[$i]['message'] = $data['title'];
          }
          if (array_key_exists("media",$data)) {
            if(array_key_exists("image", $data['media']))
            {
              $img_src = $data['media']['image']['src'];
              $newPosts[$i]['img_src'] = $img_src;
              break;
            }
          }
        }
      }
      $i += 1;
    }

    echo count($newPosts);
    header('Connection: close');
    header('Content-Length: '.ob_get_length());
    ob_end_flush();
    ob_flush();
    flush();

    foreach ($newPosts as $post) {
      $page_id = explode('_',$post['id'])[0];
      $message = $post['message'];
      $page_name = array_search ($page_id, $GLOBALS['pageList']);
      $img_src = array_key_exists('img_src',$post)?$post['img_src']:'';
      send_notification ($page_id,$message,$page_name,$img_src);
    }
    
  }
}

function send_notification ($topic, $message, $name, $img_src)
{
  
  
  // prep the bundle
  $data = array
  (
    'content'   => $message,
    'name'    => $name,
    'img'   => $img_src
    );
  // $notification = array
  // (
        // "body" => "great match!",
        // "title" => "Portugal vs. Denmark"
  // );
  $fields = array
  (
    'to'  =>  '/topics/'.$topic,
    'data'  =>  $data
    // 'notification' => $notification
    );
  
  $headers = array
  (
    'Authorization: key=' . FCM_API_ACCESS_KEY,
    'Content-Type: application/json'
    );
  
  $ch = curl_init();
  curl_setopt( $ch,CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send' );
  curl_setopt( $ch,CURLOPT_POST, true );
  curl_setopt( $ch,CURLOPT_HTTPHEADER, $headers );
  curl_setopt( $ch,CURLOPT_RETURNTRANSFER, true );
  curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
  curl_setopt( $ch,CURLOPT_POSTFIELDS, json_encode( $fields ) );
  $result = curl_exec($ch );
  curl_close( $ch );
  echo $result;
}

// send_notification("1558962134412332","TestMessage","TestUser","https://scontent.fdel1-1.fna.fbcdn.net/v/t1.0-0/p320x320/14022150_1558964121078800_2218053903092097022_n.jpg?oh=27def6e43891837324942c86f5d87c42&oe=58532F80");
cron();
?>