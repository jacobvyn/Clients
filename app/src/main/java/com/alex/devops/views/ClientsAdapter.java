package com.alex.devops.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.alex.devops.ClientViewPagerActivity;
import com.alex.devops.R;
import com.alex.devops.db.Client;
import com.alex.devops.utils.Constants;
import com.alex.devops.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.ClientViewHolder> implements View.OnClickListener {
    private final List<Client> mClientList = new ArrayList<Client>();
    private Context mContext;
    private OnItemClickListener mListener;

    public ClientsAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ClientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_holder_new, parent, false);
        return new ClientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClientViewHolder holder, int position) {
        Client client = mClientList.get(position);
        setOnclickListener(holder.rootView, position);


        holder.first.setText(client.getMainParentFirstName());
        holder.second.setText(client.getMainSecondName());

        String patronymicName = client.getMainPatronymicName();
        if (TextUtils.isEmpty(patronymicName)) {
            holder.patronymic.setVisibility(View.GONE);
        } else {
            holder.patronymic.setText(patronymicName);
            holder.patronymic.setVisibility(View.VISIBLE);
        }

        holder.phone.setText(client.getMainPhoneNumber());

        Bitmap bitMap = Utils.getBitMap(client.getMainBlobPhoto());
        holder.photo.setImageBitmap(bitMap);
    }

    private void setOnclickListener(View view, final int currPosition) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClicked(currPosition);
                }
            }
        });
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

    }

    public void setListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public ArrayList<Client> getClients() {
        return new ArrayList<Client>(mClientList);
    }

    public ArrayList<Integer> getClientsIds() {
        ArrayList<Integer> idsList = new ArrayList<Integer>();
        for (Client client : mClientList) {
            idsList.add(client.getId());
        }
        return idsList;
    }

    protected class ClientViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.client_photo_image_view)
        protected ImageView photo;
        @BindView(R.id.parent_first_name_edit_text)
        protected EditText first;
        @BindView(R.id.second_name_edit_text)
        protected EditText second;
        @BindView(R.id.patronymic_name_edit_text)
        protected EditText patronymic;
        @BindView(R.id.phone_number_edit_text)
        protected EditText phone;
        @BindView(R.id.view_holder_root)
        protected View rootView;


        public ClientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Utils.disableView(first);
            Utils.disableView(second);
            Utils.disableView(patronymic);
            Utils.disableView(phone);
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(int currPosition);
    }
}
