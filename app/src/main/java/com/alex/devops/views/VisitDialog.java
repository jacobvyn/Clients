package com.alex.devops.views;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alex.devops.R;
import com.alex.devops.commons.BaseActivity;
import com.alex.devops.utils.Utils;

public class VisitDialog extends DialogFragment implements DialogInterface.OnShowListener {
    public static String TAG = VisitDialog.class.getName();

    private EditText mVisitCounterEditText;
    private VisitDialog.Listener mListener;


    public static VisitDialog newInstance() {
        return new VisitDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.visit_dialog_layout, null);
        mVisitCounterEditText = (EditText) view.findViewById(R.id.visit_counter_edit_text_po_tome_chto);
//        mVisitCounterEditText.setText(getMaxVisitAmount());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.max_visits);
        builder.setMessage(R.string.visit_dialog_message);
        builder.setView(view);
        builder.setPositiveButton(R.string.ok, null);
        builder.setNegativeButton(R.string.abort, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(this);
        return dialog;
    }

    public void setListener(VisitDialog.Listener listener) {
        mListener = listener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private int getMaxVisitAmount() {
        return getActivity() instanceof BaseActivity ? ((BaseActivity) getActivity()).getMaxVisitAmount() : 0;
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {
        final AlertDialog dialog = (AlertDialog) dialogInterface;
        final Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOkClicked();
            }
        });
    }

    private void onOkClicked() {
        if (isDataValid()) {
            onOkPressed(getVisitCount());
            dismiss();
        } else {
            Toast.makeText(getActivity(), R.string.visit_amount_error, Toast.LENGTH_LONG).show();
        }
    }

    private boolean isDataValid() {
        return getVisitCount() > 0;
    }

    private void onOkPressed(int visitCount) {
        if (mListener != null) {
            mListener.onSetVisitCount(visitCount);
        }
    }

    private int getVisitCount() {
        String count = mVisitCounterEditText.getText().toString();
        return Utils.getInt(count);
    }


    public interface Listener {
        void onSetVisitCount(int count);
    }
}
