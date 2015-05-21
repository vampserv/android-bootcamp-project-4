package com.codepath.apps.twitterclient.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.helpers.DynamicImageView;
import com.codepath.apps.twitterclient.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by edwardyang on 5/13/15.
 */
public class MediaAdapter extends ArrayAdapter<String> {

    List<String> list;

    public MediaAdapter(Context context, List<String> images) {
        super(context, android.R.layout.simple_list_item_1, images);
        list = images;
    }

    private static class ViewHolder {
        DynamicImageView ivImage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String mediaUrl = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_media, parent, false);
            viewHolder.ivImage = (DynamicImageView) convertView.findViewById(R.id.ivImage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.ivImage.setImageResource(0);
        Picasso.with(getContext()).load(mediaUrl).placeholder(R.drawable.placeholder).into(viewHolder.ivImage);

        return convertView;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }
}
