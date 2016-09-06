package in.co.mdg.campusBuddy.fb;

/**
 * Created by root on 22/7/15.
 */
public class Page {
    private String page_name, page_id;
    private int imageId;
    private boolean isSelected = false;

    public Page(String page_name, String id, int image) {
        this.page_name = page_name;
        this.page_id = id;
        this.imageId = image;
    }
    public Page(String page_name, String id, int image,boolean isSelected) {
        this.page_name = page_name;
        this.page_id = id;
        this.imageId = image;
        this.isSelected = isSelected;
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
    String getPage_id() {
        return page_id;
    }
    public void setPage_id(String page_id) {
        this.page_id = page_id;
    }
    public int getImageId() {
        return imageId;
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
