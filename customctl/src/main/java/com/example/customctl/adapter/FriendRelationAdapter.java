package com.example.customctl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.customctl.R;

import java.util.ArrayList;

public class FriendRelationAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<String> mContextList;
    private final int mSelected;
    private final LayoutInflater mInflater;

    public FriendRelationAdapter(Context context, String[] content_list, int selected) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mContextList = new ArrayList<>();
        for (int i = 0; i < content_list.length; i++) {
            mContextList.add(content_list[i]);
        }
        mSelected = selected;

    }

    @Override
    public int getCount() {
        return mContextList.size();
    }

    @Override
    public Object getItem(int i) {
        return mContextList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item_friend_relation, null);
            holder = new ViewHolder();
            holder.tv_friend_relation = (TextView) convertView.findViewById(R.id.tv_friend_relation);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_friend_relation.setText(mContextList.get(position));
        if (position == mSelected){
            holder.tv_friend_relation.setBackgroundResource(R.color.blue);
            holder.tv_friend_relation.setTextColor(mContext.getResources().getColor(R.color.white));
        }
        return convertView;
    }

    public final class ViewHolder{
        TextView tv_friend_relation;
    }
}
