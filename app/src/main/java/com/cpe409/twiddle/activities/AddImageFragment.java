package com.cpe409.twiddle.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cpe409.twiddle.R;

/**
 * Created by Christine on 2/16/15.
 */
public class AddImageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_addimage, container, false);


    }

}
