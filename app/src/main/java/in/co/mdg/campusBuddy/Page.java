package in.co.mdg.campusBuddy;

/**
 * Created by root on 22/7/15.
 */
class Page {
    private String page_name, page_id;
    private int imageId;
    private boolean isSelected = false;

    Page(String page_name) {
        this.page_name = page_name;
    }

    Page(String page_name, String id) {
        this.page_name = page_name;
        this.page_id = id;
    }

    Page(String page_name, String id, int image) {
        this.page_name = page_name;
        this.page_id = id;
        this.imageId = image;
    }

    boolean isSelected() {
        return isSelected;
    }
    void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
    String getPage_name() {
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
