package in.co.mdg.campusBuddy;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import in.co.mdg.campusBuddy.contacts.RecyclerViewFastScroller;
import in.co.mdg.campusBuddy.contacts.data_models.Contact;
import in.co.mdg.campusBuddy.contacts.data_models.Department;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by root on 13/6/15.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ContactViewHolder> implements RecyclerViewFastScroller.BubbleTextGetter {
    private static Realm realm = Realm.getDefaultInstance();
    private RealmResults<Department> depts;
    private RealmResults<Contact> contacts;
    private RealmList<Contact> deptContacts;
    private int type = 1;
    private static ClickListener clicklistener;
    interface ClickListener {
        void itemClicked(int type,String contactName,String deptName);
    }
    public MyRecyclerAdapter() {
        if(realm.isClosed()) {realm = Realm.getDefaultInstance();}
    }

    public void setListData(int option,String deptName) {
        switch(option)
        {
            case 1:
                if(depts==null) {depts = realm.where(Department.class).findAll().sort("name");}
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
    public void setClickListener(ClickListener clickListener)
    {
       clicklistener = clickListener;
    }

    public void closeRealm() {
        realm.close();
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
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
        TextView name;
        ImageView profilePic;

        ContactViewHolder(View itemView)
        {
            super(itemView);
            name =(TextView)itemView.findViewById(R.id.name);
            profilePic=(ImageView) itemView.findViewById(R.id.profile_pic);
        }

        void bind(final int type,final Object item) {

            switch(type){
                case 1:
                    Department dept = (Department) item;
                    name.setText(dept.getName());
                    if(dept.getPhoto() != null)
                    {
                        String deptPhoto;
                        if(dept.getPhoto().length()>4)
                            deptPhoto = dept.getPhoto();
                        else
                            deptPhoto = "http://www.iitr.ac.in/departments/" + dept.getPhoto() + "/assets/images/top1.jpg";
                        Picasso.with(profilePic.getContext())
                                .load(deptPhoto)
                                .noFade()
                                .fit()
                                .into(profilePic);
                    }
                    else
                    {
                        Picasso.with(profilePic.getContext()).load(R.drawable.iit_roorkee).noFade().fit().into(profilePic);
                    }
                    break;
                case 2:case 3:
                    Contact contact = (Contact) item;
                    name.setText(contact.getName());
                    String picAddress = contact.getProfilePic();
                    if(picAddress == null)
                    {
                        picAddress ="default.jpg";
                    }
                    if (picAddress.equals("") || picAddress.equals("default.jpg")) {
                        profilePic.setImageDrawable(
                                ContextCompat.getDrawable(
                                        profilePic.getContext()
                                        , R.drawable.com_facebook_profile_picture_blank_portrait));
                    } else {
                        Picasso.with(profilePic.getContext())
                                .load("http://people.iitr.ernet.in/facultyphoto/" + picAddress)
                                .noFade()
                                .error(R.drawable.com_facebook_profile_picture_blank_portrait)
                                .into(profilePic);
                    }


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
