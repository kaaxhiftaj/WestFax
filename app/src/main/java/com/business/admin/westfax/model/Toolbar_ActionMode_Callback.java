package com.business.admin.westfax.model;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.business.admin.westfax.R;
import com.business.admin.westfax.TestFragment;
import com.business.admin.westfax.adapter.InboxAdapter;
import com.business.admin.westfax.adapter.TestAdapter;
import com.business.admin.westfax.fragment.InboxFragment;
import com.business.admin.westfax.retrofit.ResExpandList;

import java.util.ArrayList;

/**
 * Created by SONU on 22/03/16.
 */
public class Toolbar_ActionMode_Callback implements ActionMode.Callback {

    private Context context;
    private TestAdapter recyclerView_adapter;
   private ArrayList<ResExpandList> message_models;
    private boolean isListViewFragment;


    public Toolbar_ActionMode_Callback(Context context, TestAdapter recyclerView_adapter, ArrayList<ResExpandList> message_models, boolean isListViewFragment) {
        this.context = context;
        this.recyclerView_adapter = recyclerView_adapter;
        this.message_models = message_models;
        this.isListViewFragment = isListViewFragment;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_multi_select, menu);//Inflate the menu over action mode
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

        //Sometimes the meu will not be visible so for that we need to set their visibility manually in this method
        //So here show action menu according to SDK Levels
        if (Build.VERSION.SDK_INT < 11) {
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_delete), MenuItemCompat.SHOW_AS_ACTION_NEVER);
          } else {
            menu.findItem(R.id.action_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:

                //Check if current action mode is from ListView Fragment or RecyclerView Fragment

                    //If current fragment is recycler view fragment
                    Fragment recyclerFragment = new TestFragment();//Get recycler view fragment
                    if (recyclerFragment != null)
                        //If recycler fragment not null
                      //  ((TestFragment) recyclerFragment).deleteRows();//delete selected rows

                break;



        }
        return false;
    }


    @Override
    public void onDestroyActionMode(ActionMode mode) {

        //When action mode destroyed remove selected selections and set action mode to null
        //First check current fragment action mode
        recyclerView_adapter.removeSelection();  // remove selection
        Fragment recyclerFragment = new TestFragment();//Get recycler fragment
        if (recyclerFragment != null) {
            // ((TestFragment) recyclerFragment).setNullToActionMode();//Set action mode null
        }
    }
}
