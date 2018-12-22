package com.example.alhuzwiri.wisata;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Dimas Maulana on 5/26/17.
 * Email : araymaulana66@gmail.com
 */

public class RecycleviewAdapter extends RecyclerView.Adapter<RecycleviewAdapter.MahasiswaViewHolder> {


    private ArrayList<post> dataList;

    public RecycleviewAdapter(ArrayList<post> dataList) {
        this.dataList = dataList;
    }

    @Override
    public MahasiswaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.listview, parent, false);
        return new MahasiswaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MahasiswaViewHolder holder, int position) {
        holder.id.setText(dataList.get(position).getUserId());
        //holder.img.setImageBitmap(dataList.get(position).getImage());
        holder.desc.setText(dataList.get(position).getDesc());
        holder.namee.setText(dataList.get(position).getNamee());
        //holder.Image_thumb.setImageBitmap(dataList.get(position).getImage_tumb());

    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }


    public class MahasiswaViewHolder extends RecyclerView.ViewHolder{
        private TextView id,desc,namee;
        private ImageView img,Image_thumb;

        public MahasiswaViewHolder(View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.name);
            desc = (TextView) itemView.findViewById(R.id.descrip);
            img = (ImageView) itemView.findViewById(R.id.thum);
            namee = (TextView) itemView.findViewById(R.id.name);
            Image_thumb = (ImageView) itemView.findViewById(R.id.imageView2);
        }

    }

}