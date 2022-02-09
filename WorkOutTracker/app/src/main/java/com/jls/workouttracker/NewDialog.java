package com.jls.workouttracker;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;


public class NewDialog extends AppCompatDialogFragment {
    private EditText editTextName;
    private EditText editTextMuscle;
    private newDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity(), R.style.MyDialogTheme);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_new, null);

        builder.setView(view)
                .setTitle(R.string.ex_new)
                .setNegativeButton(R.string.op_cancel, (dialog, which) -> {

                })
                .setPositiveButton(R.string.op_ok, (dialog, which) -> {
                    if (!editTextName.getText().toString().isEmpty() && !editTextMuscle.getText().toString().isEmpty()) {
                        String name = editTextName.getText().toString();
                        String muscle = editTextMuscle.getText().toString();
                        listener.applyTexts(name, muscle);
                    } else {
                    }
                });

        editTextName = view.findViewById(R.id.dialog_exname);
        editTextMuscle = view.findViewById(R.id.dialog_exmuscle);

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

    public interface newDialogListener {
        void applyTexts(String name, String muscle);
    }
}
