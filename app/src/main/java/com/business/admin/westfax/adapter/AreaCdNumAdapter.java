package com.business.admin.westfax.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.business.admin.westfax.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SONY on 15-02-2018.
 */

public class AreaCdNumAdapter extends RecyclerView.Adapter<AreaCdNumAdapter.ViewHolderr> {
    public static List<String> items = new ArrayList<String>();
    Context context;
    private int lastSelectedPosition = -1;
    public ItemClickCallback itemClickCallback;
    public static String radtiovalue;
    public static String origradtiovalue;

    public interface ItemClickCallback {
        void onItemClick(int p);
    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;

    }

    public AreaCdNumAdapter(Context context, List<String> data) {
        this.context = context;
        items = data;
    }


    public class ViewHolderr extends RecyclerView.ViewHolder {
        RadioButton rd_toll;
        TextView txttolnum;

        public ViewHolderr(final View itemView) {
            super(itemView);
            txttolnum = (TextView) itemView.findViewById(R.id.txttolnum);
            rd_toll = (RadioButton) itemView.findViewById(R.id.rd_toll);


            rd_toll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();

//                    holder.getLayoutPosition();
                    notifyDataSetChanged();
                    radtiovalue = txttolnum.getText().toString();
//                    Toast.makeText(context,
//                            "selected-->" + txttolnum.getText(),
//                            Toast.LENGTH_LONG).show();
//                    Log.e("selc rad-->",txttolnum.getText()+"--"+lastSelectedPosition);
//                    onItemClickListener.onItemClick(items,position);
                    if (itemClickCallback != null) {
                        itemClickCallback.onItemClick(lastSelectedPosition);
                        //  onItemClickListener.onItemClick(txttolnum.getText().toString());
                    }

                }
            });
        }
    }

    @Override
    public ViewHolderr onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_toll, parent, false);
        ViewHolderr viewholder = new ViewHolderr(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(final ViewHolderr holder, final int position) {
        String s = items.get(position);

        String s1 = s.substring(0, 3);
        String s2 = s.substring(3, 6);
        String s3 = s.substring(6, 10);
        String mains = s1 + ")" + s2 + "-" + s3;
        origradtiovalue = items.get(position);
        holder.rd_toll.setText("(" + mains);

//
//        holder.txttolnum.setText(items.get(position));
        holder.rd_toll.setChecked(lastSelectedPosition == position);

//        holder.txttolnum.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                itemClickCallback.onItemClick(position);
//
//            }
//        });

    }

    public String getSelectedItem() {
        if (lastSelectedPosition != -1) {
        //    Toast.makeText(context, "Selected Item : " + items.get(lastSelectedPosition), Toast.LENGTH_SHORT).show();
            return items.get(lastSelectedPosition);
        }
        return "";
    }

    public interface OnItemClickListener {
        void onItemClick(String textName);
    }

    @Override
    public int getItemCount() {
        return (null != items ? items.size() : 0);
    }
//    public OnItemClickListener getOnItemClickListener() {
//        return onItemClickListener;
//    }
//
//    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
//        this.onItemClickListener = onItemClickListener;
//
//    }

}
