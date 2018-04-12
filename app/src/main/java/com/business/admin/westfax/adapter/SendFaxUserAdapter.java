package com.business.admin.westfax.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.business.admin.westfax.R;
import com.business.admin.westfax.retrofit.ResExpandList;

import java.util.ArrayList;

/**
 * Created by SONY on 01-02-2018.
 */

public class SendFaxUserAdapter extends RecyclerView.Adapter<SendFaxUserAdapter.ViewHolderr> {
    public static ArrayList<ResExpandList> items = new ArrayList<ResExpandList>();
    Context context;


    public SendFaxUserAdapter(Context context, ArrayList<ResExpandList> data) {
        this.context = context;
        items = data;
    }


    public class ViewHolderr extends RecyclerView.ViewHolder {
        EditText edtlisusr;
        ImageView imgminusr;

        public ViewHolderr(View itemView) {
            super(itemView);
            edtlisusr = (EditText) itemView.findViewById(R.id.edtlisusr);
            imgminusr = (ImageView) itemView.findViewById(R.id.imgminusr);

        }
    }

    @Override
    public ViewHolderr onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_sendfaxusrs, parent, false);
        ViewHolderr viewholder = new ViewHolderr(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(ViewHolderr holder, final int position) {
        final ResExpandList listitem = items.get(position);

        holder.edtlisusr.setText(listitem.getString2Value());
        holder.imgminusr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int positionToRemove = position;
                items.remove(positionToRemove);
                notifyDataSetChanged();

            }
        });

    }

    public void notifyData(ArrayList<ResExpandList> myList) {
//        Log.d("notifyData ", myList.size() + "");
        this.items = myList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (null != items ? items.size() : 0);
    }


}
