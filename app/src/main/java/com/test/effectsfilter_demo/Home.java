package com.test.effectsfilter_demo;

/**
 * Created by Cee on 03/03/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Home extends Activity {

    //COSTANTI
    static final int REQUEST_TAKE_PHOTO = 1;


    //ATTRIBUTI
    //path del filePhoto
    String mCurrentPhotoPath;
    //file photo
    File photoFile = null;
    //View Immagine
    private ImageView imageView1;
    //bottone per scatto photo
    private Button photoButton;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home);
        //dispatchTakePictureIntent();
        /*
        Se si clicca il bottone parte l'intent della fotocamera android
         */
        photoButton = (Button) findViewById(R.id.takePhoto);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ButtonPhoto","PREMUTO");
                dispatchTakePictureIntent();
            }
        });


    }//end onCreate()


    //implemento in bianco questo metodo in modo che non si può tornare alla splashscreeen
    @Override
    public void onBackPressed() {
    }//end onBackPressed()

    //-------------------------------------------------------------------------
    //!!!!!!!!!!!!!!------CAPIRE BENE DOVE USARE QUESTO METODO-------!!!!!!!!!!
    //-------------------------------------------------------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch(requestCode){
                case REQUEST_TAKE_PHOTO:
                    final File file = new File(mCurrentPhotoPath);
                    try {
                        Bitmap captureBmp = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(photoFile));
                        // do whatever you want with the bitmap (Resize, Rename, Add To Gallery, etc)
                        //this.imageView1 = (ImageView)this.findViewById(R.id.imageView1);
                        imageView1.setImageBitmap(captureBmp);
                        Intent modifyIntent = new Intent(this,EffectsFilterActivity.class);
                        modifyIntent.putExtra("bitmap", captureBmp);
                        modifyIntent.putExtra("fileForBitmap",file);
                        startActivity(modifyIntent);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }//End onActivityResult



    //Medoto per lo scatto della foto tramite stream della fotocamera
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go

            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);



            }
        }
    }






    //Metodo che prepara il file dove sarà messo lo stream della fotocamera+



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }//END createImageFile()





}//END CLASS
