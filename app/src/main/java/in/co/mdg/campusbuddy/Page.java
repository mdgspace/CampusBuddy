package in.co.mdg.campusbuddy;

/**
 * Created by root on 22/7/15.
 */
public class Page {
    String page_name,page_id;

    public String getPage_id() {
        return page_id;
    }

    public void setPage_id(String page_id) {
        this.page_id = page_id;
    }

    boolean isSelected= false;

    public Page(String page_name){
        this.page_name=page_name;
    }

    public Page(String page_name, String id){
        this.page_name=page_name;
        this.page_id = id;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getPage_name() {
        return page_name;
    }

    public void setPage_name(String page_name) {
        this.page_name = page_name;
    }
}
