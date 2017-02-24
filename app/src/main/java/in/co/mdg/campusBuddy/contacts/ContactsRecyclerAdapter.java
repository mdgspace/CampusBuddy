package in.co.mdg.campusBuddy.contacts;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import in.co.mdg.campusBuddy.R;
import in.co.mdg.campusBuddy.contacts.data_models.Contact;
import in.co.mdg.campusBuddy.contacts.data_models.Department;
import in.co.mdg.campusBuddy.contacts.data_models.Group;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by root on 13/6/15.
 */
public class ContactsRecyclerAdapter extends RecyclerView.Adapter<ContactsRecyclerAdapter.ContactViewHolder> implements RecyclerViewFastScroller.BubbleTextGetter {
    private Realm realm = Realm.getDefaultInstance();
    private RealmResults<Group> groups;
    private RealmList<Contact> deptContacts;
    private RealmList<Department> groupDepts;
    private int type = 1;
    public static int ENGLISH = 1;
    public static int HINDI = 2;
    private static int lang = ENGLISH;
    private static ClickListener clicklistener;

    public interface ClickListener {
        void itemClicked(int type, String contactName, String deptName, String groupName);
    }

    ContactsRecyclerAdapter() {
        if (realm.isClosed()) {
            realm = Realm.getDefaultInstance();
        }
    }

    void setListData(int option, String deptName) {
        switch (option) {
            case 1: //for showing groups
                if (groups == null) {
                    groups = realm.where(Group.class).findAll().sort("name");
                }
                break;
            case 2: //for showing departments of a group
                groupDepts = realm.where(Group.class).equalTo("name", deptName).findFirst().getDepartments();
                break;
            case 3: //for showing contacts of a dept
                deptContacts = realm.where(Department.class).equalTo("name", deptName).findFirst().getContacts();
                break;
        }
        type = option;
        notifyDataSetChanged();
    }

    void setLang(int lang) {
        ContactsRecyclerAdapter.lang = lang;
        notifyDataSetChanged();
    }

    private String getDept(Contact contact) {
        RealmQuery<Department> deptSearch = realm
                .where(Department.class)
                .equalTo("contacts.name", contact.getName());
        if (contact.getIitr_o().size() > 0)
            deptSearch.equalTo("contacts.iitr_o.number", contact.getIitr_o().get(0).getNumber());
        else if (contact.getIitr_r().size() > 0)
            deptSearch.equalTo("contacts.iitr_r.number", contact.getIitr_r().get(0).getNumber());
        else if (contact.getPhoneBSNL().size() > 0)
            deptSearch.equalTo("contacts.phoneBSNL.number", contact.getPhoneBSNL().get(0).getNumber());
        return deptSearch.findFirst().getName();
    }

    private String getGroup(Department department) {
        RealmQuery<Group> groupSearch = realm
                .where(Group.class)
                .equalTo("departments.name", department.getName());
        return groupSearch.findFirst().getName();
    }

    private String getGroup(String deptName) {
        RealmQuery<Group> groupSearch = realm
                .where(Group.class)
                .equalTo("departments.name", deptName);
        return groupSearch.findFirst().getName();
    }

    void setClickListener(ClickListener clickListener) {
        clicklistener = clickListener;
    }

    void closeRealm() {
        realm.close();
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item, parent, false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        switch (type) {
            case 1:
                holder.bind(type, groups.get(position));
                break;
            case 2:
                holder.bind(type, groupDepts.get(position));
                break;
            case 3:
                holder.bind(type, deptContacts.get(position));
        }
    }

    @Override
    public int getItemCount() {
        switch (type) {
            case 1:
                return groups.size();
            case 2:
                return groupDepts.size();
            case 3:
                return deptContacts.size();
        }
        return 0;
    }

    @Override
    public String getTextToShowInBubble(int pos) {
        switch (type) {
            case 1:
                return groups.get(pos).getName().substring(0, 1);
            case 2:
                return groupDepts.get(pos).getName().substring(0, 1);
            case 3:
                return deptContacts.get(pos).getName().substring(0, 1);
        }
        return "";
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView name, desg;
        ImageView profilePic;

        ContactViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            desg = (TextView) itemView.findViewById(R.id.desg);
            profilePic = (ImageView) itemView.findViewById(R.id.profile_pic);
        }

        void bind(final int type, final Object item) {
            Glide.clear(profilePic);
            switch (type) {
                case 1:
                    Group group = (Group) item;
                    if (lang == HINDI && group.getNameHindi() != null) {
                        name.setText(group.getNameHindi());
                    } else {
                        name.setText(group.getName());
                    }
                    desg.setVisibility(View.GONE);
//                    LoadingImages.loadDeptImages(dept.getPhoto(), profilePic);
                    break;
                case 2:
                    Department dept = (Department) item;
                    if (lang == HINDI && dept.getNameHindi() != null) {
                        name.setText(dept.getNameHindi());
                    } else {
                        name.setText(dept.getName());
                    }
                    desg.setVisibility(View.GONE);
//                    LoadingImages.loadDeptImages(dept.getPhoto(), profilePic);
                    break;
                case 3:
                    Contact contact = (Contact) item;
                    name.setText(contact.getName());
                    if (contact.getDesg() == null)
                        desg.setVisibility(View.GONE);
                    else {
                        desg.setVisibility(View.VISIBLE);
                        desg.setText(contact.getDesg());
                    }
//                    LoadingImages.loadContactImages(contact.getProfilePic(), profilePic);
                    break;
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type == 1) {
                        clicklistener.itemClicked(type, null, null, ((Group) item).getName());
                    } else if (type == 2) {
                        clicklistener.itemClicked(type, null, ((Department) item).getName(), getGroup((Department) item));
                    } else if (type == 3) {
                        Contact contact = (Contact) item;
                        String dept = getDept(contact);
                        clicklistener.itemClicked(type, contact.getName(), dept, getGroup(dept) );
                    }

                }
            });
        }
    }
}
