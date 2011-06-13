/*
 * Created on Jun 13, 2011
 *
 * Copyright (c) Sybase, Inc. 2011   
 * All rights reserved.                                    
 */

package com.charles.actions;

import com.charles.ui.AuthFragmentDialog;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

/**
 * Represents the action to show the auth dialog TODO: can add a finish action
 * to do after auth.
 * 
 * @author qiangz
 */
public class ShowAuthDialogAction implements IAction {

    private Activity mActivity;

    public ShowAuthDialogAction(Activity act) {
        this.mActivity = act;
    }

    /*
     * (non-Javadoc)
     * @see com.charles.actions.IAction#execute()
     */
    @Override
    public void execute() {
        FragmentManager fm = mActivity.getFragmentManager();
        FragmentTransaction ft = fm
                .beginTransaction();
        Fragment prev = fm.findFragmentByTag(
                "auth_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        AuthFragmentDialog authDialog = new AuthFragmentDialog();
        authDialog.setCancelable(true);
        authDialog.show(ft, "auth_dialog");
    }

}
