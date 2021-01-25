package com.carotalz.minecraftmods.controller.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.carotalz.minecraftmods.R;
import com.carotalz.minecraftmods.controller.helpers.TinyDB;
import com.carotalz.minecraftmods.databinding.ItemModsBinding;
import com.carotalz.minecraftmods.models.Constants;
import com.carotalz.minecraftmods.models.ModModel;
import com.carotalz.minecraftmods.ui.fragment.DetailsFragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ModsAdapter extends RecyclerView.Adapter<ModsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ModModel> modModels;
    private FragmentManager fragmentManager;
    public ModsAdapter(Context context, ArrayList<ModModel> modModels,FragmentManager fragmentManager) {
        this.context = context;
        this.modModels = modModels;
        this.fragmentManager=fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_mods,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModModel modModel=modModels.get(position);
        holder.binding.setData(modModel);
        if (modModels.get(position).getImages().size()>0) {
            try {
                String fileName = Constants.IMAGE_PATH + modModel.getImages().get(0).getImage();
                InputStream ims = context.getAssets().open(fileName);
                // load image as Drawable
                Drawable d = Drawable.createFromStream(ims, modModel.getImages().get(0).getImage());
                // set image to ImageView
                holder.binding.itemBanner.setImageDrawable(d);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (modModel.isFavourite()) {
            holder.binding.favIcon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_favorite_tab_active));
        } else {
            holder.binding.favIcon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_favorite_tab_inactive));
        }
        holder.binding.favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modModels.get(position).isFavourite()) {
                    modModels.get(position).setFavourite(false);
                }
                else {
                    modModels.get(position).setFavourite(true);
                }
                TinyDB tinyDB=new TinyDB(context);
                tinyDB.putListObject(Constants.MOD_DATA_KEY,modModels);
                notifyDataSetChanged();

            }
        });
        holder.binding.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container, new DetailsFragment(modModel,fragmentManager), "Details Fragment" + position);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        holder.binding.executePendingBindings();

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return modModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemModsBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
        }
    }
}
