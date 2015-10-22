package in.co.mdg.campusbuddy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
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

        boolean valid_email = true;
        holder.call.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_ic_call_black_18dp));
        holder.email.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_ic_email_black_18dp));


        if(TelephoneContacts.emailids[position] == null || TelephoneContacts.emailids[position].length() == 0){

            valid_email = false;
            holder.email.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_ic_email_grey_18dp));
        }

        final boolean email_status = valid_email;
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TelephoneContacts.c);
// ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = LayoutInflater.from(TelephoneContacts.c);
                View dialogView = inflater.inflate(R.layout.threecontacts, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setTitle(TelephoneContacts.names[position]);
                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                RelativeLayout r1=(RelativeLayout)dialogView.findViewById(R.id.c1);
                RelativeLayout r2=(RelativeLayout)dialogView.findViewById(R.id.c2);
                RelativeLayout r3=(RelativeLayout)dialogView.findViewById(R.id.c3);
                ImageButton i1=(ImageButton)dialogView.findViewById(R.id.call1);
                ImageButton i2=(ImageButton)dialogView.findViewById(R.id.call2);
                ImageButton i3=(ImageButton)dialogView.findViewById(R.id.call3);



                i1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_ic_call_black_18dp));
                i2.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_ic_call_black_18dp));
               i3.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_ic_call_black_18dp));

                boolean valid_phone_number1 = true;
                if(TelephoneContacts.contactnos_iitr_o[position] == null || TelephoneContacts.contactnos_iitr_o[position].length() == 0){
                    valid_phone_number1 = false;
                    i1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_ic_call_grey_18dp));
                }

                final boolean call_status1 = valid_phone_number1;
                r1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(call_status1){
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: 013322" + TelephoneContacts.contactnos_iitr_o[position]));
                            view.getContext().startActivity(intent);}
                            else{
                                Toast.makeText(mContext, "Contact Number not available", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });










                boolean valid_phone_number2 = true;
                if(TelephoneContacts.contactnos_iitr_r[position] == null || TelephoneContacts.contactnos_iitr_r[position].length() == 0){
                    valid_phone_number2 = false;
                    i2.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_ic_call_grey_18dp));
                }

                final boolean call_status2 = valid_phone_number2;
                r2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(call_status2){
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: 013322" + TelephoneContacts.contactnos_iitr_r[position]));
                            view.getContext().startActivity(intent);}
                        else{
                            Toast.makeText(mContext, "Contact Number not available", Toast.LENGTH_SHORT).show();
                        }
                    }
                });












                boolean valid_phone_number3 = true;
                if(TelephoneContacts.contactnos_bsnl[position] == null || TelephoneContacts.contactnos_bsnl[position].length() == 0){
                    valid_phone_number3 = false;
                    i3.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_ic_call_grey_18dp));
                }

                final boolean call_status3 = valid_phone_number3;
                r3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(call_status3){
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: 013322" + TelephoneContacts.contactnos_bsnl[position]));
                            view.getContext().startActivity(intent);}
                        else{
                            Toast.makeText(mContext, "Contact Number not available", Toast.LENGTH_SHORT).show();
                        }
                    }
                });








                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
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
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{TelephoneContacts.emailids[position]+"@iitr.ernet.in"});
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
