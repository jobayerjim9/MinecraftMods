package com.carotalz.minecraftmods.ui.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carotalz.minecraftmods.R;
import com.carotalz.minecraftmods.controller.adapter.ModsAdapter;
import com.carotalz.minecraftmods.controller.helpers.TinyDB;
import com.carotalz.minecraftmods.databinding.FragmentFavoriteBinding;
import com.carotalz.minecraftmods.models.Constants;
import com.carotalz.minecraftmods.models.ModModel;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment {
    private FragmentManager fragmentManager;
    private FragmentFavoriteBinding binding;
    private ArrayList<ModModel> favModels = new ArrayList<>();
    private ArrayList<ModModel> modModels = new ArrayList<>();
    private ModsAdapter modsAdapter;
    public FavoriteFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_favorite, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        binding.favRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        modsAdapter = new ModsAdapter(requireContext(), favModels, fragmentManager);
        binding.favRecycler.setAdapter(modsAdapter);
        getAllFavData();
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllFavData();
    }

    private void getAllFavData() {
        favModels.clear();
        modModels.clear();
        TinyDB tinyDB = new TinyDB(requireContext());
        modModels.addAll(tinyDB.getListObject(Constants.MOD_DATA_KEY));
        for (ModModel mod : modModels) {
            if (mod.isFavourite()) {
                favModels.add(mod);
            }
        }
        binding.setSize(favModels.size());
        modsAdapter.notifyDataSetChanged();

    }
}