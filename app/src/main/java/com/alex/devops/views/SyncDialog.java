package com.alex.devops.views;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.alex.devops.R;

public class SyncDialog extends DialogFragment {
    public static String TAG = SyncDialog.class.getSimpleName();
    private Listener mListener;

    public static SyncDialog newInstance() {
        return new SyncDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.data_base_reload);
        builder.setMessage(R.string.notice_db_reload);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mListener != null) {
                    mListener.onSyncConfirmed();
                }
            }
        });
        builder.setNegativeButton(R.string.abort, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        return builder.create();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public interface Listener {
        void onSyncConfirmed();
    }
}
