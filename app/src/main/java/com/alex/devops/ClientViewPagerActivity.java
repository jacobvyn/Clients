package com.alex.devops;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alex.devops.R;
import com.alex.devops.commons.SimpleActivity;
import com.alex.devops.db.Client;
import com.alex.devops.utils.Constants;
import com.alex.devops.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import static com.alex.devops.utils.Constants.ARG_CLIENT;

/**
 * Created by vynnykiakiv on 2/25/18.
 */

public class ClientViewPagerActivity extends SimpleActivity {
    private ViewPager mViewPager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clients_view_pager_layout);
        findViewById(R.id.view_pager_root_view).setBackgroundColor(getBackgroundColor());

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new ClientsViewPagerAdapter(getSupportFragmentManager(), getClients()));
    }

    private ArrayList<Client> getClients() {
        return getIntent().getParcelableArrayListExtra(Constants.ARG_VIEW_PAGER_CLIENTS);
    }

    private class ClientsViewPagerAdapter extends FragmentStatePagerAdapter {
        private ArrayList<Client> mClients;

        public ClientsViewPagerAdapter(FragmentManager supportFragmentManager, ArrayList<Client> clients) {
            super(supportFragmentManager);
            mClients = clients;
        }

        @Override
        public int getCount() {
            return mClients.size();
        }

        @Override
        public Fragment getItem(int position) {
            return SlideClientFragment.newInstance(mClients.get(position));
        }
    }

    public static class SlideClientFragment extends Fragment {


        public static Fragment newInstance(Client client) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(ARG_CLIENT, client);
            SlideClientFragment fragment = new SlideClientFragment();
            fragment.setArguments(bundle);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.view_pager_item_layout, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            Client client = getArguments().getParcelable(ARG_CLIENT);

            TextView name = view.findViewById(R.id.view_pager_item_name_text_view);
            ImageView photo = view.findViewById(R.id.view_pager_item_photo_image_view);
            name.setText(client.getMainParentFirstName());
            byte[] mainBlobPhoto = client.getMainBlobPhoto();
            photo.setImageBitmap(Utils.getBitMap(mainBlobPhoto));
        }
    }


}
