package com.allocare.thenibazzar.address;


import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.allocare.thenibazzar.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    public static BottomSheetFragment newInstance() {
        BottomSheetFragment fragment = new BottomSheetFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.fragment_bottomsheet, null);
        dialog.setContentView(contentView);
        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

}
