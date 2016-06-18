package in.co.mdg.campusBuddy.contacts;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import in.co.mdg.campusBuddy.R;
import in.co.mdg.campusBuddy.contacts.data_models.Contact;
import in.co.mdg.campusBuddy.contacts.data_models.Department;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Chirag on 15-06-2016.
 */

class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolderSearch> {

    private RealmResults<Contact> contactList;
    private Realm realm = Realm.getDefaultInstance();
    SearchListAdapter(String queryString) {
        contactList = realm.where(Contact.class).contains("name",queryString, Case.INSENSITIVE).findAll();
    }
    void changeQuery(String newQuery)
    {
        contactList = realm.where(Contact.class).contains("name",newQuery, Case.INSENSITIVE).findAll();
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolderSearch holder, int i) {
        Contact contact = contactList.get(i);

        if(contact.getProfilePic() != null)
        {
            if(contact.getProfilePic().equals("") || contact.getProfilePic().equals("default.jpg"))
            {
                holder.profilePic.setImageDrawable(
                        ContextCompat.getDrawable(
                                holder.profilePic.getContext()
                                ,R.drawable.com_facebook_profile_picture_blank_portrait));
            }
            else
            {
                Picasso.with(holder.profilePic.getContext())
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
                            holder.profilePic.getContext()
                            ,R.drawable.ic_account_circle_black_24dp));
        }
        holder.name.setText(contact.getName());
        holder.dept.setText(realm.where(Department.class).equalTo("contacts.name",contact.getName()).findFirst().getName());
    }

    @Override
    public ViewHolderSearch onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_view_searchlist, viewGroup, false);

        return new ViewHolderSearch(itemView);
    }

    class ViewHolderSearch extends RecyclerView.ViewHolder{
        ImageView profilePic;
        TextView name,dept;
        ViewHolderSearch(View itemView) {
            super(itemView);
            profilePic = (ImageView) itemView.findViewById(R.id.profile_pic);
            name = (TextView) itemView.findViewById(R.id.name);
            dept = (TextView) itemView.findViewById(R.id.department);


        }
    }

}
