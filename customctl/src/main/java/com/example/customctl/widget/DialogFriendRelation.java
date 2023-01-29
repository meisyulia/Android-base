package com.example.customctl.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.example.customctl.R;
import com.example.customctl.adapter.FriendRelationAdapter;
import com.example.customctl.util.Utils;

public class DialogFriendRelation implements AdapterView.OnItemClickListener, DialogInterface.OnDismissListener {

    private final Context mContext;
    private final View view;
    private final Dialog dialog;
    private final GridView gv_relation;
    private final LinearLayout ll_layout_gap;
    private final String[] relation_name_array;
    private final String[] relation_value_array;
    private final onSelectRelationListener mOnSelectRelationListener;
    private int mGap;
    private int mSelected;
    private FriendRelationAdapter friendRelationAdapter;

    //主要是有个接口监听被选中的好友关系：传目前的间隔（间接知道现在要修改好友关系的好友的列表下标），好友关系的名字、关系的数值
    public DialogFriendRelation(Context context,onSelectRelationListener listener) {
        mContext = context;
        view = LayoutInflater.from(context).inflate(R.layout.dialog_friend_relation, null);
        dialog = new Dialog(context, R.style.dialog_layout_bottom_transparent);

        gv_relation = (GridView) view.findViewById(R.id.gv_relation);
        ll_layout_gap = (LinearLayout) view.findViewById(R.id.ll_layout_gap);
        //获取需要的数据
        relation_name_array = context.getResources().getStringArray(R.array.relation_name);
        relation_value_array = context.getResources().getStringArray(R.array.relation_value);
        mOnSelectRelationListener = listener;
    }
    //每次弹出对话框需要知道的一些信息:间隔（当前列表项和最底列表项的差数），被选择的选项
    public void show(final int gap,int selected){
        mGap = gap;
        mSelected = selected;
        //1.该对话框显示在一级对话框的上面，对话框的高度的下方需要留点空间显示，点击该空间则关闭该对话框
        // (空间的高度大小是要显示的列表项48dp一项+之后的间隔线1dp+确认项高度46dp)
        int dip_48 = Utils.dip2px(mContext, 48);
        int dip_2 = Utils.dip2px(mContext, 2);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                dip_48 * (gap + 1) - dip_2 + gap);
        ll_layout_gap.setLayoutParams(params);
        ll_layout_gap.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //2.网格视图的填充
        friendRelationAdapter = new FriendRelationAdapter(mContext, relation_name_array, selected);
        gv_relation.setAdapter(friendRelationAdapter);
        gv_relation.setOnItemClickListener(this);
        //3.添加对话框布局，宽高，位置
        dialog.getWindow().setContentView(view);
        dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
        //对对话框消失时监听，每次消失做一些处理，比如把间隔、选的关系和选的关系的值传给选择关系的监听接口
        dialog.setOnDismissListener(this);
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mSelected = i;
        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        if(mOnSelectRelationListener != null){
            mOnSelectRelationListener.setRelation(mGap,
                    relation_name_array[mSelected],relation_value_array[mSelected] );
        }

    }

    public interface onSelectRelationListener{
        public void setRelation(int gap,String name,String value);
    }
}
