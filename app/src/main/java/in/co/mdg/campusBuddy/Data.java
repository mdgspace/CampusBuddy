package in.co.mdg.campusBuddy;

import java.util.ArrayList;

/**
 * Created by rc on 19/8/15.
 */
class Data {

   private static ArrayList<Page> fbPageList = new ArrayList<>();
    private static  ArrayList<ColorItem> itemList = new ArrayList<>();

    private static void init(){
        if(fbPageList == null || fbPageList.size() == 0) {
            fbPageList.add(new Page("Anushruti", "272394492879208", R.drawable.anushruti));
            fbPageList.add(new Page("ASHRAE", "754869404569818", R.drawable.ashrae));
            fbPageList.add(new Page("Audio Section", "418543801611643", R.drawable.audio));
            fbPageList.add(new Page("Cinema Club", "231275190406200", R.drawable.cinema_club));
            fbPageList.add(new Page("Cinematic Section", "100641016663545", R.drawable.cinematic));
            fbPageList.add(new Page("Cognizance", "217963184943488", R.drawable.cogni));
            fbPageList.add(new Page("EDC", "265096099170", R.drawable.edc));
            fbPageList.add(new Page("Electronics Section", "503218879766649", R.drawable.electronics_section));
            fbPageList.add(new Page("Fine Arts Section", "567441813288417", R.drawable.fine_arts));
            fbPageList.add(new Page("Geek Gazette", "141160935919419", R.drawable.gg));
            fbPageList.add(new Page("General Notice Board", "671125706342859", R.drawable.general_nb));
            fbPageList.add(new Page("Group For Interactive Learning", "146825225353259", R.drawable.interactive_learning));
            fbPageList.add(new Page("IIT Heartbeat", "428712690506811", R.drawable.iithbt));
            fbPageList.add(new Page("IIT Roorkee", "415004402015833", R.drawable.iit_roorkee));
            fbPageList.add(new Page("IMG", "353701311987", R.drawable.img));
            fbPageList.add(new Page("Kshitij - The Literary Magazine","316661941764002",R.drawable.kshitij));
            fbPageList.add(new Page("MDG, IITR", "198343570325312", R.drawable.mdg));
            fbPageList.add(new Page("NCC", "242919515859218", R.drawable.ncc));
            fbPageList.add(new Page("PAG IITR", "537723156291580", R.drawable.pag));
            fbPageList.add(new Page("Photography Section", "317158211638196", R.drawable.photography));
            fbPageList.add(new Page("Rhapsody", "1410660759172170", R.drawable.rhapsody));
            fbPageList.add(new Page("Sanskriti Club", "420363998145999", R.drawable.sanskriti));
            fbPageList.add(new Page("SDSLabs", "182484805131346", R.drawable.sds_labs));
            fbPageList.add(new Page("SHARE IITR", "292035034247", R.drawable.share));
            fbPageList.add(new Page("Spic Macay IITR","247777145261376",R.drawable.spicmacay));
            fbPageList.add(new Page("Team Robocon", "257702554250168", R.drawable.robocon));
            fbPageList.add(new Page("Thomso IITR", "171774543014513", R.drawable.thomso));
            fbPageList.add(new Page("WatchOut NewsAgency","293084260715524",R.drawable.wona));
        }
    }
    static ArrayList<Page> getFbPageList() {
        init();
        return fbPageList;
    }

static ArrayList<ColorItem> getColor_list() {
itemList.clear();
    itemList.add(new ColorItem("Default","#5c3bb5"));
    itemList.add(new ColorItem("Tomato", "#FF6347"));
    itemList.add(new ColorItem("Tangerine", "#F28500"));
    itemList.add(new ColorItem("Banana", "#FFE135"));
    itemList.add(new ColorItem("Sage", "#8B9476"));
    itemList.add(new ColorItem("Lavendar", "#B378D3"));
    itemList.add(new ColorItem("Flamingo", "#FC8EAC"));
    return itemList;
}}
