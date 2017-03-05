package in.co.mdg.campusBuddy.contacts;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import in.co.mdg.campusBuddy.R;
import in.co.mdg.campusBuddy.contacts.data_models.Group;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by shyam on 01-Mar-17.
 */

public class ContactsSectionRecyclerAdapter extends RecyclerView.Adapter<ContactsSectionRecyclerAdapter.ContactsSectionRecyclerItemHolder> implements RecyclerViewFastScroller.BubbleTextGetter {
    private Realm realm = Realm.getDefaultInstance();
    private RealmResults<Group> groups;
    public static int ENGLISH = 1;
    public static int HINDI = 2;
    private static int lang;
    private static ClickListener clickListener;
    ContactsRecyclerAdapter.ClickListener adapterGroupContactslistener;

    public interface ClickListener {
        void itemClickedHome(int type, String contactName, String deptName, String groupName);
    }

    public ContactsSectionRecyclerAdapter() {
        if (realm.isClosed()) {
            realm = Realm.getDefaultInstance();
        }
    }

    public void getListOfGroups(){
        groups = realm.where(Group.class).findAll().sort("name");
        notifyDataSetChanged();
    }

    void setLang(int lang) {
        ContactsSectionRecyclerAdapter.lang = lang;
        notifyDataSetChanged();
    }

    int getLang() {
        return ContactsSectionRecyclerAdapter.lang;
    }

    void setClickListener(ContactsSectionRecyclerAdapter.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    void setClickListenerDepartments(ContactsRecyclerAdapter.ClickListener clickListenerDepartments){
        adapterGroupContactslistener = clickListenerDepartments;
    }

    void closeRealm() {
        realm.close();
    }

    @Override
    public String getTextToShowInBubble(int pos) {
        return groups.get(pos).getName().substring(0, 1);
    }

    @Override
    public ContactsSectionRecyclerItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_section_cardview,parent,false);
        return new ContactsSectionRecyclerItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactsSectionRecyclerItemHolder holder, int position) {
        holder.bind(position,groups.get(position));
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    class ContactsSectionRecyclerItemHolder extends RecyclerView.ViewHolder {
        TextView departmentName;
        TextView seeAll;
        RecyclerView topThreeContacts;

        ContactsSectionRecyclerItemHolder(View view) {
            super(view);
            departmentName = (TextView) view.findViewById(R.id.departmentName);
            departmentName.setAllCaps(true);
            seeAll = (TextView) view.findViewById(R.id.seeMoreText);
            topThreeContacts = (RecyclerView) view.findViewById(R.id.cardviewSectionRecyclerView);
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
            topThreeContacts.setLayoutManager(linearLayoutManager);
            DisplayMetrics metrics = new DisplayMetrics();
//            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            topThreeContacts.addItemDecoration(new DividerItemDecoration(view.getContext(), R.drawable.divider, metrics.density));
        }

        void bind(int position, final Object item) {
            final Group group = (Group) item;
            ContactsRecyclerAdapter adapterGroupContacts = new ContactsRecyclerAdapter();
            adapterGroupContacts.setClickListener(adapterGroupContactslistener);
            if(adapterGroupContacts!=null) {
                adapterGroupContacts.setLang(lang);
            }
            if (lang == HINDI && group.getNameHindi() != null) {
                departmentName.setText(group.getNameHindi());
                adapterGroupContacts.setListData(1,groups.get(position).getName());
                topThreeContacts.setAdapter(adapterGroupContacts);
            } else {
                departmentName.setText(group.getName());
                adapterGroupContacts.setListData(1,groups.get(position).getName());
                topThreeContacts.setAdapter(adapterGroupContacts);
            }

            seeAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.itemClickedHome(1,null,null,group.getName());
                }
            });
        }

    }
}
