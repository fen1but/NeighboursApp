package com.example.neighbours;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class MapFragment extends Fragment {


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FrameLayout layout = view.findViewById(R.id.bottom_sheet);
        BottomSheetBehavior bottomSheet = BottomSheetBehavior.from(layout);
        bottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheet.setPeekHeight(300);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.map_fragment, container, false);
        return rootView;
    }
}
