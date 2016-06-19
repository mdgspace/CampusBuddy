package in.co.mdg.campusBuddy.contacts;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.co.mdg.campusBuddy.R;
import in.co.mdg.campusBuddy.contacts.data_models.Contact;
import in.co.mdg.campusBuddy.contacts.data_models.ContactSearchModel;
import in.co.mdg.campusBuddy.contacts.data_models.Department;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Chirag on 13-06-2016.
 */

public class SearchSuggestionAdapter extends ArrayAdapter<ContactSearchModel> {

    private ArrayList<ContactSearchModel> suggestions;
    private int viewResourceId;
    private LayoutInflater mInflater;
    private final static int HISTORY_ITEM_LIMIT = 2;
    private final static int TOTAL_ITEM_LIMIT = 5;

    public SearchSuggestionAdapter(Context context, int viewResourceId) {
        super(context, viewResourceId);
        this.suggestions = new ArrayList<>();
        this.viewResourceId = viewResourceId;
        mInflater = LayoutInflater.from(context);
    }



    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(viewResourceId,parent,false);
            holder = new ViewHolder();
            holder.contactName = (TextView) convertView.findViewById(R.id.contact_name);
            holder.profilePic = (ImageView) convertView.findViewById(R.id.profile_pic);
            holder.icon = (ImageView) convertView.findViewById(R.id.history_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ContactSearchModel contact = getItem(position);
        if (contact != null) {
            holder.contactName.setText(contact.getName());
            if(contact.getProfilePic() != null)
            {
                if(contact.getProfilePic().equals("") || contact.getProfilePic().equals("default.jpg"))
                {
                    holder.profilePic.setImageDrawable(
                            ContextCompat.getDrawable(
                                    getContext()
                                    ,R.drawable.com_facebook_profile_picture_blank_portrait));
                }
                else
                {
                    Picasso.with(getContext())
                            .load("http://people.iitr.ernet.in/facultyphoto/"+contact.getProfilePic())
                            .fit()
                            .error(R.drawable.com_facebook_profile_picture_blank_portrait)
                            .into(holder.profilePic);
                }
            }
            else
            {
                holder.profilePic.setImageDrawable(
                        ContextCompat.getDrawable(
                                getContext()
                                ,R.drawable.ic_account_circle_black_24dp));
            }
            if(contact.isHistorySearch())
                holder.icon.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_history_black_24dp));
            else
                holder.icon.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_search_black_24dp));
        }
        return convertView;
    }
    private static class ViewHolder {
        TextView contactName;
        ImageView icon, profilePic;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    private Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            return ((ContactSearchModel)(resultValue)).getName();
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Realm realm = Realm.getDefaultInstance();
            suggestions.clear();
            RealmResults<ContactSearchModel> historySearches = null;
            RealmResults<Contact> contacts = null;
            if (constraint == null) {
                historySearches = realm.where(ContactSearchModel.class).findAll().sort("dateAdded", Sort.DESCENDING);
            }
            else
            {
                historySearches = realm.where(ContactSearchModel.class).beginsWith("name",constraint.toString(), Case.INSENSITIVE).findAll().sort("dateAdded",Sort.DESCENDING);
                RealmQuery<Contact> contactRealmQuery = realm.where(Contact.class)
                        .beginsWith("name", constraint.toString(), Case.INSENSITIVE);
                if(historySearches.size()>0) {
                    contactRealmQuery.notEqualTo("name", historySearches.get(0).getName());
                    if (historySearches.size()>1)
                        contactRealmQuery.notEqualTo("name", historySearches.get(1).getName());
                }
                contacts = contactRealmQuery.findAll().sort("name");
            }
            for(int i=0;i<(historySearches.size()>HISTORY_ITEM_LIMIT?HISTORY_ITEM_LIMIT:historySearches.size());i++)
            {
                ContactSearchModel contactSearchModel = new ContactSearchModel();
                contactSearchModel.setName(historySearches.get(i).getName());
                contactSearchModel.setProfilePic(historySearches.get(i).getProfilePic());
                contactSearchModel.setHistorySearch(historySearches.get(i).isHistorySearch());
                contactSearchModel.setDept(historySearches.get(i).getDept());
                suggestions.add(contactSearchModel);

            }
            if(contacts != null)
            {
                int limitSearch = TOTAL_ITEM_LIMIT - (historySearches.size() > HISTORY_ITEM_LIMIT ? HISTORY_ITEM_LIMIT : historySearches.size());
                limitSearch = (limitSearch >contacts.size())?contacts.size(): limitSearch;
                for (int i = 0; i< limitSearch; i++) {
                    Contact contact = contacts.get(i);
                    ContactSearchModel contactSearchModel = new ContactSearchModel();
                    contactSearchModel.setName(contact.getName());
                    contactSearchModel.setHistorySearch(false);
                    contactSearchModel.setProfilePic(contact.getProfilePic());
                    RealmQuery<Department> deptSearch = realm
                            .where(Department.class)
                            .equalTo("contacts.name",contact.getName());
                    if(contact.getIitr_o() != null)
                        deptSearch.equalTo("contacts.iitr_o",contact.getIitr_o());
                    else if(contact.getIitr_r() != null)
                        deptSearch.equalTo("contacts.iitr_r",contact.getIitr_r());
                    else if(contact.getPhoneBSNL() != null)
                        deptSearch.equalTo("contacts.phoneBSNL",contact.getPhoneBSNL());
                    String dept = deptSearch.findFirst().getName();
                    contactSearchModel.setDept(dept);
                    suggestions.add(contactSearchModel);
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = suggestions;
            filterResults.count = suggestions.size();
            realm.close();
            return filterResults;
        }
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(results.count > 0) {
                clear();
                addAll((ArrayList<ContactSearchModel>) results.values);
                notifyDataSetChanged();
            }
        }
    };

}
