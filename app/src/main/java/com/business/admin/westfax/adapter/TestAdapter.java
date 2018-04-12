package com.business.admin.westfax.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.business.admin.westfax.FaxViewerActivity;
import com.business.admin.westfax.R;
import com.business.admin.westfax.retrofit.ResExpandList;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by SONY on 16-03-2018.
 */

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.MyViewHolder> {

    public ArrayList<ResExpandList> usersList = new ArrayList<>();
    public ArrayList<ResExpandList> selected_usersList = new ArrayList<>();
    Context mContext;
    private SparseBooleanArray mSelectedItemsIds;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView boundtxt, phnnotxt, txtpg, txtdtsend, txttime, frmtxt;
        public RelativeLayout ll_listitem;
        CheckBox chkdel;

        public MyViewHolder(View view) {
            super(view);
            phnnotxt = (TextView) view.findViewById(R.id.phnnotxt);
            boundtxt = (TextView) view.findViewById(R.id.boundtxt);
            txtpg = (TextView) view.findViewById(R.id.txtpg);
            frmtxt = (TextView) view.findViewById(R.id.frmtxt);

            txtdtsend = (TextView) view.findViewById(R.id.txtdtsend);
            ll_listitem = (RelativeLayout) view.findViewById(R.id.linitm);
            chkdel = (CheckBox) view.findViewById(R.id.chkdel);
            txttime = (TextView) view.findViewById(R.id.txttime);
        }
    }

    public TestAdapter(Context context, ArrayList<ResExpandList> userList) {
        this.mContext = context;
        this.usersList = userList;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    public TestAdapter(Context context, ArrayList<ResExpandList> userList, ArrayList<ResExpandList> selectedList) {
        this.mContext = context;
        this.usersList = userList;
        this.selected_usersList = selectedList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_userlist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final ResExpandList liss = usersList.get(position);

        String s = "";
        String mains = "";

//        holder.phnnotxt.setText(position + "-- " +datum.getData().get(0).getFaxCallInfoList().get(position).getOrigNumber());
//                liss.getMid());
        holder.boundtxt.setText(liss.getMdirection() + "");
        holder.txtpg.setText(liss.getPageCount() + " page(s)");


        if (liss.getMdirection().equals("Inbound")) {
            holder.frmtxt.setText("From");

            if ((liss.getFaxCallInfoList().get(0).getOrigCSID() + "").equals("") ||
                    (liss.getFaxCallInfoList().get(0).getOrigCSID() + "").equals(null)) {

                s = liss.getFaxCallInfoList().get(0).getOrigNumber() + "";
                if (s.length() >= 10) {
                    String s1 = s.substring(0, 3);
                    String s2 = s.substring(3, 6);
                    String s3 = s.substring(6, 10);
                    mains = "(" + s1 + ")" + s2 + "-" + s3;
                    holder.phnnotxt.setText(mains);
                }
            } else {
                mains =liss.getFaxCallInfoList().get(0).getOrigCSID()+"";
                holder.phnnotxt.setText(liss.getFaxCallInfoList().get(0).getOrigCSID());
            }

//            s = liss.getFaxCallInfoList().get(0).getOrigNumber() + "";
//
//            if (s.length() >= 10) {
//                String s1 = s.substring(0, 3);
//                String s2 = s.substring(3, 6);
//                String s3 = s.substring(6, 10);
//                mains = "(" + s1 + ")" + s2 + "-" + s3;
//
//                holder.phnnotxt.setText(mains);
//            }

        } else if (liss.getMdirection().equals("Outbound")) {
            holder.frmtxt.setText("To");
            s = liss.getFaxCallInfoList().get(0).getTermNumber() + "";

            if (s.length() >= 10) {
                String s1 = s.substring(0, 3);
                String s2 = s.substring(3, 6);
                String s3 = s.substring(6, 10);
                mains = "(" + s1 + ")" + s2 + "-" + s3;

                holder.phnnotxt.setText(mains);
            }
        }

//        Date parsedDate = null;
//        try {
//            parsedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(liss.getMdate() + "");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = null;
        try {
            date = df.parse(liss.getMdate());
            df.setTimeZone(TimeZone.getDefault());
            String formattedDate = df.format(date);


        } catch (ParseException e) {
            e.printStackTrace();
        }


        holder.itemView
                .setBackgroundColor(mSelectedItemsIds.get(position) ? 0x9934B5E4
                        : Color.TRANSPARENT);


//Now reformat it using desired display pattern:
        final String displayDate = new SimpleDateFormat("MM-dd-yyyy").format(date);
        String timemain = "";
        // Log.e("datee>>??", displayDate + "");
        holder.txtdtsend.setText(displayDate);
        Timestamp createdOn = new Timestamp(date.getTime());
        // Log.e("timm>>??", createdOn + "");

        String timmm = new SimpleDateFormat("HH:mm").format(date);
        //   Log.e("real timm>>??", timmm + "");
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            final Date dateObj = sdf.parse(timmm);
            timemain = new SimpleDateFormat("hh:mm aa").format(dateObj);
            //    Log.e("hello timm>>??", timemain + "");
            holder.txttime.setText(timemain);
        } catch (final ParseException e) {
            e.printStackTrace();
        }

//         if (selected_usersList.contains(usersList.get(position))) {
//            holder.ll_listitem.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_selected_state));
//            holder.chkdel.setChecked(true);
//        } else {
//            holder.ll_listitem.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_normal_state));
//            holder.chkdel.setChecked(false);
//        }
        holder.chkdel.setVisibility(View.GONE);

        holder.chkdel.setOnCheckedChangeListener(null);
        if (selected_usersList.contains(usersList.get(position))) {
            holder.chkdel.setVisibility(View.VISIBLE);
            holder.chkdel.setChecked(true);
            holder.ll_listitem.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_selected_state));

        } else {
            holder.chkdel.setVisibility(View.GONE);

            holder.chkdel.setChecked(false);
            holder.ll_listitem.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_normal_state));

        }
        holder.chkdel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //  mChecked.put(mData.get(holder.getAdapterPosition()), isChecked);

                //      Toast.makeText(mContext, "Status is: " + isChecked + "", Toast.LENGTH_SHORT).show();
            }
        });

        final String finalTimemain = timemain;
        final String finalMains = mains;
        holder.ll_listitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("possss---->??", position + "");
                Intent inn = new Intent(mContext, FaxViewerActivity.class);
                inn.putExtra("Id", liss.getMid() + "");
                inn.putExtra("Direction", liss.getMdirection() + "");
                inn.putExtra("Date", liss.getMdate() + "");
                inn.putExtra("Tag", liss.getMtag() + "");
                inn.putExtra("OrigNumber", finalMains);
//                liss.getFaxCallInfoList().get(0).getOrigNumber()
                inn.putExtra("Newdate", displayDate + "");
                inn.putExtra("Newtime", finalTimemain + "");
                inn.putExtra("Pagecount", liss.getPageCount() + "");


                mContext.startActivity(inn);

            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
    //Toggle selection methods
    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }


    //Remove selected selections
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }


    //Put or delete selected position into SparseBooleanArray
    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    //Get total selected count
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    //Return all selected ids
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }


}
