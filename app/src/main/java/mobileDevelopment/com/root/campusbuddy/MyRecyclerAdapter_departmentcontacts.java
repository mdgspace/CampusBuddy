package mobileDevelopment.com.root.campusbuddy;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 13/6/15.
 */
public class MyRecyclerAdapter_departmentcontacts extends RecyclerView.Adapter<ContactViewHolderdept>
{
    public List<Contact> contacts;
    public Context mContext;
    public MyRecyclerAdapter_departmentcontacts(List<Contact> contacts, Context context)
    {
        this.contacts=new ArrayList<Contact>();
        this.contacts.addAll(contacts);
        mContext = context;
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
        boolean valid_phone_number = true;
        boolean valid_email = true;
        if(TelephoneContacts.contactnos[position] == null || TelephoneContacts.contactnos[position].length() == 0){
            valid_phone_number = false;
            holder.call.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_call_grey_18dp));
        }
        if(TelephoneContacts.emailids[position] == null || TelephoneContacts.emailids[position].length() == 0){
            valid_email = false;
            holder.email.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_email_grey_18dp));
        }
        final boolean call_status = valid_phone_number;
        final boolean email_status = valid_email;
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(call_status){
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + TelephoneContacts.contactnos[position]));
                    view.getContext().startActivity(intent);
                }else{
                    Toast.makeText(mContext, "Contact Number not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email_status){
                    Intent intent=new Intent(Intent.ACTION_VIEW);
                    intent.setType("plain/text");
//                intent.setData(Uri.parse("www.gmail.com"));
                    intent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{TelephoneContacts.emailids[position]});
                    view.getContext().startActivity(intent);
                }else{
                    Toast.makeText(mContext, "Email Id not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}
