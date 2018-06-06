package org.androidtown.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class LoadImageActivity extends BaseActivity implements View.OnClickListener {

    Handler handler = new Handler(Looper.getMainLooper());
    private static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";

    //서버키 나중에 비공개로: 디비에 저장해놓고 앱 실행하면 불러오기
    private String ServerKey;
    private static final String TestMsg = "push message test";

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceForPartner;
    private DatabaseReference databaseReferenceForPushMsgTest;
    private DatabaseReference databaseReferenceForServerKey;
    private DatabaseReference databaseReferenceForUserName;

    private FirebaseStorage storage;
    private StorageReference mStorageRef;

    Button loadButton, sendButton;
    ImageView loadImgae;

    final int PICK_FROM_ALBUM = 0;
    final int PICK_FROM_CAMERA = 1;
    final int CROP_FROM_IMAGE = 2;

    private int id_view;

    String UserName;
    String friendID;
    String UserID;
    int habitIdx;

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

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        UserID = bundle.getString("ID");
        habitIdx = bundle.getInt("INDEX");
        Toast.makeText(getApplicationContext(), "/////"+habitIdx, Toast.LENGTH_SHORT).show();

        loadImgae = (ImageView) findViewById(R.id.imageLoad);
        loadButton = (Button) findViewById(R.id.loadButton);
        sendButton = (Button) findViewById(R.id.sendButton);

        loadImgae.setOnClickListener(this);
        loadButton.setOnClickListener(this);
        sendButton.setOnClickListener(this);

        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReference();

        database = FirebaseDatabase.getInstance();

        //server key
        databaseReferenceForServerKey = database.getReference("ServerKey");
        databaseReferenceForServerKey.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ServerKey = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //databaseReference = database.getReference("users/" + id);
        databaseReference = database.getReference("users/" + UserID);

        //로그인되면 스마트폰 주소 받아오기
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        databaseReference.child("fcmToken").setValue(refreshedToken);
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



        if (view.getId() == R.id.sendButton) {
            sendPostToFCM("확인해주세요!");

            //Uri file = Uri.fromFile(new File(absoultePath));
            //absoultePath //mStorageRef
            /*if (file != null) {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("업로드중...");
                progressDialog.show();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
                Date now = new Date();
                String filename = formatter.format(now) + ".png";//".jpg"?

                StorageReference storageRef = storage.getReferenceFromUrl("gs://mobileproject-57744.appspot.com/").child("images/" + filename);

                storageRef.putFile(file)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                            }

                        })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                            }
                        })

                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                @SuppressWarnings("VisibleForTests")
                                double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                                progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                            }
                        });

            } else {
                Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
            }*/

        }


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

        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case PICK_FROM_ALBUM: {
                mlmageCaptureUri = data.getData();
                Log.d("BORAM", mlmageCaptureUri.getPath().toString());
            }

            case PICK_FROM_CAMERA: {
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mlmageCaptureUri, "image/");

                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("return-data", true);

                startActivityForResult(intent, CROP_FROM_IMAGE);
                break;
            }

            case CROP_FROM_IMAGE: {
                if (resultCode != RESULT_OK)
                    return;

                final Bundle extras = data.getExtras();

                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                      "/BORAM/" + System.currentTimeMillis() + ".jpg";


                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    loadImgae.setImageBitmap(photo);

                    storeCropImage(photo, filePath);

                    absoultePath = filePath + ".jpg";
                    break;
                }

                File f = new File(mlmageCaptureUri.getPath());
                if (f.exists()) {
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
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BORAM/";
        File directory_BORAM = new File(dirPath);

        if (!directory_BORAM.exists())
            directory_BORAM.mkdir();

        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try {
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

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


    //사진이 업로드 되지 않았으면 토스트 띄워주기 --> boolean으로 변수 두면 될듯
    private void sendPostToFCM(final String message) {
        //databaseReferenceForPushMsgTest = database.getReference("users/" + userID.getText().toString()+"/habits/"+habitIdx+"/FRIENDID");
        //databaseReferenceForPushMsgTest = database.getReference("users/" + userID.getText().toString());
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        UserID = bundle.getString("ID");
        habitIdx = bundle.getInt("INDEX");
        //String habitIdxString = String.valueOf(habitIdx);
        databaseReferenceForUserName = database.getReference("users/" + UserID);
        //Toast.makeText(getApplicationContext(), "test: " + UserID, Toast.LENGTH_SHORT).show();
        databaseReferenceForUserName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserName = (String) dataSnapshot.child("NAME").getValue();

                databaseReferenceForPartner = database.getReference("users/" + UserID + "/habits/current/" + habitIdx);
                Toast.makeText(getApplicationContext(), "users/"+UserID+"/habits/"+habitIdx, Toast.LENGTH_SHORT).show();
                databaseReferenceForPartner.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        friendID = (String) dataSnapshot.child("FRIENDID").getValue();
                        Toast.makeText(getApplicationContext(), "friend / "+friendID, Toast.LENGTH_SHORT).show();

                        databaseReferenceForPushMsgTest = database.getReference("users/" + friendID);
                        databaseReferenceForPushMsgTest.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //받는 사람 주소
                                final String fcmToken = (String) dataSnapshot.child("fcmToken").getValue();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            //FMC 메세지 만들기
                                            JSONObject root = new JSONObject();
                                            JSONObject notification = new JSONObject();
                                            //notification.put("body", message);
                                            notification.put("body", "확인해주세요!");
                                            notification.put("title", UserName + " 님이 인증을 요청했어요~");
                                            root.put("notification", notification);
                                            root.put("to", fcmToken);

                                            URL Url = new URL(FCM_MESSAGE_URL);
                                            HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
                                            conn.setRequestMethod("POST");
                                            conn.setDoOutput(true);
                                            conn.setDoInput(true);
                                            conn.addRequestProperty("Authorization", "key=" + ServerKey);
                                            conn.setRequestProperty("Accept", "application/json");
                                            conn.setRequestProperty("Content-type", "application/json");
                                            OutputStream os = conn.getOutputStream();
                                            os.write(root.toString().getBytes("utf-8"));
                                            os.flush();
                                            conn.getResponseCode();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}