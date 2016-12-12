package com.guanyin.sardar.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.guanyin.sardar.criminalintent.utils.SingleFragmentActivity;

public class CrimeListActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    /**
     * Created by Sardar on 2016/12/12.
     */
    public static class DialogFragment extends android.support.v4.app.DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.date_picker_title)
                    .setPositiveButton(android.R.string.ok, null)
                    .create();
        }
    }
}
