package in.co.mdg.campusBuddy.contacts.data_models;


import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Chirag on 13-06-2016.
 */

public class ContactSearchModel extends RealmObject {
    private String name;
    private boolean historySearch;
    private String profilePic;
    private Date dateAdded;
    private String dept;

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getProfilePic() {return profilePic;}
    public void setProfilePic(String profilePic) {this.profilePic = profilePic;}
    public boolean isHistorySearch() {return historySearch;}
    public void setHistorySearch(boolean historySearch) {this.historySearch = historySearch;}
    public Date getDateAdded() {return dateAdded;}
    public void setDateAdded(Date dateAdded) {this.dateAdded = dateAdded;}
    public String getDept() {return dept;}
    public void setDept(String dept) {this.dept = dept;}
}
