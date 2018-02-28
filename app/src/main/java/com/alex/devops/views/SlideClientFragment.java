package com.alex.devops.views;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @BindView(R.id.view_pager_item_name_text_view)
    protected TextView mParentName;
    @BindView(R.id.view_pager_item_photo_image_view)
    protected ImageView mParentPhoto;

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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Client mClient = getArguments().getParcelable(Constants.ARG_NEW_CLIENT);

        mParentName.setText(mClient.getMainParentFirstName());
        Bitmap bitMap = Utils.getBitMap(mClient.getMainBlobPhoto());
        mParentPhoto.setImageBitmap(bitMap);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();
    }
}