package in.co.mdg.campusBuddy.contacts.data_models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Chirag on 13-06-2016.
 */

public class Department extends RealmObject {
    private String name;
    private String photo; //full url: http://www.iitr.ac.in/departments/{photo}/assets/images/top1.jpg
    private RealmList<Contact> contacts;

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getPhoto() {return photo;}
    public void setPhoto(String photo) {this.photo = photo;}
    public RealmList<Contact> getContacts() {return contacts;}
    public void setContacts(RealmList<Contact> contacts) {this.contacts = contacts;}
}
