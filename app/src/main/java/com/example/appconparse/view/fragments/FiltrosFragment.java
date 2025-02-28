package com.example.appconparse.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appconparse.R;


public class FiltrosFragment extends Fragment {

    public FiltrosFragment(){

    }

    public static FiltrosFragment newInstance(String param1, String param2){
        return new FiltrosFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState){
        return inflater.inflate(R.layout.fragment_filtros, container, false);
    }

}