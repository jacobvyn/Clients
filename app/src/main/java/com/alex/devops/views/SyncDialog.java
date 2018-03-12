package com.alex.devops.views;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alex.devops.BuildConfig;
import com.alex.devops.R;
import com.alex.devops.commons.BaseActivity;
import com.alex.devops.db.Credentials;

public class SyncDialog extends DialogFragment implements DialogInterface.OnShowListener {
    public static String TAG = SyncDialog.class.getSimpleName();
    private Listener mListener;
    private EditText mDomainEditText;
    private String mOldDomain;
    private View mSettingsLayout;
    private EditText mLoginEditText;
    private EditText mPasswordEditText;
    private String mOldLogin;
    private String mOldPassword;


    public static SyncDialog newInstance() {
        return new SyncDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mSettingsLayout = getActivity().getLayoutInflater().inflate(R.layout.sync_dialog_layout, null);
        mDomainEditText = (EditText) mSettingsLayout.findViewById(R.id.sync_dialog_domain_edit_text);
        mLoginEditText = (EditText) mSettingsLayout.findViewById(R.id.sync_dialog_login_edit_text);
        mPasswordEditText = (EditText) mSettingsLayout.findViewById(R.id.sync_dialog_password_edit_text);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.data_base_reload);
        builder.setMessage(R.string.notice_db_reload);
        builder.setView(mSettingsLayout);
        builder.setPositiveButton(R.string.ok, null);
        builder.setNegativeButton(R.string.abort, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNeutralButton(R.string.settings, null);

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(this);
        return dialog;
    }

    private void onSettings() {
        if (mSettingsLayout.getVisibility() == View.GONE) {
            mSettingsLayout.setVisibility(View.VISIBLE);
            setFields();
        } else {
            mSettingsLayout.setVisibility(View.GONE);
        }
    }

    private void setFields() {
        Credentials credentials = getCredentials();
        mOldDomain = credentials.getDomain();
        mOldLogin = credentials.getLogin();
        mOldPassword = credentials.getPassword();

        if (!TextUtils.isEmpty(mOldDomain)) {
            mDomainEditText.setText(mOldDomain);
        }
        if (!TextUtils.isEmpty(mOldLogin)) {
            mLoginEditText.setText(mOldLogin);
        } else {
            mLoginEditText.setText(BuildConfig.LOGIN_MOCK);
        }
        if (!TextUtils.isEmpty(mOldPassword)) {
            mPasswordEditText.setText(mOldPassword);
        } else {
            mPasswordEditText.setText(BuildConfig.PASSWORD_MOCK);
        }
    }

    private boolean isSettingEnabled() {
        return mSettingsLayout != null && mSettingsLayout.getVisibility() == View.VISIBLE;
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
        AlertDialog dialog = (AlertDialog) dialogInterface;
        Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOkClicked();
            }
        });

        Button settings = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSettings();
            }
        });
    }

    private void onOkClicked() {
        if (isSettingEnabled()) {
            checkAndSetNewCredentials();
        } else {
            checkSavedCredentials();
        }
    }

    private void checkSavedCredentials() {
        if (getCredentials().isValid()) {
            syncAndDismiss();
        } else {
            Toast.makeText(getActivity(), R.string.error_settings_all, Toast.LENGTH_SHORT).show();
        }
    }

    private void syncAndDismiss() {
        onSyncConfirmed();
        dismiss();
    }

    private void checkAndSetNewCredentials() {
        if (isSettingsValid()) {
            setNewCredentials();
            syncAndDismiss();
        }
    }

    private boolean isSettingsValid() {
        boolean domainValid = isDomainValid();
        boolean loginValid = isLoginValid();
        boolean passwordValid = isPasswordValid();

        if (!domainValid) {
            Toast.makeText(getActivity(), R.string.error_settings_domain, Toast.LENGTH_LONG).show();
            return false;
        }

        if (!loginValid) {
            Toast.makeText(getActivity(), R.string.error_settings_login, Toast.LENGTH_LONG).show();
            return false;
        }

        if (!passwordValid) {
            Toast.makeText(getActivity(), R.string.error_settings_password, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean isPasswordValid() {
        String password = mPasswordEditText != null ? mPasswordEditText.getText().toString().trim() : "";
        return password.length() > 5;
    }

    private boolean isLoginValid() {
        String login = mLoginEditText != null ? mLoginEditText.getText().toString().trim() : "";
        return login.length() > 5;
    }

    private void onSyncConfirmed() {
        if (mListener != null) {
            mListener.onSyncConfirmed();
        }
    }

    private void setNewCredentials() {
        String login = getLogin();
        String password = getPassword();
        String domain = getDomain();
        onCredentialsChanged(new Credentials(login, password, domain));
    }

    private String getPassword() {
        return mPasswordEditText != null ? mPasswordEditText.getText().toString() : "";
    }

    private String getLogin() {
        return mLoginEditText != null ? mLoginEditText.getText().toString() : "";
    }

    private String getDomain() {
        return mDomainEditText != null ? mDomainEditText.getText().toString() : "";
    }

    private void onCredentialsChanged(Credentials credentials) {
        if (mListener != null) {
            mListener.onCredentialsChanged(credentials);
        }
    }

    private boolean isDomainValid() {
        int minLength = getActivity().getString(R.string.https_protocol).length();
        final String domain = mDomainEditText.getText().toString();
        return mDomainEditText != null && URLUtil.isNetworkUrl(domain) && domain.length() > minLength;
    }

    private Credentials getCredentials() {
        return getActivity() instanceof BaseActivity ? ((BaseActivity) getActivity()).getCredentials() : new Credentials();
    }


    public interface Listener {
        void onSyncConfirmed();

        void onCredentialsChanged(Credentials credentials);
    }
}
