package com.jls.workouttracker;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.jls.workouttracker.activities.RegisterActivity;
import com.jls.workouttracker.activities.WorksListActivity;

public class NewDialog extends AppCompatDialogFragment {
    private EditText editTextName;
    private EditText editTextMuscle;
    private newDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_new, null);

        builder.setView(view)
                .setTitle("New Exercise")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (! editTextName.getText().toString().isEmpty() && ! editTextMuscle.getText().toString().isEmpty()) {
                            String name = editTextName.getText().toString();
                            String muscle = editTextMuscle.getText().toString();
                            listener.applyTexts(name, muscle);
                        } else {
                        }
                    }
                });

        editTextName = view.findViewById(R.id.ex_name);
        editTextMuscle = view.findViewById(R.id.ex_muscle);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (newDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement newDialogListener");
        }

    }

    public interface newDialogListener{
        void applyTexts(String name, String muscle);
    }
}
