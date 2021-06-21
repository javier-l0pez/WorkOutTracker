package com.jls.workouttracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jls.workouttracker.ExampleItem;
import com.jls.workouttracker.R;
import com.jls.workouttracker.model.Exercise;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    Context context;

    private ArrayList<Exercise> mExerciseList;

    private int pos;
    public int getPos() { return pos; }

    public ExerciseAdapter(Context context, ArrayList<Exercise> mExerciseList) {
//        this.context = context;
        this.mExerciseList = mExerciseList;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        ExerciseViewHolder evh = new ExerciseViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise currentItem = mExerciseList.get(position);

        holder.textTitle.setText(currentItem.getName());
        holder.textMuscle.setText(currentItem.getMuscle());
        holder.myImageView.setImageResource(currentItem.getImg());

//        holder.itemView.setOnLongClickListener(v -> {
//            this.pos = position;
//            return false;
//        });
    }

    @Override
    public int getItemCount() {
        return mExerciseList.size();
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        public ImageView myImageView;
        public TextView textTitle;
        public TextView textMuscle;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            myImageView = itemView.findViewById(R.id.image_exercise);
            textTitle = itemView.findViewById(R.id.text_view_exname);
            textMuscle = itemView.findViewById(R.id.text_view_muscle);
        }
    }

}
