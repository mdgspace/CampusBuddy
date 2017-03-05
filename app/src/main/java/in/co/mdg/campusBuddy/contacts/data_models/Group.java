package in.co.mdg.campusBuddy.contacts.data_models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Chirag on 26-01-2017.
 */

public class Group extends RealmObject{
    @SerializedName("group_name")
    private String name;
    @SerializedName("group_name_hindi")
    private String nameHindi;
    @SerializedName("depts")
    private RealmList<Department> departments;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameHindi() {
        return nameHindi;
    }

    public void setNameHindi(String nameHindi) {
        this.nameHindi = nameHindi;
    }

    public RealmList<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(RealmList<Department> departments) {
        this.departments = departments;
    }
}
