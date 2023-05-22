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
import com.cemcoma.myapplication.listings.Upload;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class addListingActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button chooseImageButton, uploadListingButton;
    private EditText booknameText, authorText, priceText;
    private ImageView previewView;
    private Uri uri;
    private User user;
    private FirebaseStorage mStorage;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_listing);

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
        final boolean[] isUploaded = {false};
        if(uri != null) {
            mStorage.getReference("mp-uploads").child(booknameText.getText().toString().trim() + "-" + user.getUID() +"."+ getExtension(uri)).putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Upload upload = new Upload(booknameText.getText().toString().trim() + "-" + user.getUID(),taskSnapshot.getUploadSessionUri().toString(), user.getUsername(),Integer.parseInt(priceText.getText().toString()),user.getRating());
                            mFirestore.collection("mp-listings").document(booknameText.getText().toString().trim() + "-" + user.getUID()).set(upload);
                            close();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(addListingActivity.this, "Listing was not uploaded, try again", Toast.LENGTH_SHORT).show();
                            isUploaded[0] = false;
                        }
                    });
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }

        return isUploaded[0];
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
}