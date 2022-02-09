package com.jls.workouttracker.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.jls.workouttracker.DAOexercise;
import com.jls.workouttracker.R;
import com.jls.workouttracker.fragments.PublicWorkoutFragment;
import com.jls.workouttracker.model.Exercise;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static com.jls.workouttracker.adapters.RVAdapter.EXTRA_EDIT;

public class AddEditActivity extends AppCompatActivity {

    private Button btnUpdateCreate, btnSetImage;
    private EditText text_name, text_muscle;
    private boolean isPublic, imageUpdated;

    private ImageView mImageView;
    private Uri mImageUri;
    private StorageReference myStor;
    private StorageTask mUploadTask;
    private static final int PICK_IMAGE_REQUEST = 1;
    public static final int REQUEST_READ_EXTERNAL = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        text_name = findViewById(R.id.act_edit_exname);
        text_muscle = findViewById(R.id.act_edit_exmuscle);
        btnUpdateCreate = findViewById(R.id.btn_update_create);
        Exercise ex_edit = (Exercise) getIntent().getSerializableExtra(EXTRA_EDIT);

        btnSetImage = findViewById(R.id.btn_update_image);
        mImageView = findViewById(R.id.image_view_set);
        myStor = FirebaseStorage.getInstance().getReference("images");

        isPublic = getIntent().getBooleanExtra(PublicWorkoutFragment.EXTRA_PUBLIC, false);
        DAOexercise dao = new DAOexercise(isPublic);

//        registerForContextMenu(mImageView);

        if (ex_edit == null) {
            btnUpdateCreate.setText(R.string.ex_create);
            Exercise ex_create = new Exercise();

            btnSetImage.setOnClickListener(v -> checkPerm());

            btnUpdateCreate.setOnClickListener(v -> createExercise(dao, ex_create));

        } else {
            btnUpdateCreate.setText(R.string.ex_update);
            text_name.setText(ex_edit.getName());
            text_muscle.setText(ex_edit.getMuscle());
            if (!ex_edit.getImg().isEmpty()) {
                Picasso.get()
                        .load(ex_edit.getImg())
                        .fit()
                        .centerCrop()
                        .into(mImageView);
            }

            imageUpdated = false;
            btnSetImage.setOnClickListener(v -> {
                checkPerm();
                imageUpdated = true;
            });

            btnUpdateCreate.setOnClickListener(v -> editExercise(dao, ex_edit, imageUpdated));

        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_item_option, menu);
        menu.findItem(R.id.menu_item_edit).setVisible(false);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_remove:
                mImageView.setImageDrawable(null);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void  checkPerm() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==  PackageManager.PERMISSION_GRANTED){
            openFileChooser();
        } else {
            requestStoragePermission();
        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.perm_needed)
                    .setMessage(R.string.perm_why)
                    .setPositiveButton(R.string.op_ok, (dialog, which) -> ActivityCompat.requestPermissions(AddEditActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL))
                    .setNegativeButton(R.string.op_cancel, (dialog, which) -> dialog.dismiss())
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_EXTERNAL){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, R.string.perm_gr, Toast.LENGTH_SHORT).show();
                openFileChooser();
            } else {
                Toast.makeText(this, R.string.perm_den, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(mImageView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(Exercise exercise, @NonNull OnGetDataListener listener) {
        listener.onStart();
        if (mImageUri != null) {
            StorageReference fileReference = myStor.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Log.i("ImageSucc", "Image set successfully");
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            exercise.setImg(uri.toString());
                            listener.onSuccess();
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddEditActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        listener.onFailure();
                    });
        } else {
            exercise.setImg("");
            listener.onSuccess();
        }
    }

    private void editExercise(DAOexercise daoedit, @NonNull Exercise exer_edit, boolean isUpdated) {

        btnUpdateCreate.setOnClickListener(v -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("name", text_name.getText().toString());
            hashMap.put("muscle", text_muscle.getText().toString());
            if (mUploadTask != null && mUploadTask.isInProgress()) {
                Toast.makeText(getApplicationContext(), R.string.up_progress, Toast.LENGTH_SHORT).show();
            } else {

                if (isUpdated) {

                    if (mUploadTask != null && mUploadTask.isInProgress()) {
                        Toast.makeText(getApplicationContext(), R.string.up_progress, Toast.LENGTH_SHORT).show();
                    } else {
                        uploadFile(exer_edit, new OnGetDataListener() {
                            @Override
                            public void onSuccess() {
                                hashMap.put("img", exer_edit.getImg());
                                daoedit.update(exer_edit.getKey(), hashMap).addOnSuccessListener(suc -> {
                                    Toast.makeText(getApplicationContext(), R.string.item_ex_up, Toast.LENGTH_SHORT).show();
                                    finish();
                                }).addOnFailureListener(er -> Toast.makeText(getApplicationContext(), "" + er.getMessage(), Toast.LENGTH_SHORT).show());

                            }

                            @Override
                            public void onStart() {
                                Log.d("edit", "Ex edition started");
                            }

                            @Override
                            public void onFailure() {
                                Log.d("onFailure", "Failed");
                            }
                        });
                    }

                } else {
                    Log.d("imgup", "Not updated");
                    daoedit.update(exer_edit.getKey(), hashMap).addOnSuccessListener(suc -> {
                        Toast.makeText(getApplicationContext(), R.string.item_ex_up, Toast.LENGTH_SHORT).show();
                        finish();
                    }).addOnFailureListener(er -> Toast.makeText(getApplicationContext(), "" + er.getMessage(), Toast.LENGTH_SHORT).show());
                }

            }

        });
    }

    private void createExercise(DAOexercise daocreate, Exercise exer_crea) {
        if (!text_name.getText().toString().isEmpty() && !text_muscle.getText().toString().isEmpty()) {
            String name = text_name.getText().toString();
            String muscle = text_muscle.getText().toString();

            if (mUploadTask != null && mUploadTask.isInProgress()) {
                Toast.makeText(getApplicationContext(), R.string.up_progress, Toast.LENGTH_SHORT).show();
            } else {
                uploadFile(exer_crea, new OnGetDataListener() {
                    @Override
                    public void onSuccess() {
                        exer_crea.setName(name);
                        exer_crea.setMuscle(muscle);

                        daocreate.add(exer_crea).addOnSuccessListener(suc -> {
                            Toast.makeText(getApplicationContext(), R.string.item_ex_rec, Toast.LENGTH_SHORT).show();
                            finish();
                        }).addOnFailureListener(er -> Toast.makeText(getApplicationContext(), "" + er.getMessage(), Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onStart() {
                        Log.d("create", "Ex creation started");
                    }

                    @Override
                    public void onFailure() {
                        Log.d("onFailure", "Failed");
                    }
                });
            }

        }
    }

    public interface OnGetDataListener {
        void onSuccess();

        void onStart();

        void onFailure();
    }

}