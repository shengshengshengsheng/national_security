package com.shengsheng.police.controller.adapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.shengsheng.police.R;
import com.shengsheng.police.model.bean.UserInfo;
import java.util.ArrayList;
import java.util.List;
public class GroupDetailAdapter extends BaseAdapter {
    private Context mContext;
    private OnGroupDetailListener mOnGroupDetailListener;
    private boolean mIsCanModify;//是否允许添加和删除群成员
    private boolean mIsDeleteModel;//删除模式 true表示可以删除，false表示不能删除
    private List<UserInfo> mUsers=new ArrayList<>();
    public GroupDetailAdapter(Context context,boolean isCanModify,OnGroupDetailListener onGroupDetailListener) {
        mContext=context;
        mIsCanModify=isCanModify;
        mOnGroupDetailListener=onGroupDetailListener;
    }
//获取当前的删除模式
    public boolean ismIsDeleteModel() {
        return mIsDeleteModel;
    }
//设置当前的删除模式
    public void setmIsDeleteModel(boolean mIsDeleteModel) {
        this.mIsDeleteModel = mIsDeleteModel;
    }
    //刷新数据
    public void refresh(List<UserInfo> users)
    {
        if(users!=null&&users.size()>=0)
        {
            mUsers.clear();

            //添加加号和减号
            initUsers();
            mUsers.addAll(0,users);
        }
        notifyDataSetChanged();
    }
    private void initUsers() {
        UserInfo add = new UserInfo("add");
        UserInfo delete = new UserInfo("delete");
        mUsers.add(delete);
        mUsers.add(0,add);
    }
    @Override
    public int getCount() {
        return mUsers==null?0:mUsers.size();
    }
    @Override
    public Object getItem(int position) {
        return mUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_group_detail, null);
            holder.photo=convertView.findViewById(R.id.iv_group_detail_photo);
            holder.name=convertView.findViewById(R.id.tv_group_detail_name);
            holder.delete=convertView.findViewById(R.id.iv_group_detail_delete);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        //获取当前item数据
        final UserInfo userInfo = mUsers.get(position);
        if (mIsCanModify){
            //群主或者开放了群邀请
            if (position ==getCount()-1){//减号处理
                if (mIsDeleteModel){
                    //删除模式下的减号
                    convertView.setVisibility(View.INVISIBLE); // 隐藏整个减号
                }else{
                    convertView.setVisibility(View.VISIBLE);//展示整个建号
                    holder.delete.setVisibility(View.GONE);//隐藏小减号
                    holder.name.setVisibility(View.INVISIBLE); //隐藏名字
                    holder.photo.setImageResource(R.drawable.em_smiley_minus_btn_pressed);//设置图片
                }

            }
            else if(position ==getCount()-2){//加号

                if (mIsDeleteModel){
                    //删除模式下的减号
                    convertView.setVisibility(View.GONE); // 隐藏整个减号

                }else{
                    convertView.setVisibility(View.VISIBLE);//展示整个建号
                    holder.delete.setVisibility(View.GONE);//隐藏小减号
                    holder.name.setVisibility(View.INVISIBLE); //隐藏名字
                    holder.photo.setImageResource(R.drawable.em_smiley_add_btn_pressed);//设置图片
                }
            }else{//群成员
                convertView.setVisibility(View.VISIBLE);
                holder.name.setVisibility(View.VISIBLE);
                //根据删除模式决定是否展示小减号
                if (mIsDeleteModel)
                {
                    holder.delete.setVisibility(View.VISIBLE);
                }
                else
                    {
                    holder.delete.setVisibility(View.GONE);
                }

                holder.name.setText(userInfo.getName());
                holder.photo.setImageResource(R.drawable.em_default_avatar);
            }
            //点击事件的处理
            if (position ==getCount()-1){//减号
                holder.photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mIsDeleteModel){
                            mIsDeleteModel = true;
                            notifyDataSetChanged();
                        }
                    }
                });
            }else if(position == getCount()-2){//加号

                holder.photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnGroupDetailListener != null){
                            mOnGroupDetailListener.onAddMembers(userInfo);
                        }
                    }
                });
            }
            else{//群成员

                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnGroupDetailListener != null){
                            mOnGroupDetailListener.onDeleteMembers(userInfo);
                        }
                    }
                });
            }


        }
        else{
            //普通群成员
            if (position ==getCount()-1||position ==getCount()-2){
                convertView.setVisibility(View.GONE);
            }
            else{
                convertView.setVisibility(View.VISIBLE);
                holder.name.setText(userInfo.getName());
                holder.photo.setImageResource(R.drawable.em_default_avatar);
                //删除按钮
                holder.delete.setVisibility(View.GONE);
            }
        }
        return convertView;
    }
    private class ViewHolder
    {
        private ImageView photo;
        private ImageView delete;
        private TextView name;
    }
    public interface OnGroupDetailListener
    {
        //添加群成员方法
        void onAddMembers(UserInfo user);
        //删除群成员方法
        void onDeleteMembers(UserInfo user);
    }

}
