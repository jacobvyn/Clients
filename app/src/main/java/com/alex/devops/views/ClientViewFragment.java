package com.alex.devops.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.alex.devops.R;
import com.alex.devops.db.Client;
import com.alex.devops.db.Human;
import com.alex.devops.db.Parent;

public class ClientViewFragment extends Fragment {
    public static final String TAG = ClientViewFragment.class.getName();

    private ChildFragment mChildFragment;
    private ParentViewFragment mSecondParent;
    private ParentViewFragment mMainParent;
    private boolean mIsAddParent = true;
    private OnParentsChanged mParentsListener;
    private View mRootView;

    public static ClientViewFragment newInstance() {
        return new ClientViewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_view_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRootView = view;
        mChildFragment = ChildFragment.newInstance();
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.child_root_container, mChildFragment)
                .commit();
        mMainParent = ParentViewFragment.newInstance(false);
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.main_parent_root_container, mMainParent)
                .commit();

        addFreeSpace(true);
    }

    private void addFreeSpace(final boolean add) {
        mRootView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (add) {
                    mMainParent.addFreeSpace();
                } else {
                    mMainParent.removeFreeSpace();
                }
            }
        }, 100);
    }

    public void onAddParentClicked() {
        if (mIsAddParent) {
            addSecondParentFragment();
            addFreeSpace(false);
        } else {
            removeSecondParentFragment();
            addFreeSpace(true);
        }
    }

    private void addSecondParentFragment() {
        mSecondParent = ParentViewFragment.newInstance(true);
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.second_parent_root_container, mSecondParent)
                .commit();
        mIsAddParent = false;
        onParentsChanged(true);
    }

    private void removeSecondParentFragment() {
        getChildFragmentManager()
                .beginTransaction()
                .remove(mSecondParent)
                .commit();
        mSecondParent = null;
        mIsAddParent = true;
        onParentsChanged(false);
    }

    private boolean validateTextField(EditText editText, int length) {
        return editText != null && editText.getText().toString().length() >= length;
    }


    public Client getClient() {
        Human child = mChildFragment.getChild();
        Parent mainParent = mMainParent.getParent();

        Client client = new Client();
        client.setChildName(child.getFirstName());
        client.setChildBirthDay(child.getBirthDay());
        client.setMainParent(mainParent);

        if (mSecondParent != null) {
            Parent secondParent = mSecondParent.getParent();
            client.setSecondParent(secondParent);
        }
        return client;
    }

    public boolean checkInputData() {
        if (mChildFragment != null && !mChildFragment.checkInputData()) {
            return false;
        }

        if (mMainParent != null && !mMainParent.checkInputData()) {
            return false;
        }

        if (mSecondParent != null && !mSecondParent.checkInputData()) {
            return false;
        }
        return true;
    }

    public void setOnParentsChangedListener(OnParentsChanged listener) {
        mParentsListener = listener;
    }

    private void onParentsChanged(boolean added) {
        if (mParentsListener != null) {
            mParentsListener.onParentsChanged(added);
        }
    }

    public interface OnParentsChanged {
        void onParentsChanged(boolean added);
    }
}
