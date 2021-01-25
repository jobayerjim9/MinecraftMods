package com.carotalz.minecraftmods.ui.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.carotalz.minecraftmods.R;
import com.carotalz.minecraftmods.controller.adapter.ModsAdapter;
import com.carotalz.minecraftmods.controller.helpers.TinyDB;
import com.carotalz.minecraftmods.databinding.FragmentModsBinding;
import com.carotalz.minecraftmods.models.Constants;
import com.carotalz.minecraftmods.models.ModModel;

import java.util.ArrayList;

public class ModsFragment extends Fragment {
    private FragmentManager fragmentManager;
    private FragmentModsBinding binding;
    private ArrayList<ModModel> modModels=new ArrayList<>();
    private ModsAdapter modsAdapter;
    public ModsFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_mods, container, false);
        init();

        return binding.getRoot();
    }

    private void init() {
        binding.modsRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        modsAdapter=new ModsAdapter(requireContext(),modModels,fragmentManager);
        binding.modsRecycler.setAdapter(modsAdapter);
        getAllData();
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllData();
    }

    private void getAllData() {
        modModels.clear();
        TinyDB tinyDB=new TinyDB(requireContext());
        modModels.addAll(tinyDB.getListObject(Constants.MOD_DATA_KEY));
        binding.setSize(modModels.size());
        modsAdapter.notifyDataSetChanged();
    }

}