package com.business.admin.westfax.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.business.admin.westfax.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SONY on 03-02-2018.
 */

public class TollNumAdapter extends RecyclerView.Adapter<TollNumAdapter.ViewHolderr> {
    public static List<String> items = new ArrayList<String>();
    Context context;
    private int lastSelectedPosition = -1;
    public ItemClickCallback itemClickCallback;
    public static String radtiovalue;
    public static String origradnumb;
    SparseBooleanArray booleanArray;
    private RadioButton lastCheckedRB = null;
    private RadioGroup lastCheckedRadioGroup = null;

    private static int lastCheckedPos = 0;
    public interface ItemClickCallback {
        void onItemClick(int p);
    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

    public TollNumAdapter(Context context, List<String> data) {
        this.context = context;
        items = data;
        booleanArray = new SparseBooleanArray();
    }

    public class ViewHolderr extends RecyclerView.ViewHolder {
        RadioButton rd_toll;
        TextView txttolnum;
        RadioGroup grpnumbs;

        public ViewHolderr(final View itemView) {
            super(itemView);
            txttolnum = (TextView) itemView.findViewById(R.id.txttolnum);
            rd_toll = (RadioButton) itemView.findViewById(R.id.rd_toll);
            grpnumbs = (RadioGroup) itemView.findViewById(R.id.grpnumbs);


            rd_toll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    lastSelectedPosition = getAdapterPosition();

//                    holder.getLayoutPosition();
                //    notifyItemRangeChanged(0, items.size());
                    notifyDataSetChanged();

//                    Toast.makeText(context,
//                            "selected-->" + txttolnum.getText(),
//                            Toast.LENGTH_LONG).show();
                    Log.e("selc rad-->",rd_toll.getText()+"--"+lastSelectedPosition);
//                    onItemClickListener.onItemClick(items,position);

                    radtiovalue = rd_toll.getText().toString();




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
        origradnumb = items.get(position);
        String mains = s1 + ")" + s2 + "-" + s3;

        holder.rd_toll.setText("(" + mains);

        if (lastSelectedPosition != -1) {

            holder.rd_toll.setChecked(lastSelectedPosition == position);
        }

        else {

        }




//         if (holder.rd_toll.isChecked()){
//         }

//        holder.txttolnum.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                itemClickCallback.onItemClick(position);
//
//            }
//        });

    }


    public interface OnItemClickListener {
        void onItemClick(String textName);
    }

    public String getSelectedItemForToll() {
        if (lastSelectedPosition != -1) {
            // Toast.makeText(context, "Selected Item : " + items.get(lastSelectedPosition), Toast.LENGTH_SHORT).show();
            return items.get(lastSelectedPosition);
        }
        return "";
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
