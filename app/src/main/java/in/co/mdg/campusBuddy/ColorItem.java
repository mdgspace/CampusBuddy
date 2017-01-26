package in.co.mdg.campusBuddy;

/**
 * Created by rc on 29/8/15.
 */
public class ColorItem {

    String color, hash;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public ColorItem(String color, String hash) {
        super();
        this.color = color;
        this.hash = hash;

    }


}