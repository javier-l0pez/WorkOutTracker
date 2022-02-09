package com.jls.workouttracker.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jls.workouttracker.DAOexercise;
import com.jls.workouttracker.ExerciseVH;
import com.jls.workouttracker.R;
import com.jls.workouttracker.activities.AddEditActivity;
import com.jls.workouttracker.model.Exercise;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.jls.workouttracker.fragments.PublicWorkoutFragment.EXTRA_PUBLIC;

public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private boolean isPublic;
    private boolean isAdmin;

    private List<Exercise> exerciseList = new ArrayList<>();
    public static final String EXTRA_EDIT = "com.jls.workouttracker.EXTRA_EDIT";

    public RVAdapter(Context ctx) { this.context = ctx; }

    public RVAdapter(Context ctx, boolean isPub, boolean isAdm) {
        this.context = ctx;
        this.isPublic = isPub;
        this.isAdmin = isAdm;
    }

    public void setItems(List<Exercise> ex) {
        exerciseList.clear();
        exerciseList.addAll(ex);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_exercise, parent, false);
        return new ExerciseVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ExerciseVH evh;
        evh = (ExerciseVH) holder;
        Exercise exercise = exerciseList.get(position);
        evh.txt_name.setText(exercise.getName());
        evh.txt_muscle.setText(exercise.getMuscle());
        if (! exercise.getImg().isEmpty()) {
            Picasso.get()
                    .load(exercise.getImg())
                    .fit()
                    .centerCrop()
                    .into(((ExerciseVH) holder).iv_image);
        }

        if ((! isPublic ) | (isPublic & isAdmin)){
            evh.txt_option.setVisibility(View.VISIBLE);
            evh.txt_option.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(context, evh.txt_option);
                popupMenu.inflate(R.menu.menu_item_option);
                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.menu_item_edit:
                            Intent intent = new Intent(context, AddEditActivity.class);
                            intent.putExtra(EXTRA_EDIT, exercise);
                            intent.putExtra(EXTRA_PUBLIC, isPublic);
                            context.startActivity(intent);
                            break;
                        case R.id.menu_item_remove:
                            DAOexercise dao = new DAOexercise(isPublic);
                            dao.remove(exercise.getKey()).addOnSuccessListener(suc -> {
                                Toast.makeText(context, R.string.item_ex_rem, Toast.LENGTH_SHORT).show();
                                notifyItemRemoved(position);
                            }).addOnFailureListener(er -> Toast.makeText(context, "" + er.getMessage(), Toast.LENGTH_SHORT).show());
                            break;
                    }
                    return false;
                });
                popupMenu.show();
            });
        }

    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }
}
