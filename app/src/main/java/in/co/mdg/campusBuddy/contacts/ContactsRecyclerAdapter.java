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
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by root on 13/6/15.
 */
class ContactsRecyclerAdapter extends RecyclerView.Adapter<ContactsRecyclerAdapter.ContactViewHolder> implements RecyclerViewFastScroller.BubbleTextGetter {
    private static Realm realm = Realm.getDefaultInstance();
    private RealmList<Department> depts = new RealmList<>();
    private RealmResults<Contact> contacts;
    private RealmList<Contact> deptContacts;
    private int type = 1;
    private static ClickListener clicklistener;
    interface ClickListener {
        void itemClicked(int type,String contactName,String deptName);
    }
    ContactsRecyclerAdapter() {
        if(realm.isClosed()) {realm = Realm.getDefaultInstance();}
    }

    void setListData(int option, String deptName) {
        switch(option)
        {
            case 1:
                if(depts.size() == 0) {
                    RealmResults<Department> results = realm.where(Department.class).notEqualTo("name","Medical Aid").findAll().sort("name");
                    Department medicalAid = realm.where(Department.class).equalTo("name","Medical Aid").findFirst();
                    if(medicalAid != null) {
                        depts.add(0,medicalAid);
                    }
                    depts.addAll(results);
                }
                break;
            case 2:
                if(contacts==null) {contacts = realm.where(Contact.class).isNotNull("profilePic").findAll().sort("name");}
                break;
            case 3:
                deptContacts = realm.where(Department.class).equalTo("name", deptName).findFirst().getContacts();
                break;
        }
        type = option;
        notifyDataSetChanged();
    }
    private static String getDept(Contact contact)
    {
        RealmQuery<Department> deptSearch = realm
                .where(Department.class)
                .equalTo("contacts.name",contact.getName());
        if(contact.getIitr_o() != null)
            deptSearch.equalTo("contacts.iitr_o",contact.getIitr_o());
        else if(contact.getIitr_r() != null)
            deptSearch.equalTo("contacts.iitr_r",contact.getIitr_r());
        else if(contact.getPhoneBSNL() != null)
            deptSearch.equalTo("contacts.phoneBSNL",contact.getPhoneBSNL());
        return deptSearch.findFirst().getName();
    }
    void setClickListener(ClickListener clickListener)
    {
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
        switch (type)
        {
            case 1:
                holder.bind(type,depts.get(position));
                break;
            case 2:
                holder.bind(type,contacts.get(position));
                break;
            case 3:
                holder.bind(type,deptContacts.get(position));
        }
    }

    @Override
    public int getItemCount() {
        switch(type)
        {
            case 1:
                return depts.size();
            case 2:
                return contacts.size();
            case 3:
                return deptContacts.size();
        }
        return 0;
    }

    @Override
    public String getTextToShowInBubble(int pos) {
        switch(type)
        {
            case 1:
                return depts.get(pos).getName().substring(0,1);
            case 2:
                return contacts.get(pos).getName().substring(0,1);
            case 3:
                return deptContacts.get(pos).getName().substring(0,1);
        }
        return "";
    }
    static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView name,desg;
        ImageView profilePic;

        ContactViewHolder(View itemView)
        {
            super(itemView);
            name =(TextView)itemView.findViewById(R.id.name);
            desg =(TextView)itemView.findViewById(R.id.desg);
            profilePic=(ImageView) itemView.findViewById(R.id.profile_pic);
        }

        void bind(final int type,final Object item) {
            Glide.clear(profilePic);
            switch(type){
                case 1:
                    Department dept = (Department) item;
                    name.setText(dept.getName());
                    desg.setVisibility(View.GONE);
                    LoadingImages.loadDeptImages(dept.getPhoto(),profilePic);
                    break;
                case 2:case 3:
                    Contact contact = (Contact) item;
                    name.setText(contact.getName());
                    if(contact.getDesignation() == null)
                        desg.setVisibility(View.GONE);
                    else{
                        desg.setVisibility(View.VISIBLE);
                        desg.setText(contact.getDesignation());
                    }
                    LoadingImages.loadContactImages(contact.getProfilePic(),profilePic);
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
                    if(type == 1)
                    {
                        clicklistener.itemClicked(type,null,name.getText().toString());
                    }
                    else if(type == 2 || type ==3)
                    {
                        clicklistener.itemClicked(type,name.getText().toString(),getDept((Contact)item));
                    }

                }
            });
            }
    }
}
