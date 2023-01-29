package com.example.customctl.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.example.customctl.R;
import com.example.customctl.adapter.FriendAdapter;
import com.example.customctl.bean.Friend;

import java.util.List;

public class DialogFriend implements View.OnClickListener, FriendAdapter.OnDeleteListener {

    private final Context mContext;
    private final View view;
    private final TextView tv_title;
    private final ListView lv_friend;
    private final onAddFriendListener mOnAddFriendListener;
    private final List<Friend> mFriendList;
    private final FriendAdapter friendAdapter;
    private final Dialog dialog;

    public DialogFriend(Context context, List<Friend> friendList, onAddFriendListener listener) {
        mContext = context;
        view = LayoutInflater.from(context).inflate(R.layout.dialog_friend, null);
        dialog = new Dialog(context, R.style.dialog_layout_bottom);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        lv_friend = (ListView) view.findViewById(R.id.lv_friend);
        view.findViewById(R.id.tv_ok).setOnClickListener(this);
        mOnAddFriendListener = listener;
        mFriendList = friendList;
        friendAdapter = new FriendAdapter(mContext, mFriendList, this);
        lv_friend.setAdapter(friendAdapter);
    }

    public void show(){
        //设置对话框是否可以取消
        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setContentView(view);
        dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    public void dismiss(){
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    public boolean isShowing(){
        if(dialog != null){
            return dialog.isShowing();
        }else{
            return false;
        }
    }

    public void setCancelable(boolean flag){
        dialog.setCancelable(flag);
    }

    public void setCancelableOnTouchOutside(boolean flag){
        dialog.setCanceledOnTouchOutside(flag);
    }

    public void setTitle(String title){
        tv_title.setText(title);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_ok){
            if (mOnAddFriendListener != null){
                mOnAddFriendListener.addFriend(friendAdapter.getFriends());
                dialog.dismiss();
            }
        }
    }

    @Override
    public void onDeleteClick(int position) {
        mFriendList.remove(position);
        friendAdapter.notifyDataSetChanged();
    }

    public interface onAddFriendListener{
        public void addFriend(List<Friend> friendList);
    }
}
