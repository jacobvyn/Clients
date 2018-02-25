package com.alex.devops.views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alex.devops.ClientViewPagerActivity;
import com.alex.devops.R;
import com.alex.devops.db.Client;
import com.alex.devops.utils.Constants;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.ClientViewHolder> implements View.OnClickListener {
    private final List<Client> mClientList = new ArrayList<Client>();
    private Context mContext;

    public ClientsAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ClientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_holder, parent, false);
        return new ClientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClientViewHolder holder, int position) {
        Client client = mClientList.get(position);
        holder.firstName.setText(client.getMainParentFirstName());
        holder.secondName.setText(client.getMainSecondName());
        Picasso.with(mContext).load(new File(client.getMainPhotoPath())).into(holder.imageView);
        holder.rootView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mClientList.size();
    }

    public void setClients(List<Client> clientList) {
        mClientList.clear();
        mClientList.addAll(clientList);
        notifyDataSetChanged();
    }

    public void clear() {
        mClientList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(mContext, ClientViewPagerActivity.class);
        ArrayList<Client> clients = new ArrayList<>(mClientList);
        intent.putParcelableArrayListExtra(Constants.ARG_VIEW_PAGER_CLIENTS, clients);
        mContext.startActivity(intent);
    }

    class ClientViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView firstName;
        private TextView secondName;
        private View rootView;


        public ClientViewHolder(View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.view_holder_root);
            imageView = itemView.findViewById(R.id.find_client_image_view);
            firstName = itemView.findViewById(R.id.client_first_name_text_iew);
            secondName = itemView.findViewById(R.id.client_second_name_text_vew);
        }
    }
}
