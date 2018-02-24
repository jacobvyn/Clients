package com.alex.devops;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alex.devops.db.Client;
import com.example.test.app.jacob.mygalleryapp.R;

import java.util.List;

public class SearchFragment extends Fragment {
    public static String TAG = SearchFragment.class.getName();
    private RecyclerView mRecycleView;
    private ClientsAdapter mClientsAdapter;
    private View mNoResultView;

    public SearchFragment() {
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mClientsAdapter = new ClientsAdapter(getActivity());
        return inflater.inflate(R.layout.search_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecycleView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mNoResultView = view.findViewById(R.id.no_result_image);
        mNoResultView.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.setAdapter(mClientsAdapter);
    }

    public void onSearchFinished(List<Client> clients) {
        mClientsAdapter.setClients(clients);
        showNoResult(clients.size());
    }

    private void showNoResult(int size) {
        if (size == 0) {
            mNoResultView.setVisibility(View.VISIBLE);
            mRecycleView.setVisibility(View.GONE);
        } else {
            mNoResultView.setVisibility(View.GONE);
            mRecycleView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        mClientsAdapter.clear();
        super.onDestroy();
    }

    public void clear() {
        mClientsAdapter.clear();
        mNoResultView.setVisibility(View.GONE);
    }
}
