package in.co.mdg.campusBuddy.contacts;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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

class SearchSuggestionAdapter extends ArrayAdapter<ContactSearchModel> {


    private ArrayList<ContactSearchModel> suggestions;
    private int viewResourceId;
    private LayoutInflater mInflater;
    private String queryString;
    private final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.parseColor("#be5e00"));

    SearchSuggestionAdapter(Context context, int viewResourceId) {
        super(context, viewResourceId);
        this.suggestions = new ArrayList<>();
        this.viewResourceId = viewResourceId;
        mInflater = LayoutInflater.from(context);
    }


    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(viewResourceId, parent, false);
            holder = new ViewHolder();
            holder.contactName = (TextView) convertView.findViewById(R.id.contact_name);
            holder.dept = (TextView) convertView.findViewById(R.id.contact_dept);
            holder.profilePic = (ImageView) convertView.findViewById(R.id.profile_pic);
            holder.icon = (ImageView) convertView.findViewById(R.id.history_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        ContactSearchModel contact = getItem(position);
        if (contact != null) {
            SpannableStringBuilder sb = new SpannableStringBuilder(contact.getName());
            if (!queryString.equals("")) {
                int searchMatchPosition = contact.getName().toLowerCase().indexOf(queryString.toLowerCase());
                if (searchMatchPosition != -1)
                    sb.setSpan(fcs, searchMatchPosition, searchMatchPosition + queryString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }
            holder.contactName.setText(sb);
            Glide.clear(holder.profilePic);
            if (contact.isDept()) {

                LoadingImages.loadDeptImages(contact.getProfilePic(),holder.profilePic);
            } else {
                if( contact.getProfilePic() != null)
                    holder.dept.setVisibility(View.GONE);
                else{
                    holder.dept.setVisibility(View.VISIBLE);
                    holder.dept.setText(contact.getDept());
                }
                LoadingImages.loadContactImages(contact.getProfilePic(),holder.profilePic);
                if (contact.isHistorySearch())
                    holder.icon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_history_black_24dp));
                else
                    holder.icon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_search_black_24dp));
            }

        }
        return convertView;
    }

    private static class ViewHolder {
        TextView contactName,dept;
        ImageView icon, profilePic;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    private Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            return ((ContactSearchModel) (resultValue)).getName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Realm realm = Realm.getDefaultInstance();
            suggestions.clear();
            RealmResults<ContactSearchModel> historySearches = null;
            RealmResults<Contact> contacts = null;
            RealmResults<Department> depts = null;
            RealmResults<Contact> adminContacts = null;
            if (constraint == null) {
                queryString = "";
                historySearches = realm.where(ContactSearchModel.class).findAll().sort("dateAdded", Sort.DESCENDING);
            } else {
                queryString = constraint.toString();
                historySearches = realm.where(ContactSearchModel.class)
                        .contains("name", queryString, Case.INSENSITIVE)
                        .findAll()
                        .sort("dateAdded", Sort.DESCENDING);
                RealmQuery<Contact> contactRealmQuery = realm.where(Contact.class)
                        .contains("name", queryString, Case.INSENSITIVE);
                if (historySearches.size() > 0) {
                    contactRealmQuery.notEqualTo("name", historySearches.get(0).getName());
                    if (historySearches.size() > 1)
                        contactRealmQuery.notEqualTo("name", historySearches.get(1).getName());
                }
                contacts = contactRealmQuery.findAll().sort("name");
                depts = realm.where(Department.class).contains("name", queryString, Case.INSENSITIVE).findAll().sort("name");
                adminContacts = realm.where(Department.class)
                        .equalTo("name","Administration")
                        .findFirst()
                        .getContacts()
                        .where()
                        .contains("designation",queryString,Case.INSENSITIVE)
                        .findAll()
                        .sort("name");
            }
            int HISTORY_ITEM_LIMIT = 2;
            for (int i = 0; i < (historySearches.size() > HISTORY_ITEM_LIMIT ? HISTORY_ITEM_LIMIT : historySearches.size()); i++) {
                ContactSearchModel contactSearchModel = new ContactSearchModel();
                contactSearchModel.setName(historySearches.get(i).getName());
                contactSearchModel.setProfilePic(historySearches.get(i).getProfilePic());
                contactSearchModel.setHistorySearch(historySearches.get(i).isHistorySearch());
                contactSearchModel.setDept(historySearches.get(i).getDept());
                suggestions.add(contactSearchModel);

            }
            if (contacts != null) {
                int TOTAL_ITEM_LIMIT = 5;
                int limitSearch = TOTAL_ITEM_LIMIT - (historySearches.size() > HISTORY_ITEM_LIMIT ? HISTORY_ITEM_LIMIT : historySearches.size());
                limitSearch = (limitSearch > contacts.size()) ? contacts.size() : limitSearch;
                for (int i = 0; i < limitSearch; i++) {
                    Contact contact = contacts.get(i);
                    ContactSearchModel contactSearchModel = new ContactSearchModel();
                    contactSearchModel.setName(contact.getName());
                    contactSearchModel.setHistorySearch(false);
                    contactSearchModel.setProfilePic(contact.getProfilePic());
                    RealmQuery<Department> deptSearch = realm
                            .where(Department.class)
                            .equalTo("contacts.name", contact.getName());
                    if (contact.getIitr_o() != null)
                        deptSearch.equalTo("contacts.iitr_o", contact.getIitr_o());
                    else if (contact.getIitr_r() != null)
                        deptSearch.equalTo("contacts.iitr_r", contact.getIitr_r());
                    else if (contact.getPhoneBSNL() != null)
                        deptSearch.equalTo("contacts.phoneBSNL", contact.getPhoneBSNL());
                    String dept = deptSearch.findFirst().getName();
                    contactSearchModel.setDept(dept);
                    suggestions.add(contactSearchModel);
                }
            }
            if (depts != null) {
                int DEPT_ITEM_LIMIT = 5;
                for (int i = 0; i < (depts.size() > DEPT_ITEM_LIMIT ? DEPT_ITEM_LIMIT : depts.size()); i++) {
                    Department dept = depts.get(i);
                    ContactSearchModel contactSearchModel = new ContactSearchModel();
                    contactSearchModel.setName(dept.getName());
                    contactSearchModel.setHistorySearch(false);
                    contactSearchModel.setProfilePic(dept.getPhoto());
                    contactSearchModel.setDept(true);
                    suggestions.add(contactSearchModel);
                }
            }
            if (adminContacts != null) {
                int ADMIN_ITEM_LIMIT = 5;
                for (int i = 0; i < (adminContacts.size() > ADMIN_ITEM_LIMIT ? ADMIN_ITEM_LIMIT : adminContacts.size()); i++) {
                    Contact adminContact = adminContacts.get(i);
                    ContactSearchModel contactSearchModel = new ContactSearchModel();
                    contactSearchModel.setName(adminContact.getDesignation());
                    contactSearchModel.setHistorySearch(false);
                    contactSearchModel.setProfilePic(adminContact.getProfilePic());
                    contactSearchModel.setDept("Administration");
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
            if (results.count > 0) {
                clear();
                addAll((ArrayList<ContactSearchModel>) results.values);
                notifyDataSetChanged();
            }
        }
    };

}
