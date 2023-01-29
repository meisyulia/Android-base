package com.example.customctl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.customctl.R;
import com.example.customctl.bean.AppInfo;
import com.example.customctl.bean.StringUtil;

import java.util.ArrayList;

public class TrafficInfoAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<AppInfo> mAppInfoList;

    public TrafficInfoAdapter(Context context, ArrayList<AppInfo> appInfoList) {
        mContext = context;
        mAppInfoList = appInfoList;
    }

    @Override
    public int getCount() {
        return mAppInfoList.size();
    }

    @Override
    public Object getItem(int i) {
        return mAppInfoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView ==  null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_traffic, null);
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.tv_label = (TextView) convertView.findViewById(R.id.tv_label);
            holder.tv_package_name = (TextView) convertView.findViewById(R.id.tv_package_name);
            holder.tv_traffic = (TextView) convertView.findViewById(R.id.tv_traffic);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        AppInfo info = mAppInfoList.get(position);
        if (info.icon != null){
            holder.iv_icon.setImageDrawable(info.icon);
        }
        holder.tv_label.setText(info.label);
        holder.tv_package_name.setText(info.package_name);
        holder.tv_traffic.setText(StringUtil.formatData(info.traffic));
        return convertView;
    }

    public final class ViewHolder{
        public ImageView iv_icon;
        public TextView tv_label;
        public TextView tv_package_name;
        public TextView tv_traffic;
    }
}
