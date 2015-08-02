package mobileDevelopment.com.root.campusbuddy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by rc on 18/5/15.
 */
public class delete_edit_choose extends DialogFragment  {

    AlertPositiveListener alertPositiveListener;

    private AlertPositiveListener mAlertPositiveListener;
    public interface AlertPositiveListener {
        public void onPositiveClick(int position);
    }

    // Use this instance of the interface to deliver action events

/*
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
    */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof AlertPositiveListener))
            throw new RuntimeException("The Activity must implement Callback to be used by ColorChooserDialog.");
        mAlertPositiveListener = (AlertPositiveListener) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        //getting arguments from the parent activity

        Bundle b=getArguments();
        int position=b.getInt("position");
/*
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
       // LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setTitle("Choose what you want to do?");
        //builder.setView(inflater.inflate(R.layout.activity_deleteand_edit_events2, null));

        builder.setSingleChoiceItems(RadioButtons.dae, position, null);
        builder.setPositiveButton("OK", positiveListener);
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
*/
        boolean wrapInScrollView = true;
        MaterialDialog material_dialog = new MaterialDialog.Builder(getActivity())
                .title("Choose your action")
                .customView(R.layout.activity_delete_edit, wrapInScrollView)
                .positiveText("OK")
                .negativeText("Cancel")
                .build();



//        TextView tv_edit = (TextView) dialog.findViewById(R.id.text_edit);
//        TextView tv_delete = (TextView) dialog.findViewById(R.id.text_delete);
//
//        tv_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mListener.DeleteClickListener();
//            dismiss();
//            }
//        });
//        tv_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mListener.EditClickListener();
//                dismiss();
//            }
//        });


        return material_dialog;
    }

    private void setBackgroundCompat(View view, Drawable d) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(d);
        } else {
            //noinspection deprecation
            view.setBackgroundDrawable(d);
        }
    }

    DialogInterface.OnClickListener positiveListener=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            AlertDialog dialog1=(AlertDialog)dialog;
            int position=dialog1.getListView().getCheckedItemPosition();
            alertPositiveListener.onPositiveClick(position);
        }
    };


    public void show(AppCompatActivity context, int preselect) {
        Bundle args = new Bundle();
        args.putInt("preselect", preselect);
        setArguments(args);
        show(context.getFragmentManager(), "ChooseDialogFragment");
    }

}
