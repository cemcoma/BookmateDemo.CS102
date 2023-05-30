package com.cemcoma.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.cemcoma.myapplication.R;
import com.cemcoma.myapplication.User;
import com.cemcoma.myapplication.listings.UploadMp;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class settingsActivity extends AppCompatActivity {

    private Button cPreferences , logout , cPp;

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageButton goBack;
    private ImageView previewView;
    private Uri uri, downloadUri;
    private User user;
    private FirebaseStorage mStorage;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mStorage = FirebaseStorage.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        user = new User(FirebaseAuth.getInstance().getCurrentUser());
        cPreferences = findViewById(R.id.pref);
        logout = findViewById(R.id.logout);
        cPp = findViewById(R.id.pp);

        goBack = findViewById(R.id.goback);

        cPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext() , preferencesActivity.class);
                i.putExtra("user" , FirebaseAuth.getInstance().getCurrentUser());
                i.putExtra("username" , user.getUsername());
                i.putExtra("password" , user.getPassword());
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent =  new Intent(getApplicationContext(), userLoginActivity.class);
                startActivity(intent);
            }
        });

        cPp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
                finish();
            }
        });



    }
    private String getExtension(Uri uri) { // gets the file extension e.g. png jpeg
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private boolean uploadFile() {

        if(uri != null) {
            StorageReference storageReference= FirebaseStorage.getInstance().getReference();

            final StorageReference ref  = mStorage.getReference("pp").child(user.getUID() +"."+ getExtension(uri));
            UploadTask uploadTask = ref.putFile(uri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        setDownloadUriPlease(task.getResult());
                    }
                }
            });
        }

        return false;
    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
        }
    }
    private void setDownloadUriPlease(Uri uri) {
        downloadUri = uri;
        mFirestore.collection("users").document(user.getUID()).update("profileUrl" , downloadUri);
    }

}