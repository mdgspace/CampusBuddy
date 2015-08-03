package mobileDevelopment.com.root.campusbuddy;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 13/6/15.
 */
public class MyRecyclerAdapter_departmentcontacts extends RecyclerView.Adapter<ContactViewHolderdept>
{
    public List<Contact> contacts;
    public MyRecyclerAdapter_departmentcontacts(List<Contact> contacts)
    {
        this.contacts=new ArrayList<Contact>();
        this.contacts.addAll(contacts);
    }

    @Override
    public ContactViewHolderdept onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_dept,parent,false);
        return new ContactViewHolderdept(itemView);
    }

    @Override
    public void onBindViewHolder(ContactViewHolderdept holder, final int position) {
        Contact contact=contacts.get(position);
        holder.nameT.setText(contact.getName());
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + TelephoneContacts.contactnos[position]));
                view.getContext().startActivity(intent);
            }
        });

        holder.email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setType("plain/text");
//                intent.setData(Uri.parse("www.gmail.com"));
                intent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{TelephoneContacts.emailids[position]});
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}
