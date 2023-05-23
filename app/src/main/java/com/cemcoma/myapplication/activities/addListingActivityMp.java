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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cemcoma.myapplication.R;
import com.cemcoma.myapplication.User;
import com.cemcoma.myapplication.listings.UploadMp;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class addListingActivityMp extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button chooseImageButton, uploadListingButton;
    private EditText booknameText, authorText, priceText;
    private ImageView previewView;
    private Uri uri, downloadUri;
    private User user;
    private FirebaseStorage mStorage;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_listing_mp);

        user = new User(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()));
        chooseImageButton = (Button) findViewById(R.id.addImageButton);
        uploadListingButton = (Button) findViewById(R.id.uploadListingButton);
        booknameText = (EditText) findViewById(R.id.booknameView);
        authorText = (EditText) findViewById(R.id.authorView);
        priceText = (EditText) findViewById(R.id.priceView);
        previewView = (ImageView)findViewById(R.id.previewView);

        mStorage = FirebaseStorage.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        uploadListingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isUploaded = uploadFile();
                if(isUploaded) close();
            }
        });
    }
    private String getExtension(Uri uri) { // gets the file extension e.g. png jpeg
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private boolean uploadFile() {
        if(booknameText.getText().length() == 0 || authorText.getText().length() == 0 || priceText.getText().length() == 0) {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(uri != null) {
            StorageReference storageReference= FirebaseStorage.getInstance().getReference();

            final StorageReference ref  = mStorage.getReference("mp-uploads").child(booknameText.getText().toString().trim() + "-" + user.getUID() +"."+ getExtension(uri));
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
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
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
            Picasso.with(this).load(uri).into(previewView);

        }
    }

    private void close() {
        this.finish();
    }

    private void setDownloadUriPlease(Uri uri) {
        downloadUri = uri;
        UploadMp upload = new UploadMp(booknameText.getText().toString(), downloadUri.toString() , user.getUsername(),Integer.parseInt(priceText.getText().toString()),user.getRating(), authorText.getText().toString());
        mFirestore.collection("mp-listings").document(booknameText.getText().toString().trim() + "-" + user.getUID()).set(upload);
        close();
    }
}