package mobileDevelopment.com.root.campusbuddy;

import android.app.Activity;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import android.support.v7.app.AlertDialog;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;


/**
 * Created by rc on 18/5/15.
 */
public class Dialog_for_more_delete extends DialogFragment  {

    AlertPositiveListenerfordelete alertPositiveListener;
    long position;
    int a=0;

    private AlertPositiveListenerfordelete mAlertPositiveListener;
    public interface AlertPositiveListenerfordelete{
        public void onPositiveClickfordelete(int position);
    }

    // Use this instance of the interface to deliver action events


    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            alertPositiveListener = (AlertPositiveListenerfordelete) activity;
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
        try {
            Bundle b = getArguments();
            position = b.getLong("position");
        }
        catch (Exception e)
        {
//            Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // LayoutInflater inflater = getActivity().getLayoutInflater();
        //builder.setView(inflater.inflate(R.layout.activity_deleteand_edit_events2, null));

        builder.setMultiChoiceItems(R.array.do_multiple, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                if (b == true) {
                    a = 1;
                }
                else a=0;
            }
        });
        builder.setPositiveButton("OK", positiveListenerfordelete);
        AlertDialog dialog = builder.create();




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
        return dialog;

    }

    DialogInterface.OnClickListener positiveListenerfordelete=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
//            AlertDialog dialog1=(AlertDialog)dialog;
//            int position=dialog1.getListView().getCheckedItemPosition();
            alertPositiveListener.onPositiveClickfordelete(a);
        }
    };

//    DialogInterface.OnClickListener positiveListener=new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//            AlertDialog dialog1=(AlertDialog)dialog;
//            int position=dialog1.getListView().getCheckedItemPosition();
//            alertPositiveListener.onPositiveClick(position);
//        }
//    };


}
