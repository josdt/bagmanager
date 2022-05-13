package com.example.bagsmanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bagsmanager.HomeActivity;
import com.example.bagsmanager.Model.ItemMenu;
import com.example.bagsmanager.R;

import java.util.List;

public class MenuAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<ItemMenu> list;

    public MenuAdapter(Context context, int layout, List<ItemMenu> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    private class ViewHolder{
        TextView tv;
        ImageView iv;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view= layoutInflater.inflate(layout, null);
            viewHolder= new ViewHolder();

            viewHolder.tv= (TextView)  view.findViewById(R.id.tvNameItem);
            viewHolder.iv = (ImageView)  view.findViewById(R.id.ivIcon);

            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }

        viewHolder.tv.setText(list.get(i).nameItem);
        viewHolder.iv.setImageResource(list.get(i).icon);

        viewHolder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity)context).itemAction(list.get(i).id);
            }
        });

        return view;
    }
}
