package in.co.mdg.campusBuddy.contacts;

/**
 * Created by shyam on 06-Mar-17.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import in.co.mdg.campusBuddy.R;
import in.co.mdg.campusBuddy.contacts.data_models.Contact;
import in.co.mdg.campusBuddy.contacts.data_models.Department;
import in.co.mdg.campusBuddy.contacts.data_models.Group;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class SwipeRecyclerViewAdapter extends RecyclerSwipeAdapter<SwipeRecyclerViewAdapter.SimpleViewHolder> implements RecyclerViewFastScroller.BubbleTextGetter{
    private Realm realm = Realm.getDefaultInstance();
    private RealmResults<Contact> deptContacts;
    private int type = 1;
    public static int HINDI = 2;
    private static int lang;
    private String emailId;
    private String mobile;
    private String phone;
    private Context context;
    private String std_code_res_off;


    public SwipeRecyclerViewAdapter(Context context) {
        this.context = context;
        if (realm.isClosed()) {
            realm = Realm.getDefaultInstance();
        }
    }

    void setListData(String deptName) {
        //for showing contacts of a dept
        deptContacts = realm.where(Department.class).equalTo("name", deptName).findFirst().getContacts().sort("name");
        notifyDataSetChanged();
    }

    void setLang(int lang) {
        SwipeRecyclerViewAdapter.lang = lang;
        notifyDataSetChanged();
    }

    int getLang() {
        return SwipeRecyclerViewAdapter.lang;
    }

    private String getDept(Contact contact) {
        RealmQuery<Department> deptSearch = realm
                .where(Department.class)
                .equalTo("contacts.name", contact.getName());
        if (contact.getIitr_o().size() > 0)
            deptSearch.equalTo("contacts.iitr_o.number", contact.getIitr_o().get(0).getNumber());
        else if (contact.getIitr_r().size() > 0)
            deptSearch.equalTo("contacts.iitr_r.number", contact.getIitr_r().get(0).getNumber());
        else if (contact.getPhoneBSNL().size() > 0)
            deptSearch.equalTo("contacts.phoneBSNL.number", contact.getPhoneBSNL().get(0).getNumber());
        return deptSearch.findFirst().getName();
    }

    private String getGroup(String deptName) {
        RealmQuery<Group> groupSearch = realm
                .where(Group.class)
                .equalTo("departments.name", deptName);
        return groupSearch.findFirst().getName();
    }

    void closeRealm() {
        realm.close();
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_recycler_view_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        viewHolder.bind(type, deptContacts.get(position));
        final Contact contact = (Contact) deptContacts.get(position);
        final String dept = getDept(contact);
        String group = getGroup(dept);

        if (group.equals("Saharanpur Campus")) {
            std_code_res_off = context.getResources().getString(R.string.std_code_res_off_shrnpr);
        } else {
            std_code_res_off = context.getResources().getString(R.string.std_code_res_off_rk);
        }

//                    LoadingImages.loadContactImages(contact.getProfilePic(), profilePic);

        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        // Drag From Right
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper));


        // Handling different events when swiping
        viewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });

        viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, ShowContact.class);
                in.putExtra("name", contact.getName());
                in.putExtra("dept", dept);
                in.putExtra("group", getGroup(dept));
                in.putExtra("lang", lang);
                context.startActivity(in);
//                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);}
            }
        });


        viewHolder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int officeContactSize = contact.getIitr_o().size();
                int residenceContactSize = contact.getIitr_r().size();
                if (officeContactSize > 0){
                    phone = String.format("%s%s", std_code_res_off, contact.getIitr_o().get(0).getNumber());
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone.replace(" ", "")));
                    context.startActivity(intent);
                }else if(residenceContactSize > 0){
                    phone = String.format("%s%s", std_code_res_off, contact.getIitr_r().get(0).getNumber());
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone.replace(" ", "")));
                    context.startActivity(intent);
                }
                else{
                    Toast.makeText(context,"Phone number does not exist!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewHolder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int mobileContactSize = contact.getMobile().size();
                if (mobileContactSize > 0){
                    mobile = contact.getMobile().get(0).getNumber();
                    Intent intent;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                        intent = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:" + mobile.replace(" ", "")));
                    }else{
                        //Old way of accessing sms activity
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setType("vnd.android-dir/mms-sms");
                        intent.putExtra("address", phone);
                        intent.putExtra("exit_on_sent", true);
                    }
                    context.startActivity(intent);
                }
                else{
                    Toast.makeText(context,"Mobile number does not exist!",Toast.LENGTH_SHORT).show();
                }
            }
        });


        viewHolder.email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int emailContactSize = contact.getEmail().size();
                if (emailContactSize > 0) {
                    emailId = contact.getEmail().get(0).getNumber();
                    if (!emailId.contains("@")) {
                        emailId = emailId.concat("@iitr.ac.in");
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + emailId));
                    context.startActivity(intent);
                }else {
                    Toast.makeText(context,"Email address does not exist",Toast.LENGTH_SHORT).show();
                }
            }
        });


        // mItemManger is member in RecyclerSwipeAdapter Class
        mItemManger.bindView(viewHolder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return deptContacts.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public String getTextToShowInBubble(int pos) {
          return deptContacts.get(pos).getName().substring(0, 1);
    }

    //  ViewHolder Class

    class SimpleViewHolder extends RecyclerView.ViewHolder {
        TextView name, desg;
        ImageView profilePic;
        SwipeLayout swipeLayout;
        TextView call;
        TextView email;
        TextView message;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            name = (TextView) itemView.findViewById(R.id.swipeContactName);
            desg = (TextView) itemView.findViewById(R.id.swipeContactDesg);
            profilePic = (ImageView) itemView.findViewById(R.id.swipe_profile_pic);
            call = (TextView) itemView.findViewById(R.id.swipeCall);
            email = (TextView) itemView.findViewById(R.id.swipeEmail);
            message = (TextView) itemView.findViewById(R.id.swipeMessage);
        }

        void bind(final int type, final Object item) {
            Glide.clear(profilePic);
            final Contact contact = (Contact) item;
            if (lang == HINDI && contact.getNameHindi() != null) {
                name.setText(contact.getNameHindi());
            } else {
                name.setText(contact.getName());
            }
            if (contact.getDesg() == null)
                desg.setVisibility(View.GONE);
            else {
                desg.setVisibility(View.VISIBLE);
                if (lang == HINDI && contact.getDesgHindi() != null) {
                    desg.setText(contact.getDesgHindi());
                } else {
                    desg.setText(contact.getDesg());
                }
            }
        }
    }
}