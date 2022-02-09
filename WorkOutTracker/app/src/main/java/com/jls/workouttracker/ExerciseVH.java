package com.jls.workouttracker;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ExerciseVH extends RecyclerView.ViewHolder {

    public TextView txt_name, txt_muscle, txt_option;
    public ImageView iv_image;

    public ExerciseVH(@NonNull View itemView) {
        super(itemView);
        txt_name = itemView.findViewById(R.id.item_textview_exname);
        txt_muscle = itemView.findViewById(R.id.item_textview_muscle);
        txt_option = itemView.findViewById(R.id.item_option);
        iv_image = itemView.findViewById(R.id.item_image_exercise);
    }
}
