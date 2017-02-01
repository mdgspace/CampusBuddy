package in.co.mdg.campusBuddy.contacts.data_models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Chirag on 13-06-2016.
 */

public class Department extends RealmObject {
//    @PrimaryKey
    private int primary_key;
    @SerializedName("dept_name")
    private String name;
    @SerializedName("dept_name_hindi")
    private String nameHindi;
    @SerializedName("photo")
    private String photo; //full url: http://www.iitr.ac.in/departments/{photo}/assets/images/top1.jpg
    @SerializedName("contacts")
    private RealmList<Contact> contacts;

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getNameHindi() { return nameHindi; }
    public void setNameHindi(String nameHindi) { this.nameHindi = nameHindi; }
    public String getPhoto() {return photo;}
    public void setPhoto(String photo) {this.photo = photo;}
    public RealmList<Contact> getContacts() {return contacts;}
    public void setContacts(RealmList<Contact> contacts) {this.contacts = contacts;}
}
