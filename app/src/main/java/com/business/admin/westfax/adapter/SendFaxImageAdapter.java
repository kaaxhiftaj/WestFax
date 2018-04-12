package com.business.admin.westfax.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.business.admin.westfax.R;
import com.business.admin.westfax.retrofit.ResExpandList;

import java.util.ArrayList;

/**
 * Created by SONY on 20-02-2018.
 */

public class SendFaxImageAdapter extends RecyclerView.Adapter<SendFaxImageAdapter.ViewHolderr> {
    public static ArrayList<ResExpandList> items = new ArrayList<ResExpandList>();
    ArrayList<Uri> urislist;
    Context context;


    public SendFaxImageAdapter(Context context, ArrayList<ResExpandList> data) {
        this.context = context;
        items = data;
        urislist = new ArrayList<>();
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

        holder.edtlisusr.setText(listitem.getString1Name());
        holder.imgminusr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int positionToRemove = position;
                items.remove(positionToRemove);
                urislist.remove(positionToRemove);
                notifyDataSetChanged();

            }
        });

    }

    public void notifyData(ArrayList<ResExpandList> myList) {
//        Log.d("notifyData ", myList.size() + "");
        this.items = myList;

        notifyDataSetChanged();
    }

    public void notifyDataForUri(ArrayList<Uri> myList) {
//        Log.d("notifyData ", myList.size() + "");
        this.urislist = myList;

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (null != items ? items.size() : 0);
    }


}
