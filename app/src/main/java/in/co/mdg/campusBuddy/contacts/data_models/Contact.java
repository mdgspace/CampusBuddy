package in.co.mdg.campusBuddy.contacts.data_models;

import io.realm.RealmObject;

/**
 * Created by Chirag on 13-06-2016.
 */

public class Contact extends RealmObject {
    private String name;
    private String designation;
    private String iitr_o;          //
    private String iitr_r;          //
    private String phoneBSNL;       //
    private String email;           //{email}@iitr.ac.in
    private String profilePic;      //http://people.iitr.ernet.in/facultyphoto/{profilePic}
    private String website;

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getDesignation() {return designation;}
    public void setDesignation(String designation) {this.designation = designation;}
    public String getIitr_o() {return iitr_o;}
    public void setIitr_o(String iitr_o) {this.iitr_o = iitr_o;}
    public String getIitr_r() {return iitr_r;}
    public void setIitr_r(String iitr_r) {this.iitr_r = iitr_r;}
    public String getPhoneBSNL() {return phoneBSNL;}
    public void setPhoneBSNL(String phoneBSNL) {this.phoneBSNL = phoneBSNL;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getProfilePic() {return profilePic;}
    public void setProfilePic(String profilePic) {this.profilePic = profilePic;}
    public String getWebsite() {return website;}
    public void setWebsite(String website) {this.website = website;}
}
