package in.co.mdg.campusBuddy.contacts.data_models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Chirag on 13-06-2016.
 */

public class Contact extends RealmObject {
//    @PrimaryKey
//    private int primary_key;
    @SerializedName("name")
    private String name;
    @SerializedName("name_hindi")
    private String nameHindi;
    @SerializedName("desg")
    private String desg;
    @SerializedName("desgHindi")
    private String desgHindi;
    private RealmList<RealmString> iitr_o;          //
    private RealmList<RealmString> iitr_r;          //
    @SerializedName("bsnl_res")
    private RealmList<RealmString> phoneBSNL;       //
    private RealmList<RealmString> email;           //{email}@iitr.ac.in
    private String profilePic;      //http://people.iitr.ernet.in/facultyphoto/{profilePic}
    private String website;

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getDesg() {return desg;}
    public void setDesg(String desg) {this.desg = desg;}
    public RealmList<RealmString> getIitr_o() {return iitr_o;}
    public void setIitr_o(RealmList<RealmString> iitr_o) {this.iitr_o = iitr_o;}
    public RealmList<RealmString> getIitr_r() {return iitr_r;}
    public void setIitr_r(RealmList<RealmString> iitr_r) {this.iitr_r = iitr_r;}
    public RealmList<RealmString> getPhoneBSNL() {return phoneBSNL;}
    public void setPhoneBSNL(RealmList<RealmString> phoneBSNL) {this.phoneBSNL = phoneBSNL;}
    public RealmList<RealmString> getEmail() {return email;}
    public void setEmail(RealmList<RealmString> email) {this.email = email;}
    public String getProfilePic() {return profilePic;}
    public void setProfilePic(String profilePic) {this.profilePic = profilePic;}
    public String getWebsite() {return website;}
    public void setWebsite(String website) {this.website = website;}
}
