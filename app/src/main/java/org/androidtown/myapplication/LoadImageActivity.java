package org.androidtown.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class LoadImageActivity extends BaseActivity implements View.OnClickListener {

    Button loadButton, sendButton;
    ImageView loadImgae;

    final int PICK_FROM_ALBUM = 0;
    final int PICK_FROM_CAMERA=1;
    final int CROP_FROM_IMAGE =2;

    private int id_view;

    Uri mlmageCaptureUri, photoURI, albumURI;
    String absoultePath;
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupActionBar();

        loadImgae = (ImageView) findViewById(R.id.imageLoad);
        loadButton = (Button) findViewById(R.id.loadButton);
        sendButton = (Button) findViewById(R.id.sendButton);

        loadImgae.setOnClickListener(this);
        loadButton.setOnClickListener(this);
        sendButton.setOnClickListener(this);
    }

    public void onClick(View view) {
        id_view = view.getId();

        if (view.getId() == R.id.imageLoad || view.getId() == R.id.loadButton) {

            DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int i) {
                    doTakePhotoAction();
                }
            };

            DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    doTakeAlbumAction();
                }
            };

            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                }
            };

            new AlertDialog.Builder(this).setTitle("업로드할 이미지 선택")
                    .setNegativeButton("사진촬영", cameraListener)
                    .setNeutralButton("앨범선택", albumListener)
                    .setPositiveButton("취소", cancelListener).show();
        }

        //전송 버튼 눌리면 전송해줘야함
        //푸쉬메세지와 함께 사용자가 입력한 값과 사진을
        //if()
    }

    private void doTakeAlbumAction() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    //사진 찍기
    private void doTakePhotoAction() {
        /*
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takePictureIntent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(takePictureIntent, PICK_FROM_CAMERA);
        }
        */

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, PICK_FROM_CAMERA);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode!=RESULT_OK)
            return;

        switch (requestCode){
            case PICK_FROM_ALBUM:
            {
                mlmageCaptureUri=data.getData();
                Log.d("BORAM",mlmageCaptureUri.getPath().toString());
            }

            case PICK_FROM_CAMERA:
            {
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mlmageCaptureUri,"image/");

                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("return-data", true);

                startActivityForResult(intent, CROP_FROM_IMAGE);
                break;
            }

            case CROP_FROM_IMAGE:
            {
                if(resultCode!=RESULT_OK)
                    return;

                final Bundle extras = data.getExtras();

                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+
                        "/BORAM/"+System.currentTimeMillis()+".jpg";

                if(extras!=null)
                {
                    Bitmap photo = extras.getParcelable("data");
                    loadImgae.setImageBitmap(photo);

                    storeCropImage(photo,filePath);

                    absoultePath = filePath;
                    break;
                }

                File f = new File(mlmageCaptureUri.getPath());
                if(f.exists()){
                    f.delete();
                }
            }
        }

        /*
        if(resultCode == RESULT_OK){
            loadImgae.setImageURI(data.getData());
        }

        if(requestCode==PICK_FROM_CAMERA &&requestCode==RESULT_OK){


            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            loadImgae.setImageBitmap(imageBitmap);
        }

        /*
        else if  (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK) {
            try {
                //Uri에서 이미지 이름을 얻어온다.
                //String name_Str = getImageNameToUri(data.getData());

                //이미지 데이터를 비트맵으로 받아온다.
                Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                //Bitmap image_bitmap=

                //배치해놓은 ImageView에 set
                loadImgae.setImageBitmap(image_bitmap);
                Toast.makeText(getApplication(),"Image",Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        */
        /*
        if (requestCode == PICK_FROM_ALBUM) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    //String name_Str = getImageNameToUri(data.getData());

                    //이미지 데이터를 비트맵으로 받아온다.
                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    //배치해놓은 ImageView에 set
                    loadImgae.setImageBitmap(image_bitmap);

                    final Bundle extras = data.getExtras();

                    String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+
                            "/BORAM/"+System.currentTimeMillis()+".jpg";

                    if(extras!=null)
                    {
                        Bitmap photo = extras.getParcelable("data");
                        loadImgae.setImageBitmap(photo);

                        storeCropImage(photo,filePath);

                        absoultePath = filePath;
                    }

                    File f = new File(mlmageCaptureUri.getPath());
                    if(f.exists()){
                        f.delete();
                    }
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        */
        /*
        if(resultCode!=RESULT_OK)
            return;

        switch (requestCode){
            case PICK_FROM_ALBUM:
            {
                mlmageCaptureUri=data.getData();
                Log.d("BORAM",mlmageCaptureUri.getPath().toString());
            }

            case PICK_FROM_CAMERA:
            {
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mlmageCaptureUri,"image/");

                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("return-data", true);

                startActivityForResult(intent, CROP_FROM_IMAGE);
                break;
            }

            case CROP_FROM_IMAGE:
            {
                if(resultCode!=RESULT_OK)
                    return;

                final Bundle extras = data.getExtras();

                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+
                        "/BORAM/"+System.currentTimeMillis()+".jpg";

                if(extras!=null)
                {
                    Bitmap photo = extras.getParcelable("data");
                    loadImgae.setImageBitmap(photo);

                    storeCropImage(photo,filePath);

                    absoultePath = filePath;
                    break;
                }

                File f = new File(mlmageCaptureUri.getPath());
                if(f.exists()){
                    f.delete();
                }
            }
        }
        */
    }

    private void storeCropImage(Bitmap bitmap, String filePath) {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/BORAM/";
        File directory_BORAM = new File(dirPath);

        if(!directory_BORAM.exists())
            directory_BORAM.mkdir();

        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try{
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));

            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));

            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //뒤로가는 버튼 생성
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    //뒤로가기 버튼이 눌렀을 경우
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}