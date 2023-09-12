package sp.com.concord;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Therapist_signup extends AppCompatActivity {
    private TextInputEditText tName, tEmail, tPasswd, tConPass, tPhNo, tSpecial;
    private ImageView tPhoto;

    private FirebaseAuth mAuth;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private Uri ImageUri;


    private static final String TAG = "FARHAN";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_therapist_signup);
        tName = findViewById(R.id.tFullName);
        tEmail = findViewById(R.id.tEmail);
        tPasswd = findViewById(R.id.tPasswd);
        tConPass = findViewById(R.id.tConfPasswd);
        tPhNo = findViewById(R.id.tPhNo);
        tSpecial = findViewById(R.id.tSpecialize);
        tPhoto = findViewById(R.id.tPhoto);
    }

    // function to let's the user to choose image from camera or gallery
    private void chooseImage(Context context){
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit" }; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(optionsMenu[i].equals("Take Photo")){
                    // Open the camera and get the photo
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                }
                else if(optionsMenu[i].equals("Choose from Gallery")){
                    // choose from  external storage
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                }
                else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    // function to check permission
    public static boolean checkAndRequestPermissions(final Activity context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    // Handled permission Result
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(Therapist_signup.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(Therapist_signup.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    chooseImage(Therapist_signup.this);
                }
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        tPhoto.setImageBitmap(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                            ImageUri = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (ImageUri != null) {
                            Cursor cursor = getContentResolver().query(ImageUri, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                tPhoto.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }

    public void uploadPpic(View view) {
        if(checkAndRequestPermissions(Therapist_signup.this)){
            chooseImage(Therapist_signup.this);
        }
    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void signUpTherapist() {
        String name = tName.getText().toString().trim();
        String email = tEmail.getText().toString().trim();
        String passwd = tPasswd.getText().toString().trim();
        String passwdConf = tConPass.getText().toString().trim();
        String phNo = tPhNo.getText().toString().trim();
        String special = tSpecial.getText().toString().trim();




        tPhoto.buildDrawingCache();
        Bitmap photo = tPhoto.getDrawingCache();
        byte[] pPic = getBytes(photo);

        final String type = "Therapist";
        int ttl_patient = 0;




        if (name.isEmpty()) {
            tName.setError("Full name is required");
            tName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            tEmail.setError("Email is required");
            tEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tEmail.setError("Please provide a valid Email");
            tEmail.requestFocus();
            return;
        }

        if (passwd.isEmpty()) {
            tPasswd.setError("Password is required");
            tPasswd.requestFocus();
            return;
        }

        if (passwd.length()<6) {
            tPasswd.setError("Password must be 6 character long");
            tPasswd.requestFocus();
            return;
        }

        if (passwdConf.isEmpty()) {
            tConPass.setError("Please re-enter your password");
            tConPass.requestFocus();
            return;
        }

        if (!passwdConf.equals(passwd)) {
            tConPass.setError("Password do not match re-entered password");
            tConPass.requestFocus();
            return;
        }

        if (phNo.isEmpty()) {
            tPhNo.setError("Phone number is required");
            tPhNo.requestFocus();
            return;
        }

        if (special.isEmpty()) {
            tSpecial.setError("Specialisation is required");
            tSpecial.requestFocus();
            return;
        }

        if(ImageUri != null){
            Log.d(TAG, "ImageUri not empty ");

        }else{
            Toast.makeText(this,"please upload a picture ",Toast.LENGTH_LONG).show();
        }

        mAuth.createUserWithEmailAndPassword(email, passwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {

                            StorageReference fileRef =  storageRef.child(System.currentTimeMillis() + "." + getFileExtension(ImageUri));
                            fileRef.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri downloadUrl) {
                                            Therapist therapist = new Therapist (name, email, passwd, phNo, special, type, downloadUrl.toString(), ttl_patient);
                                            FirebaseDatabase.getInstance().getReference("Members/Therapist")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .setValue(therapist).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()) {
                                                        Toast.makeText(Therapist_signup.this, "User has been added successfully!", Toast.LENGTH_LONG).show();
                                                        finish();
                                                    } else {
                                                        Toast.makeText(Therapist_signup.this, "Failed to sign up! Try again!", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                        }
                                    });



                                }

                            });

                            //Therapist therapist = new Therapist (name, email, passwd, phNo, special, type, pUrl);

                           /* FirebaseDatabase.getInstance().getReference("Members/Therapist")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(therapist).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()) {
                                        Toast.makeText(Therapist_signup.this, "User has been added successfully!", Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        Toast.makeText(Therapist_signup.this, "Failed to sign up! Try again!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });*/
                        } else {
                            Toast.makeText(Therapist_signup.this, "Failed to sign up!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }


    public void therapistSignUpComplete(View view) {
        signUpTherapist();
    }

    /*private void uploadToFirebase(Uri uri){
        StorageReference fileRef =  storageRef.child(System.currentTimeMillis() + "." + getFileExtension(uri));

        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               url = fileRef.getDownloadUrl().toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Therapist_signup.this,"failed to upload",Toast.LENGTH_LONG).show();
            }
        });
        //String mURL = url;
       // return mURL;
    }*/

    private String getFileExtension(Uri mUri) {

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

}