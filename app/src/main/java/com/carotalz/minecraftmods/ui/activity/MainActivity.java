package com.carotalz.minecraftmods.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.carotalz.minecraftmods.R;
import com.carotalz.minecraftmods.databinding.ActivityMainBinding;
import com.carotalz.minecraftmods.ui.fragment.FavoriteFragment;
import com.carotalz.minecraftmods.ui.fragment.ModsFragment;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        init();
    }

    private void init() {
        try {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.container, new ModsFragment(getSupportFragmentManager()), "mods")
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Log.d("backSTack",getSupportFragmentManager().getBackStackEntryCount()+"");
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    TabLayout.Tab tab = binding.homeTabLayout.getTabAt(0);
                    tab.select();
                }
            }
        });
        binding.homeTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (binding.homeTabLayout.getSelectedTabPosition() == 0) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.popBackStack();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new ModsFragment(getSupportFragmentManager()), "mods")
                            .setReorderingAllowed(true)
                            .addToBackStack("mods") // name can be null
                            .commit();
                } else if (binding.homeTabLayout.getSelectedTabPosition() == 1) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.popBackStack();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new FavoriteFragment(getSupportFragmentManager()), "favourite")
                            .setReorderingAllowed(true)
                            .addToBackStack("favorite") // name can be null
                            .commit();

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

}