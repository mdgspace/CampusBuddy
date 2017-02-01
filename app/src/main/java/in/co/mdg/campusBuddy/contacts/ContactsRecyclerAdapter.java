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
class ContactsRecyclerAdapter extends RecyclerView.Adapter<ContactsRecyclerAdapter.ContactViewHolder> implements RecyclerViewFastScroller.BubbleTextGetter {
    private Realm realm = Realm.getDefaultInstance();
    private RealmResults<Group> groups;
    private RealmResults<Contact> contacts;
    private RealmList<Contact> deptContacts;
    private RealmList<Department> groupDepts;
    private int type = 1;
    private static ClickListener clicklistener;

    interface ClickListener {
        void itemClicked(int type, String contactName, String deptName);
    }

    ContactsRecyclerAdapter() {
        if (realm.isClosed()) {
            realm = Realm.getDefaultInstance();
        }
    }

    void setListData(int option, String deptName) {
        switch (option) {
            case 1:
                if (groups == null) {
                    groups = realm.where(Group.class).findAll().sort("name");
                }
                break;
            case 2:
                if (contacts == null) {
                    contacts = realm.where(Contact.class).isNotNull("profilePic").findAll().sort("name");
                }
                break;
            case 3:
                deptContacts = realm.where(Department.class).equalTo("name", deptName).findFirst().getContacts();
                break;
            case 4:
                groupDepts = realm.where(Group.class).equalTo("name", deptName).findFirst().getDepartments();
        }
        type = option;
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
                holder.bind(type, contacts.get(position));
                break;
            case 3:
                holder.bind(type, deptContacts.get(position));
                break;
            case 4:
                holder.bind(type, groupDepts.get(position));
        }
    }

    @Override
    public int getItemCount() {
        switch (type) {
            case 1:
                return groups.size();
            case 2:
                return contacts.size();
            case 3:
                return deptContacts.size();
            case 4:
                return groupDepts.size();
        }
        return 0;
    }

    @Override
    public String getTextToShowInBubble(int pos) {
        switch (type) {
            case 1:
                return groups.get(pos).getName().substring(0, 1);
            case 2:
                return contacts.get(pos).getName().substring(0, 1);
            case 3:
                return deptContacts.get(pos).getName().substring(0, 1);
            case 4:
                return groupDepts.get(pos).getName().substring(0, 1);
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
                    String str = group.getName();
//                    String[] strArray = str.split(" ");
//                    StringBuilder builder = new StringBuilder();
//                    for (String s : strArray) {
//                        String cap = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
//                        builder.append(cap + " ");
//                    }
                    name.setText(str);
                    desg.setVisibility(View.GONE);
//                    LoadingImages.loadDeptImages(dept.getPhoto(), profilePic);
                    break;
                case 2:
                case 3:
                    Contact contact = (Contact) item;
                    name.setText(contact.getName());
                    if (contact.getDesg() == null)
                        desg.setVisibility(View.GONE);
                    else {
                        desg.setVisibility(View.VISIBLE);
                        desg.setText(contact.getDesg());
                    }
                    LoadingImages.loadContactImages(contact.getProfilePic(), profilePic);
                    break;
                case 4:
                    Department dept = (Department) item;
                    str = dept.getName();
//                    strArray = str.split(" ");
//                    builder = new StringBuilder();
//                    for (String s : strArray) {
//                        String cap = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
//                        builder.append(cap + " ");
//                    }
                    name.setText(str);
                    desg.setVisibility(View.GONE);
                    LoadingImages.loadDeptImages(dept.getPhoto(), profilePic);
                    break;
            }
//            itemView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
//                        profilePic.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).start();
//                    } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
//                        profilePic.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start();
//                    }
//                    return true;
//                }
//            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type == 1) {
                        clicklistener.itemClicked(type, null, name.getText().toString());
                    } else if (type == 2 || type == 3) {
                        clicklistener.itemClicked(type, name.getText().toString(), getDept((Contact) item));
                    } else if (type == 4) {
                        clicklistener.itemClicked(type, null, name.getText().toString());
                    }

                }
            });
        }
    }
}
