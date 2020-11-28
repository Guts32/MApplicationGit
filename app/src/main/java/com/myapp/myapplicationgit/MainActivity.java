package com.myapp.myapplicationgit;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.OnPageChange;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import butterknife.BindView;
import butterknife.ButterKnife;



public class MainActivity extends AppCompatActivity {
    final private int REQUEST_CODE_ASK_PERMISON = 111;
//Pupusor si lees esto pues no se que he echo pero sirve xD

    private Button mButtonUpload;
    private TextView User;
    private ImageView  mButtonChooseImage;
    private CircleImageView mImageView;
    private ProgressBar mProgresBar;
    private static final int ACCESS_FILE = 43;
    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    DatabaseReference myRef;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonChooseImage = findViewById(R.id.Buscar);
        User = findViewById(R.id.P_user_name);
        mImageView = findViewById(R.id.image_view);
        mButtonUpload = findViewById(R.id.image_subir);
        User = findViewById(R.id.P_user_name);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        String id = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        myRef = database.getReference("User").child(id);

        mProgresBar = findViewById(R.id.progress_bar);

        getUserInf();
        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicitarpermisos();
                openGallery();
            }
        });

    }


    private void solicitarpermisos(){
        int permisoStorage = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permisoStorage != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISON);
            }
        }
    }

    private void openGallery(){
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(gallery,"MyApp"), ACCESS_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ACCESS_FILE && resultCode == Activity.RESULT_OK){
            Uri File_Uri = data.getData();
            CropImage.activity(File_Uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setActivityTitle("Crop Image")
                    .setFixAspectRatio(true)
                    .setCropMenuCropButtonTitle("Done")
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                mImageView.setImageURI(resultUri);
                mButtonUpload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mProgresBar.setVisibility(View.VISIBLE);
                       // Uri FileUri = data.getData();
                        String id = mAuth.getCurrentUser().getUid();
                        StorageReference Folder = FirebaseStorage.getInstance().getReference().child("User").child(id);

                        final StorageReference file_name = Folder.child("f" +
                                "ile"+resultUri.getLastPathSegment());


                        file_name.putFile(resultUri).addOnSuccessListener(taskSnapshot -> file_name.getDownloadUrl().addOnSuccessListener(uri -> {

                            Map<String,Object> hashMap = new HashMap<>();
                            hashMap.put("foto", String.valueOf(uri));
                            myRef.updateChildren(hashMap);
                            Toast.makeText(MainActivity.this, "Imagen subida correctamente", Toast.LENGTH_SHORT).show();
                            mProgresBar.setVisibility(View.GONE);
                        }));
                    }
                });
            }
            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception exception = result.getError();
                Toast.makeText(MainActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void getUserInf(){
        String id = mAuth.getCurrentUser().getUid();
        mDatabase.child("User").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String name = snapshot.child("Username").getValue().toString();
                    String email = snapshot.child("email").getValue().toString();
                    String foto = snapshot.child("foto").getValue().toString();


                    User.setText(name);
                    Picasso
                            .with(getApplicationContext())
                            .load(foto)
                            .resize(200, 200)
                            .into(mImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}