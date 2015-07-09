package mobileDevelopment.com.root.campusbuddy;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by root on 9/7/15.
 */
public class CustomList extends ArrayAdapter<String> {

    final Activity context;
    final String[] fbpages;
    public CustomList(Activity context,String[]fbpages)
    {
        super(context,R.layout.mytextviewfb,fbpages);
        this.context=context;
        this.fbpages=fbpages;
    }


    @Override
    public View getView(int p, View view,ViewGroup parent)
    {
        LayoutInflater inflator=context.getLayoutInflater();
        View row=inflator.inflate(R.layout.mytextviewfb, null, true);
        TextView txt=(TextView)row.findViewById(R.id.textforpage);
        txt.setText(fbpages[p]);
        txt.setHeight(100);
        return row;
    }
}
