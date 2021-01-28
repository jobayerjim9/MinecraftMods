package com.carotalz.minecraftmods.ui.fragment;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import com.carotalz.minecraftmods.R;
import com.carotalz.minecraftmods.controller.adapter.ImageSliderAdapter;
import com.carotalz.minecraftmods.databinding.FragmentDetailsBinding;
import com.carotalz.minecraftmods.models.Constants;
import com.carotalz.minecraftmods.models.ModModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;

import kotlin.io.FilesKt;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;

public class DetailsFragment extends Fragment {
    FragmentDetailsBinding binding;
    private ModModel modModel;
    private FragmentManager fragmentManager;
    File rootFolder;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (content!=null) {
            FilesKt.deleteRecursively(content);
        }
    }

    public DetailsFragment(ModModel modModel,FragmentManager fragmentManager) {
        this.modModel = modModel;
        this.fragmentManager=fragmentManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false);
        binding.setData(modModel);
        init();
        return binding.getRoot();

    }

    private void init() {
        isStoragePermissionGranted();
        rootFolder = new File(Environment.getExternalStorageDirectory() + Constants.MAIN_FOLDER);
        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(requireContext(), modModel.getImages());
        binding.detailsSlider.setAdapter(imageSliderAdapter);
        binding.indicator.setViewPager(binding.detailsSlider);
        binding.downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadContent();
            }
        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.popBackStack();
            }
        });
    }

    String text;

    private void downloadContent() {
        text = getString(R.string.downloading);
        binding.downloadText.setText(text);
        new CountDownTimer(2000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                text = text.concat(".");
                binding.downloadText.setText(text);
            }

            @Override
            public void onFinish() {
                binding.downloadText.setText(requireContext().getString(R.string.install));
                binding.downloadButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isStoragePermissionGranted()) {
                            if (rootFolder.exists()) {
                                installMod();
                            } else {
                                if (rootFolder.mkdirs()) {
                                    installMod();
                                }
                                else {
                                    Toast.makeText(requireContext(), requireContext().getString(R.string.folder_create_error), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
            }
        }.start();
    }
    File content;
    private void installMod() {
        InputStream is;
        OutputStream out = null;
        try {
            String path = Constants.FILE_PATH + modModel.getFile();
            is = requireContext().getAssets().open(path);
            content = new File(rootFolder, modModel.getFile());
            if (content.exists()) {
                if(content.delete()) {
                    out = new FileOutputStream(content);
                    //copyFile(ims, out);
                    byte[] data = new byte[is.available()];
                    is.read(data);
                    out.write(data);
                    is.close();
                    out.close();
                    Toast.makeText(requireContext(), requireContext().getString(R.string.success_export), Toast.LENGTH_SHORT).show();
                    openFile(content);
                }
            }
            else {
                out = new FileOutputStream(content);
                //copyFile(ims, out);
                byte[] data = new byte[is.available()];
                is.read(data);
                out.write(data);
                is.close();
                out.close();
                Toast.makeText(requireContext(), requireContext().getString(R.string.success_export), Toast.LENGTH_SHORT).show();
                openFile(content);
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void openFile(File content) {

        Uri minecraftData = FileProvider.getUriForFile(requireContext(), requireContext().getApplicationContext().getPackageName() + ".provider", content);
        // Uri minecraftData=Uri.parse("file:///storage/emulated/0/fromgate.mcpack");
        Intent intent = new Intent(Intent.ACTION_VIEW, minecraftData);
        intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        PackageManager packageManager = requireContext().getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 2);
        boolean isIntentSafe = activities.size() > 0;
// Start an activity if it's safe
        if (isIntentSafe) {
            killAppBypackage("com.mojang.minecraftpe");
            List<ResolveInfo> resInfoList = requireContext().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                requireContext().grantUriPermission(packageName, minecraftData, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            Toast.makeText(requireContext(), requireContext().getString(R.string.imported_success), Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
        else {
            Intent playStore = new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=com.mojang.minecraftpe"));
            startActivity(playStore);
            Toast.makeText(requireContext(), requireContext().getString(R.string.not_found_minecraft), Toast.LENGTH_SHORT).show();
        }
    }
    final String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private void killAppBypackage(String packageTokill) {

        List<ApplicationInfo> packages;
        PackageManager pm;
        pm = requireContext().getPackageManager();
        //get a list of installed apps.
        packages = pm.getInstalledApplications(0);


        ActivityManager mActivityManager = (ActivityManager) requireContext().getSystemService(Context.ACTIVITY_SERVICE);
        String myPackage = requireContext().getApplicationContext().getPackageName();

        for (ApplicationInfo packageInfo : packages) {

            if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                continue;
            }
            if (packageInfo.packageName.equals(myPackage)) {
                continue;
            }
            if (packageInfo.packageName.equals(packageTokill)) {
                mActivityManager.killBackgroundProcesses(packageInfo.packageName);
            }

        }

    }
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("DetailsActivity", "Permission is granted");
                return true;
            } else {

                Log.v("DetailsActivity", "Permission is revoked");
                ActivityCompat.requestPermissions(requireActivity(), permissions, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("DetailsActivity", "Permission is granted");
            return true;
        }
    }

}