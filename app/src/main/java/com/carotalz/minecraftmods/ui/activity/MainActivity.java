package com.carotalz.minecraftmods.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.carotalz.minecraftmods.R;
import com.carotalz.minecraftmods.databinding.ActivityMainBinding;
import com.carotalz.minecraftmods.ui.fragment.DetailsFragment;
import com.carotalz.minecraftmods.ui.fragment.FavoriteFragment;
import com.carotalz.minecraftmods.ui.fragment.ModsFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;

import static com.carotalz.minecraftmods.models.Constants.DETAILS_FRAGMENT_TAG;
import static com.carotalz.minecraftmods.models.Constants.FAVOURITE_FRAGMENT_TAG;
import static com.carotalz.minecraftmods.models.Constants.MOD_FRAGMENT_TAG;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        init();
    }

    private int count = 0;

    private void init() {
        count = 0;

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.container, new ModsFragment(getSupportFragmentManager()), MOD_FRAGMENT_TAG)
                .commit();


        binding.homeTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (binding.homeTabLayout.getSelectedTabPosition() == 0) {
                    Log.d("tabSelected", "Mod Tab");
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.popBackStack();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new ModsFragment(getSupportFragmentManager()), MOD_FRAGMENT_TAG)
                            .setReorderingAllowed(true)
                            .addToBackStack(MOD_FRAGMENT_TAG) // name can be null
                            .commit();
                } else if (binding.homeTabLayout.getSelectedTabPosition() == 1) {
                    count = 0;
                    Log.d("tabSelected", "Fav Tab");
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.popBackStack();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new FavoriteFragment(getSupportFragmentManager()), FAVOURITE_FRAGMENT_TAG)
                            .setReorderingAllowed(true)
                            .addToBackStack(FAVOURITE_FRAGMENT_TAG) // name can be null
                            .commit();

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (binding.homeTabLayout.getSelectedTabPosition() == 0) {
                    Log.d("tabSelected", "Mod Tab");
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.popBackStack();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new ModsFragment(getSupportFragmentManager()), MOD_FRAGMENT_TAG)
                            .setReorderingAllowed(true)
                            .addToBackStack(MOD_FRAGMENT_TAG) // name can be null
                            .commit();
                } else if (binding.homeTabLayout.getSelectedTabPosition() == 1) {
                    count = 0;
                    Log.d("tabSelected", "Fav Tab");
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.popBackStack();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new FavoriteFragment(getSupportFragmentManager()), FAVOURITE_FRAGMENT_TAG)
                            .setReorderingAllowed(true)
                            .addToBackStack(FAVOURITE_FRAGMENT_TAG) // name can be null
                            .commit();

                }
            }
        });
    }


    @Override
    public void onBackPressed() {

        Log.d("backStackEntry", getSupportFragmentManager().getBackStackEntryCount() + "");
        DetailsFragment detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentByTag(DETAILS_FRAGMENT_TAG);
        FavoriteFragment favoriteFragment = (FavoriteFragment) getSupportFragmentManager().findFragmentByTag(FAVOURITE_FRAGMENT_TAG);
        ModsFragment modsFragment = (ModsFragment) getSupportFragmentManager().findFragmentByTag(MOD_FRAGMENT_TAG);
        if (count > 0 && (detailsFragment == null || !detailsFragment.isVisible()) && (favoriteFragment == null || !favoriteFragment.isVisible())) {
            new MaterialAlertDialogBuilder(MainActivity.this)
                    .setTitle(getString(R.string.exit_confirm))
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();

        } else if (!modsFragment.isVisible()) {
            super.onBackPressed();
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();

        } else {
            count++;
            if (count > 0) {
                Toast.makeText(this, getString(R.string.again_to_exit), Toast.LENGTH_SHORT).show();
            }
        }

        if (modsFragment != null && modsFragment.isVisible()) {
            TabLayout.Tab tab = binding.homeTabLayout.getTabAt(0);
            tab.select();
            count++;
            Log.d("currentView", "Mods Fragment");
        }
        if (favoriteFragment != null && favoriteFragment.isVisible()) {
            TabLayout.Tab tab = binding.homeTabLayout.getTabAt(1);
            tab.select();
            count = 0;
            Log.d("currentView", "Fav Fragment");
        }
//        Log.d("backStackEntryAfter",getSupportFragmentManager().getBackStackEntryCount()+"");
//        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
//            TabLayout.Tab tab = binding.homeTabLayout.getTabAt(0);
//            tab.select();
//            count++;
//        }


    }
}