package com.alex.devops;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alex.devops.db.Client;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.ClientViewHolder> {
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

    class ClientViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView firstName;
        private TextView secondName;

        public ClientViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.find_client_image_view);
            firstName = itemView.findViewById(R.id.client_first_name_text_iew);
            secondName = itemView.findViewById(R.id.client_second_name_text_vew);
        }
    }
}
