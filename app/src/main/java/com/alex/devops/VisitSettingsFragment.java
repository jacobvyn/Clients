package com.alex.devops;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alex.devops.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class VisitSettingsFragment extends DialogFragment implements View.OnClickListener {
    public static String TAG = VisitSettingsFragment.class.getName();
    protected Unbinder mUnBinder;
    private Listener mListener;

    @BindView(R.id.settings_dialog_cancel_button)
    protected Button mCancelButton;
    @BindView(R.id.settings_dialog_ok_button)
    protected Button mOkButton;
    @BindView(R.id.settings_dialog_visit_count_edit_text)
    protected EditText mMaxVisitCountEditText;
    @BindView(R.id.settings_dialog_root_layout)
    protected View mRootView;


    public static VisitSettingsFragment newInstance() {
        VisitSettingsFragment fragment = new VisitSettingsFragment();
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogStyle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_visit_settings_layout, container, false);
        mUnBinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCancelButton.setOnClickListener(this);
        mOkButton.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.settings_dialog_cancel_button: {
                dismiss();
                break;
            }
            case R.id.settings_dialog_ok_button: {
                onOkPressed();
                break;
            }
        }
    }

    private void onOkPressed() {
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
            mListener.onOkPressed(visitCount);
        }
    }

    private int getVisitCount() {
        String count = mMaxVisitCountEditText.getText().toString();
        return Utils.getInt(count);
    }

    public interface Listener {
        void onOkPressed(int count);
    }
}
