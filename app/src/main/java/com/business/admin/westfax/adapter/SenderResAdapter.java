package com.business.admin.westfax.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.business.admin.westfax.R;
import com.business.admin.westfax.retrofit.ResExpandList;
import com.business.admin.westfax.retrofit.ResultRet;

import java.util.ArrayList;

/**
 * Created by SONY on 27-01-2018.
 */

public class SenderResAdapter extends RecyclerView.Adapter<SenderResAdapter.ViewHolderr> {
    public static ArrayList<ResExpandList> items = new ArrayList<ResExpandList>();
    Context context;


    public SenderResAdapter(Context context, ArrayList<ResExpandList> data) {
        this.context = context;
        items = data;
    }


    public class ViewHolderr extends RecyclerView.ViewHolder {
        EditText txtsmail;
        CheckBox chkok;
        ImageView imgminsender;

        public ViewHolderr(View itemView) {
            super(itemView);
            txtsmail = (EditText) itemView.findViewById(R.id.txtsmail);
            imgminsender = (ImageView) itemView.findViewById(R.id.imgminsender);
            chkok = (CheckBox) itemView.findViewById(R.id.chkok);
        }
    }

    @Override
    public ViewHolderr onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_faxsndr_recivr, parent, false);
        ViewHolderr viewholder = new ViewHolderr(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(final ViewHolderr holder, final int position) {
        final ResExpandList listitem = items.get(position);

        holder.txtsmail.setText(listitem.getString1Value() + "");
        holder.imgminsender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int positionToRemove = position;
                items.remove(positionToRemove);
                notifyDataSetChanged();

            }
        });


        if (listitem.getString2Value().equals("True")) {
            holder.chkok.setChecked(true);
        } else {
            holder.chkok.setChecked(false);

        }




        holder.chkok.setOnCheckedChangeListener(null);

        //if true, your checkbox will be selected, else unselected

        holder.chkok.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    listitem.setString2Value("True");
                }else {
                    listitem.setString2Value("False");
                }

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
