package in.co.mdg.campusBuddy;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rc on 29/8/15.
 */
public class Color_list_adapter extends BaseAdapter {

    ArrayList<Color_item> mData;
    Context mContext;
    LayoutInflater inflater;
    Dialog_color.ColorDialogListener mListener;

    public Color_list_adapter(ArrayList<Color_item> data, Context context, Dialog_color.ColorDialogListener mListener) {
        mData = data;
        mContext = context;
        inflater = LayoutInflater.from(context);
        this.mListener = mListener;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.custom_color_item, null);
            holder=new Holder();
            holder.colorll=(LinearLayout)convertView.findViewById(R.id.colorLL);
            holder.colortext=(TextView)convertView.findViewById(R.id.color_text);
            holder.colorimage=(ImageButton)convertView.findViewById(R.id.color_image);
            final Color_item color_item=mData.get(position);
            try {
                holder.colortext.setText(color_item.getColor());
                holder.colorimage.setBackgroundColor(Color.parseColor(color_item.getHash()));
                holder.colorll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("Hey", "Hey");
                        mListener.onColorChoose(position);
                    }
                });

            }
            catch(Exception e)
            {
                Log.e("HEY", e.toString());
            }
        }



        return convertView;
    }


    public static class Holder {

        LinearLayout colorll;
        TextView colortext;
        ImageButton colorimage;
    }
}
