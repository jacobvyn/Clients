package com.alex.devops.views;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alex.devops.R;
import com.alex.devops.db.Client;
import com.alex.devops.utils.Constants;
import com.alex.devops.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SlideClientFragment extends Fragment {
    private Unbinder mUnBinder;

    @BindView(R.id.pager_child_first_name_text_view)
    protected TextView mChildName;
    @BindView(R.id.pager_birth_day_text_view)
    protected TextView mChildBirthDay;

    @BindView(R.id.pager_parent_name_text_view)
    protected TextView mMainParentName;
    @BindView(R.id.pager_second_name_text_view)
    protected TextView mMainParentSurname;
    @BindView(R.id.pager_patronymic_name_text_view)
    protected TextView mMainParentPatronymicName;
    @BindView(R.id.pager_phone_number_text_view)
    protected TextView mMainParentPhone;
    @BindView(R.id.pager_parent_photo_image_view)
    protected ImageView mPrimaryParentPhoto;
    @BindView(R.id.pager_second_parent_container)
    protected FrameLayout mSecondParentContainer;

    @BindView(R.id.pager_separator)
    protected View mSeparator;

    private ImageView mSecondParentPhoto;
    private TextView mSecondParentName;
    private TextView mSecondParentSurname;
    private TextView mSecondParentPatronymic;
    private TextView mSecondParentPhoneNumber;
    private Client mClient;

    public static Fragment newInstance(Client client) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.ARG_NEW_CLIENT, client);
        SlideClientFragment fragment = new SlideClientFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_pager_item_layout, container, false);
        mUnBinder = ButterKnife.bind(this, view);
        mClient = getArguments().getParcelable(Constants.ARG_NEW_CLIENT);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setChildData();
        setMainParentData();
        if (mClient.hasSecondParent()) {
            initSecondParent();
        }
    }

    private void initSecondParent() {
        mSeparator.setVisibility(View.VISIBLE);
        View secondParentView = getLayoutInflater().inflate(R.layout.pager_parent_view_layout, mSecondParentContainer, true);
        initSecondParentViews(secondParentView);
        setSecondParentData();
    }

    private void setSecondParentData() {
        Bitmap bitMap = Utils.getBitMap(mClient.getSecondPhotoBlob());
        mSecondParentPhoto.setImageBitmap(bitMap);
        mSecondParentName.setText(mClient.getSecondParentFirstName());
        mSecondParentSurname.setText(mClient.getSecondParentSecondName());
        mSecondParentPhoneNumber.setText(mClient.getSecondPhoneNumber());

        String patronymicName = mClient.getSecondPatronymicName();
        if (TextUtils.isEmpty(patronymicName)) {
            mSecondParentPatronymic.setVisibility(View.GONE);
        } else {
            mSecondParentPatronymic.setText(patronymicName);
            mSecondParentPatronymic.setVisibility(View.VISIBLE);
        }
    }

    private void initSecondParentViews(View view) {
        mSecondParentPhoto = (ImageView) view.findViewById(R.id.pager_parent_photo_image_view);
        mSecondParentName = (TextView) view.findViewById(R.id.pager_parent_name_text_view);
        mSecondParentSurname = (TextView) view.findViewById(R.id.pager_second_name_text_view);
        mSecondParentPatronymic = (TextView) view.findViewById(R.id.pager_patronymic_name_text_view);
        mSecondParentPhoneNumber = (TextView) view.findViewById(R.id.pager_phone_number_text_view);
    }

    private void setMainParentData() {
        Bitmap bitMap = Utils.getBitMap(mClient.getMainBlobPhoto());
        mPrimaryParentPhoto.setImageBitmap(bitMap);

        mMainParentName.setText(mClient.getMainParentFirstName());
        mMainParentSurname.setText(mClient.getMainSecondName());
        mMainParentPhone.setText(mClient.getMainPhoneNumber());
        String patronymicName = mClient.getMainPatronymicName();
        if (TextUtils.isEmpty(patronymicName)) {
            mMainParentPatronymicName.setVisibility(View.GONE);
        } else {
            mMainParentPatronymicName.setText(patronymicName);
            mMainParentPatronymicName.setVisibility(View.VISIBLE);
        }
    }

    private void setChildData() {
        mChildName.setText(mClient.getChildName());
        mChildBirthDay.setText(Utils.getFormattedDate(mClient.getChildBirthDay()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();
    }
}