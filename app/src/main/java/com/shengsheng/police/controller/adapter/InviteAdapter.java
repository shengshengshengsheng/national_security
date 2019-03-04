package com.shengsheng.police.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.shengsheng.police.R;
import com.shengsheng.police.model.bean.GroupInfo;
import com.shengsheng.police.model.bean.InvitationInfo;
import com.shengsheng.police.model.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;
//邀请信息列表页面的适配器
public class InviteAdapter extends BaseAdapter {
    private Context mContext;
    private InvitationInfo invitationInfo;
    private List<InvitationInfo> mInvitationInfos = new ArrayList<>();
    private OnInviteListener mOnInviteListener;
    public InviteAdapter(Context context,OnInviteListener OnInviteListener) {
        this.mContext = context;
        mOnInviteListener=OnInviteListener;
    }

    //刷新数据的方法
    public void refresh(List<InvitationInfo> invitationInfos) {
        //校验
        if (invitationInfos != null&&invitationInfos.size()>=0) {
            //添加数据
            mInvitationInfos.clear();
            mInvitationInfos.addAll(invitationInfos);
            //刷新界面
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mInvitationInfos == null ? 0 : mInvitationInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mInvitationInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //创建holder
        ViewHolder holder = null;
        if (convertView == null) {
            //创建convertView
            holder=new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_invite, null);
            holder.name = convertView.findViewById(R.id.tv_invite_name);
            holder.reason = convertView.findViewById(R.id.tv_invite_reason);
            holder.accept = convertView.findViewById(R.id.bt_invite_accept);
            holder.reject = convertView.findViewById(R.id.bt_invite_reject);
            convertView.setTag(holder);
        }
        else
            {
            holder = (ViewHolder) convertView.getTag();
        }
        //绑定数据
        invitationInfo = mInvitationInfos.get(position);
        UserInfo user = this.invitationInfo.getUserInfo();
        if (user != null)
        //联系人邀请
        {
            //名称的展示
            holder.name.setText(this.invitationInfo.getUserInfo().getName());
            //隐藏button
            holder.accept.setVisibility(View.GONE);
            holder.reject.setVisibility(View.GONE);
            if (invitationInfo.getStatus() == InvitationInfo.InvitationStatus.NEW_INVITE) {//新邀请
                //展示button
                holder.reject.setVisibility(View.VISIBLE);
                holder.accept.setVisibility(View.VISIBLE);
                //设置reason
                if (invitationInfo.getReason() == null) {
                    holder.reason.setText("添加好友");
                } else {
                    holder.reason.setText(invitationInfo.getReason());
                }
            }
                else if (invitationInfo.getStatus() == InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER) {//邀请被接受
                if (invitationInfo.getReason() == null) {
                    holder.reason.setText("邀请被接受");
                } else {
                    holder.reason.setText(this.invitationInfo.getReason());
                }
                }
                else if (invitationInfo.getStatus() == InvitationInfo.InvitationStatus.INVITE_ACCEPT)
                { //接受邀请
                if (invitationInfo.getReason() == null) {
                    holder.reason.setText("接受邀请");
                } else {
                    holder.reason.setText(invitationInfo.getReason());
                }
                }
        //接受按钮的监听
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnInviteListener != null) {
                    mOnInviteListener.onAccept(invitationInfo);
                }
            }
        });
        //拒绝按钮的监听
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnInviteListener != null) {
                    mOnInviteListener.onReject(invitationInfo);
                }
            }
        });
    }
        else {
            //群组的邀请信息
        }
        //返回布局
        return convertView;
    }
    private class ViewHolder {
        private TextView name;
        private TextView reason;
        private Button accept;
        private Button reject;

    }
    public interface OnInviteListener
    {
        //联系人接受按钮的点击事件
        void onAccept(InvitationInfo invitationInfo);
        //联系人拒绝按钮的点击事件
        void onReject( InvitationInfo invitationInfo);
    }
}
