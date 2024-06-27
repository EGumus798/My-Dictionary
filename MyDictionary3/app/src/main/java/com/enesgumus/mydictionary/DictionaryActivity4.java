package com.enesgumus.mydictionary;

import static android.graphics.ImageDecoder.decodeBitmap;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.enesgumus.mydictionary.databinding.ActivityDictionaryBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;


public class DictionaryActivity4 extends AppCompatActivity {
    private ActivityDictionaryBinding binding;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;
    Bitmap selectedImage;
    SQLiteDatabase database;

    String info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDictionaryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        registerLauncher();
        database = this.openOrCreateDatabase("Wordssss", MODE_PRIVATE,null);


        Intent intent = getIntent();
        info = intent.getStringExtra("info");

        if (info.equals("newwww")){
            //new word
            binding.turkishText.setText("");
            binding.englishText.setText("");
            binding.saveButton.setVisibility(View.VISIBLE);
            binding.deleteButton.setVisibility(View.INVISIBLE);
            binding.imageView.setImageResource(R.drawable.selectimagess);
        } else {
            int wordId = intent.getIntExtra("wordId",1);
            binding.saveButton.setVisibility(View.INVISIBLE);

            try {

                Cursor cursor = database.rawQuery("SELECT * FROM wordssss WHERE id = ?",new String[] {String.valueOf(wordId)});
                int englishnameIx = cursor.getColumnIndex("englishname");
                int turkishnameIx = cursor.getColumnIndex("turkishname");
                int imageIx = cursor.getColumnIndex("image");

                while(cursor.moveToNext()) {
                    binding.englishText.setText(cursor.getString(englishnameIx));
                    binding.turkishText.setText(cursor.getString(turkishnameIx));

                    byte [] bytes = cursor.getBlob(imageIx);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
                    binding.imageView.setImageBitmap(bitmap);

                }
                cursor.close();


            } catch (Exception e){
                e.printStackTrace();
            }

        }


    }

    public void save(View view){
        String english = binding.englishText.getText().toString();
        String turkish = binding.turkishText.getText().toString();

        Bitmap smallImage = makeSmallerImage(selectedImage,300);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        smallImage.compress(Bitmap.CompressFormat.PNG,50,outputStream);
        byte[] byteArray = outputStream.toByteArray();
        try {
            database = this.openOrCreateDatabase("Wordssss", MODE_PRIVATE,null);
            database.execSQL("CREATE TABLE IF NOT EXISTS wordssss (id INTEGER PRIMARY KEY, englishname VARCHAR, turkishname VARCHAR, image BLOB)");

            String sqlString = "INSERT INTO wordssss(englishname, turkishname, image) VALUES(?, ?, ?)";
            SQLiteStatement sqLiteStatement = database.compileStatement(sqlString);
            sqLiteStatement.bindString(1,english);
            sqLiteStatement.bindString(2,turkish);
            sqLiteStatement.bindBlob(3,byteArray);
            sqLiteStatement.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }


        Intent intent = new Intent(DictionaryActivity4.this,MainActivity4.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
    // DictionaryActivity içinde
    public void deleteWord(View view) {
        if (info.equals("oldddd")) {
            // Eğer kelime daha önce kaydedilmişse (yani güncellenen bir kelimeyse), silme işlemini gerçekleştir
            try {
                // Kelimenin ID'sini al
                int wordId = getIntent().getIntExtra("wordId", 1);

                // Veritabanından kelimeyi sil
                database.execSQL("DELETE FROM wordssss WHERE id = ?", new Object[]{wordId});

                // Kullanıcıyı bilgilendir
                Toast.makeText(this, "Word deleted successfully", Toast.LENGTH_SHORT).show();

                // Ana ekrana geri dön
                Intent intent = new Intent(DictionaryActivity4.this, MainActivity4.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Eğer yeni eklenen bir kelimeyse, silme işlemi yapılamaz, kullanıcıyı bilgilendir
            Toast.makeText(this, "Cannot delete a new word", Toast.LENGTH_SHORT).show();
        }
    }
    public Bitmap makeSmallerImage(Bitmap image, int maximumSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1){
            //yatay görünüm
            width = maximumSize;
            height = (int) (width / bitmapRatio);
        } else {
            //dikey görünüm
            height = maximumSize;
            width = (int) (height * bitmapRatio);
        }
        return image.createScaledBitmap(image,width,height,true);
    }
    public void selectImage(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            //Android 33 ve üstüyse -> READ_MEDIA_IMAGES
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_MEDIA_IMAGES)) {

                    Snackbar.make(view,"Permission needed for gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission",new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //request permission
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);

                        }
                    }).show();

                } else {
                    //request permission
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);

                }

            } else {
                //gallery
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intentToGallery);
            }
        } else {
            //Android 32 ve altındaysa -> READ_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    Snackbar.make(view,"Permission needed for gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //request permission
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

                        }
                    }).show();

                } else {
                    //request permission
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

                }

            } else {
                //gallery
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intentToGallery);
            }
        }



    }

    private void registerLauncher() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK){
                    Intent intentFromResult = result.getData();
                    if (intentFromResult != null){
                        Uri imageData = intentFromResult.getData();
                        // binding.imageView.setImageURI(imageData);
                        try {
                            if (Build.VERSION.SDK_INT >= 28){
                                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(),imageData);
                                selectedImage = ImageDecoder.decodeBitmap(source);
                                binding.imageView.setImageBitmap(selectedImage);
                            } else {
                                selectedImage = MediaStore.Images.Media.getBitmap(DictionaryActivity4.this.getContentResolver(),imageData);
                                binding.imageView.setImageBitmap(selectedImage);
                            }

                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        });
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result) {
                    //permission granted
                    Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGallery);

                } else {
                    //permission denied
                    Toast.makeText(DictionaryActivity4.this, "Permission needed!",Toast.LENGTH_LONG).show();

                }

            }
        });
    }
}