package com.alex.devops.trash;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jacob on 10.12.2016.
 */
public class CustomAdapter extends BaseAdapter {
    FragmentActivity activity;
    LayoutInflater inflater;
    List<ItemObject> storage;

    public CustomAdapter(FragmentActivity activity, List<ItemObject> allItems) {
        this.activity = activity;
        storage = allItems;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return storage.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        ViewHolder holder;
//        if (convertView == null) {
//            holder = new ViewHolder();
//            convertView = inflater.inflate(R.layout.pop_music_item, parent, false);
//            holder.screenShot = (ImageView) convertView.findViewById(R.id.screen_shot);
//            holder.musicName = (TextView) convertView.findViewById((R.id.music_name));
//            holder.musicAuthor = (TextView) convertView.findViewById(R.id.music_author);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        holder.screenShot.setImageResource(storage.get(position).getScreenShot());
//        holder.musicName.setText(storage.get(position).getMusicName());
//        holder.musicAuthor.setText(storage.get(position).getMusicAuthor());


        return null;
    }

    static class ViewHolder {
        ImageView screenShot;
        TextView musicName;
        TextView musicAuthor;
    }
}
