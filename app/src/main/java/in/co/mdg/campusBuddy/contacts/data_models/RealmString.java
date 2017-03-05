package in.co.mdg.campusBuddy.contacts.data_models;

import io.realm.RealmObject;

/**
 * Created by Chirag on 26-01-2017.
 */

public class RealmString extends RealmObject {
    private String number;
    public RealmString() {}
    RealmString(String number) {
        this.number = number;
    }
    public String getNumber() {
        return number;
    }
}
