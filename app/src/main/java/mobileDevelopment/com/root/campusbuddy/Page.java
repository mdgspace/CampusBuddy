package mobileDevelopment.com.root.campusbuddy;

/**
 * Created by root on 22/7/15.
 */
public class Page {
    String page_name;
    boolean isSelected;

    public Page(String page_name){
        this.page_name=page_name;
        this.isSelected=false;
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
