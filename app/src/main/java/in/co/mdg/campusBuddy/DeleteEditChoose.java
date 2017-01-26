package in.co.mdg.campusBuddy;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;


/**
 * Created by rc on 18/5/15.
 */
public class DeleteEditChoose extends DialogFragment {

    private AlertPositiveListener alertPositiveListener;

    interface AlertPositiveListener {
        void onPositiveClick(int choice, boolean isMultiChecked);
    }

    // Use this instance of the interface to deliver action events


    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            alertPositiveListener = (AlertPositiveListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement ChooseDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        //getting arguments from the parent activity

        Bundle b = getArguments();
        boolean isMultiEnabled = b.getBoolean("isMultiEnabled");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setTitle("Choose what you want to do?");
        View view = inflater.inflate(R.layout.activity_delete_edit, null);
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        final CheckBox multiCB = (CheckBox) view.findViewById(R.id.multi_checkbox);
        if (!isMultiEnabled) {
            multiCB.setEnabled(false);
            multiCB.setVisibility(View.GONE);
        }

        builder.setView(view);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int choice;
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.edit_radio:
                        choice = 1;
                        break;
                    case R.id.delete_radio:
                        choice = 2;
                        break;
                    default:
                        choice = 0;
                }
                boolean isMultiChecked = multiCB.isChecked();
                alertPositiveListener.onPositiveClick(choice, isMultiChecked);
            }
        });
        builder.setNegativeButton("Cancel", null);

        return builder.create();

    }

}
