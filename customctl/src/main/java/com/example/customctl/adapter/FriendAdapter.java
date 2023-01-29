package com.example.customctl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.customctl.R;
import com.example.customctl.bean.Friend;
import com.example.customctl.widget.DialogFriendRelation;

import java.util.List;

public class FriendAdapter extends BaseAdapter implements DialogFriendRelation.onSelectRelationListener {

    private final Context mContext;
    private final List<Friend> mFriendList;
    private final String[] names;
    private final OnDeleteListener deleteListener;
    private ViewHolder holder;

    public FriendAdapter(Context context, List<Friend> friendList, OnDeleteListener listener) {
        mContext = context;
        mFriendList = friendList;
        names = mContext.getResources().getStringArray(R.array.relation_name);
        deleteListener = listener;
    }

    @Override
    public int getCount() {
        return mFriendList.size();
    }

    @Override
    public Object getItem(int i) {
        return mFriendList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_friend,null);
            holder.rl_relation = (RelativeLayout) convertView.findViewById(R.id.rl_relation);
            holder.tv_relation = (TextView) convertView.findViewById(R.id.tv_relation);
            holder.ib_dropdown = (ImageButton) convertView.findViewById(R.id.ib_dropdown);
            holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
            holder.rg_admit = (RadioGroup) convertView.findViewById(R.id.rg_admit);
            holder.rb_true = (RadioButton) convertView.findViewById(R.id.rb_true);
            holder.rb_false = (RadioButton) convertView.findViewById(R.id.rb_false);
            holder.tv_operation = (TextView) convertView.findViewById(R.id.tv_operation);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_relation.setText(mFriendList.get(i).relation);
        holder.tv_phone.setText(mFriendList.get(i).phone);
        holder.rg_admit.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.rb_true){
                    mFriendList.get(i).admit_circle = true;
                }else if(checkedId == R.id.rb_false){
                    mFriendList.get(i).admit_circle = false;
                }
            }
        });
        holder.tv_operation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deleteListener != null){
                    deleteListener.onDeleteClick(i);
                }
            }
        });
        //对关系改变的监听
        setRelationChangeListener(holder,i);
        //关闭关系对话框后将背景设置成白色
        holder.rl_relation.setBackgroundResource(R.color.white);
        return convertView;
    }

    private void setRelationChangeListener(final ViewHolder holder,final int i) {
        holder.rl_relation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int selected = 0;
                for (int j = 0; j < names.length; j++) {
                    if (names[j].equals(mFriendList.get(i).relation)){
                        //知道该朋友的关系的下标
                        selected = j;
                        break;
                    }
                }
                //点击后需要展示关系的对话框
                //新建关系对话框
                DialogFriendRelation dialog = new DialogFriendRelation(mContext, FriendAdapter.this);
                //展示对话框(注意展示的位置和展示试默认选择的关系）
                // 难点1：这是二级对话框要在一级对话框上面进行展示需要给下面的对话框留间隔空间展示好友列表加一个确认TextView
                dialog.show(getCount()- i,selected);
                //将关系控件背景设置成灰色表示被点击中
                holder.rl_relation.setBackgroundResource(R.color.grey);
            }
        });
    }

    //获取好友列表
    public List<Friend> getFriends(){
        return mFriendList;
    }

    @Override
    public void setRelation(int gap, String name, String value) {
        //getCount()-gap = position
        mFriendList.get(getCount()-gap).relation = name;
        mFriendList.get(getCount()-gap).value = value;

        notifyDataSetChanged();
    }

    public interface OnDeleteListener{
        public void onDeleteClick(int position);
    }
    class ViewHolder{
        RelativeLayout rl_relation;
        TextView tv_relation;
        ImageButton ib_dropdown;
        TextView tv_phone;
        RadioGroup rg_admit;
        RadioButton rb_true;
        RadioButton rb_false;
        TextView tv_operation;
    }
}
