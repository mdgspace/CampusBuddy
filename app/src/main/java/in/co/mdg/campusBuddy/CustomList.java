package in.co.mdg.campusBuddy;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by root on 9/7/15.
 */
public class CustomList extends ArrayAdapter<Page> {

//    ArrayList<String> alreadychosen;

    Activity context;
    ArrayList<Page> pageList;
//    ArrayList<String> listofvalues;
    public CustomList(Activity context,ArrayList<Page> pageList)
    {
        super(context,R.layout.mytextviewfb,pageList);
        this.context=context;
        this.pageList=pageList;
//        alreadychosen=PagesSelected.getSelectedPageIds(getContext());

    }


    @NonNull
    @Override
    public View getView(final int p, View view, @NonNull ViewGroup parent)
    {
        LayoutInflater inflator=context.getLayoutInflater();
        View row=inflator.inflate(R.layout.mytextviewfb, null, true);
        TextView txt=(TextView)row.findViewById(R.id.textforpage);

        final CheckBox cb = (CheckBox) row.findViewById(R.id.checkBox);
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cb.setChecked(!cb.isChecked());
            }
        });
//        Fblist fblist=new Fblist();
//        try {
//            for (int i = 0; i < alreadychosen.size(); i++) {
//
////                if (fblist.fbpageslikedmap.get(fblist.pageList.get(p).getPage_name()) == alreadychosen.get(i)) {
//                if(listofvalues.get(p)==alreadychosen.get(i)){
//                cb.setChecked(true);
//                }
//            }
//        }
//        catch (Exception e)
//        {
//            Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
//        }
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pageList.get(p).setIsSelected(isChecked);
            }
        });
        cb.setChecked(pageList.get(p).isSelected());
        txt.setText(pageList.get(p).getPage_name());
//        txt.setHeight(100);
        return row;
    }
}
