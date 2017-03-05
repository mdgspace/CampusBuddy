<?php
header("Content-Type: image/jpg");
if(isset($_GET["src"]))
{
    $url = $_GET["src"];
    $crop = $_GET["crop"];
    $w = $_GET["w"];
    $h = $_GET["h"];
    imagejpeg(resize_image(get_image_from_url($url),$w,$h,$crop), null, 100);
} else {
    exit;
}
function get_image_from_url($url) {
    $ch = curl_init ($url);
    curl_setopt($ch, CURLOPT_HEADER, 0);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_BINARYTRANSFER,1);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);
    $resource = curl_exec($ch);
    curl_close ($ch);
    return imagecreatefromstring($resource);
}

function resize_image($image, $w, $h, $crop=FALSE) {
    $width = imagesx($image);
    $height = imagesy($image);
    if($w<$width || $h<$height) {
        $r = $width / $height;
        if ($crop) {
            if ($width > $height) {
                $width = ceil($width-($width*abs($r-$w/$h)));
            } else {
                $height = ceil($height-($height*abs($r-$w/$h)));
            }
            $newwidth = $w;
            $newheight = $h;
        } else {
            if ($w/$h > $r) {
                $newwidth = $h*$r;
                $newheight = $h;
            } else {
                $newheight = $w/$r;
                $newwidth = $w;
            }
        }
        $dst = imagecreatetruecolor($newwidth, $newheight);
        imagecopyresampled($dst, $image, 0, 0, 0, 0, $newwidth, $newheight, $width, $height);
        return $dst;
    } else {
        return $image;
    }

}

?>