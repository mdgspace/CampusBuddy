package in.co.mdg.campusbuddy;

import java.util.ArrayList;

/**
 * Created by rc on 19/8/15.
 */
public class Data {

   public  static ArrayList<Page> fbPageList = new ArrayList<>();
    public  static  ArrayList<Color_item> itemList = new ArrayList<Color_item>();

    public static final int[] images = new int[23];

    public static void init(){
        fbPageList.clear();
        fbPageList.add(new Page("Anushruti", "272394492879208"));
        fbPageList.add(new Page("ASHRAE", "754869404569818"));
        fbPageList.add(new Page("Audio Section", "418543801611643"));
        fbPageList.add(new Page("Cinema Club", "231275190406200"));
        fbPageList.add(new Page("Cinematic Section", "100641016663545"));
        fbPageList.add(new Page("Cognizance", "217963184943488"));
        fbPageList.add(new Page("EDC", "265096099170"));
        fbPageList.add(new Page("Electronics Section", "503218879766649"));
        fbPageList.add(new Page("Fine Arts Section", "567441813288417"));
        fbPageList.add(new Page("General Notice Board", "671125706342859"));
        fbPageList.add(new Page("Group For Interactive Learning, IITR", "146825225353259"));
        fbPageList.add(new Page("IIT Roorkee", "415004402015833"));
        fbPageList.add(new Page("IMG", "353701311987"));
        fbPageList.add(new Page("MDG, IITR", "198343570325312"));
        fbPageList.add(new Page("NCC", "242919515859218"));
        fbPageList.add(new Page("Photography Section", "317158211638196"));
        fbPageList.add(new Page("Rhapsody", "1410660759172170"));
        fbPageList.add(new Page("Sanskriti Club", "420363998145999"));
        fbPageList.add(new Page("SDSLabs", "182484805131346"));
        fbPageList.add(new Page("SHARE IITR", "292035034247"));
        fbPageList.add(new Page("Team Robocon", "257702554250168"));
        fbPageList.add(new Page("Thomso IITR", "171774543014513"));
        fbPageList.add(new Page("PAG IITR", "537723156291580"));
    }

    public static void initImages(){
        images[0] = R.drawable.anushruti;
        images[1] = R.drawable.ashrae;
        images[2] = R.drawable.audio;
        images[3] = R.drawable.cinema_club;
        images[4] = R.drawable.cinematic;
        images[5] = R.drawable.cogni;
        images[6] = R.drawable.edc;
        images[7] = R.drawable.electronics_section;
        images[8] = R.drawable.fine_arts;
        images[9] = R.drawable.general_nb;
        images[10] = R.drawable.interactive_learning;
        images[11] = R.drawable.iit_roorkee;
        images[12] = R.drawable.img;
        images[13] = R.drawable.mdg;
        images[14] = R.drawable.ncc;
        images[15] = R.drawable.photography;
        images[16] = R.drawable.rhapsody;
        images[17] = R.drawable.sanskriti;
        images[18] = R.drawable.sds_labs;
        images[19] = R.drawable.share;
        images[20] = R.drawable.robocon;
        images[21] = R.drawable.thomso;
        images[22] = R.drawable.pag;
    }

    public static ArrayList<Page> getFbPageList() {
        init();
        return fbPageList;
    }

    public static int[] getImages() {
        initImages();
        return images;
    }

public static ArrayList<Color_item> getColor_list() {
itemList.clear();
    itemList.add(new Color_item("Tomato", "#FF6347"));
    itemList.add(new Color_item("Tangerine", "#F28500"));
    itemList.add(new Color_item("Banana", "#FFE135"));
    itemList.add(new Color_item("Sage", "#8B9476"));
    itemList.add(new Color_item("Lavendar", "#B378D3"));
    itemList.add(new Color_item("Flamingo", "#FC8EAC"));
    return itemList;
}}
