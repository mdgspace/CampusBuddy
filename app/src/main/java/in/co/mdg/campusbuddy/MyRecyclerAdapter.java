package in.co.mdg.campusbuddy;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 13/6/15.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<ContactViewHolder>
{
    public List<Contact> contacts;
    public MyRecyclerAdapter(List<Contact> contacts)
    {
        this.contacts=new ArrayList<Contact>();
        this.contacts.addAll(contacts);
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view,parent,false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        Contact contact=contacts.get(position);
        holder.nameT.setText(contact.getName());

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}
