package com.alex.devops.views;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alex.devops.R;
import com.alex.devops.commons.BaseActivity;

public class SyncDialog extends DialogFragment implements DialogInterface.OnShowListener {
    public static String TAG = SyncDialog.class.getSimpleName();
    private Listener mListener;
    private EditText mURLEditText;
    private String mOldBaseUrl;

    public static SyncDialog newInstance() {
        return new SyncDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.sync_dialog_layout, null);
        mURLEditText = (EditText) view.findViewById(R.id.sync_dialog_url_edit_text);
        mOldBaseUrl = getBaseURL();
        mURLEditText.setText(mOldBaseUrl);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.data_base_reload);
        builder.setMessage(R.string.notice_db_reload);
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

    private String getBaseURL() {
        return getActivity() instanceof BaseActivity ? ((BaseActivity) getActivity()).getBaseURL() : "";
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setListener(Listener listener) {
        mListener = listener;
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
        if (isURLValid()) {
            checkBaseUrl();
            onSyncConfirmed();
            dismiss();
        } else {
            Toast.makeText(getActivity(), "Url is not valid", Toast.LENGTH_SHORT).show();
        }
    }

    private String getNewBaseURL() {
        if (mURLEditText != null) {
            return mURLEditText.getText().toString();
        } else {
            return "";
        }
    }

    private void onSyncConfirmed() {
        if (mListener != null) {
            mListener.onSyncConfirmed();
        }
    }

    private void checkBaseUrl() {
        String newBaseURL = getNewBaseURL();
        if (!mOldBaseUrl.equals(newBaseURL)) {
            onBaseURLChanged(newBaseURL);
        }
    }

    private void onBaseURLChanged(String newBaseURL) {
        if (mListener != null) {
            mListener.onBaseURLChanged(newBaseURL);
        }
    }

    private boolean isURLValid() {
        int minLength = getActivity().getString(R.string.https_protocol).length();
        final String url = mURLEditText.getText().toString();
        return mURLEditText != null && URLUtil.isNetworkUrl(url) && url.length() > minLength;
    }

    public interface Listener {
        void onSyncConfirmed();

        void onBaseURLChanged(String newBaseURL);
    }
}
