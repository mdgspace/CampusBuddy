package mobileDevelopment.com.root.campusbuddy;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by rc on 29/8/15.
 */
public class Dialog_color extends DialogFragment {

    public interface ColorDialogListener {
        public void onColorChoose(int position);

    }

    // Use this instance of the interface to deliver action events
    ColorDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (ColorDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement ColorDialogListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Choose color for event");
        // Get the layout inflater
       // LayoutInflater inflater = getActivity().getLayoutInflater();
        View customView = LayoutInflater.from(getActivity()).inflate(
                R.layout.custom_color, null, false);
        ListView listView = (ListView) customView.findViewById(R.id.color_list);


        Color_list_adapter mAdapter = new Color_list_adapter(Data.getColor_list(), getActivity(), mListener);

        listView.setAdapter(mAdapter);
       // listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        dialog.setView(customView);



        return dialog.show();

    }

}
