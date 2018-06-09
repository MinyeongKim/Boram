package org.androidtown.myapplication;

import android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class LoadImageActivity extends BaseActivity implements View.OnClickListener {

    Handler handler = new Handler(Looper.getMainLooper());
    private static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";

    //서버키 나중에 비공개로: 디비에 저장해놓고 앱 실행하면 불러오기
    private String ServerKey;
    private static final String TestMsg = "push message test";

    private StorageReference mImageRef;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceHistoryIndex;
    private DatabaseReference databaseReferenceForPartner;
    private DatabaseReference databaseReferenceForPushMsgTest;
    private DatabaseReference databaseReferenceForServerKey;
    private DatabaseReference databaseReferenceForUserName;
    private DatabaseReference databaseReference1;

    private FirebaseStorage storage;
    private StorageReference mStorageRef;

    String filename;
    Uri selectedImage;
    private Uri filePath;
    String HISTORYidx;
    String habitTitle;
    String nextPersonName = "";

    Button loadButton, sendButton;
    ImageView loadImgae;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_ALBUM = 2;
    private static final int CROP_FROM_CAMERA = 3;

    private Uri photoUri;
    private String[] permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA};
    private static final int MULTIPLE_PERMISSIONS = 101;

    private String mCurrentPhotoPath;

    private int id_view;

    String UserName;
    String friendID;
    String UserID;
    int habitIdx;
    String checkType;

    String mlmageCaptureUri;
    Uri photoURI;
    Uri albumURI;
    String absoultePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupActionBar();

        checkPermissions();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        UserID = bundle.getString("ID");
        habitIdx = bundle.getInt("INDEX");
        Toast.makeText(getApplicationContext(), "/////" + habitIdx, Toast.LENGTH_SHORT).show();

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
        databaseReferenceHistoryIndex = database.getReference("users/" + UserID + "/habits/current/" + habitIdx + "/history");

        //로그인되면 스마트폰 주소 받아오기
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        databaseReference.child("fcmToken").setValue(refreshedToken);
    }

    private boolean checkPermissions() {
        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(this, pm);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(pm);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private void showNoPermissionToastAndFinish() {
        Toast.makeText(this, "권한 요청에 동의 해주셔야 이용 가능합니다. 설정에서 권한 허용 하시기 바랍니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void onClick(View view) {
        id_view = view.getId();

        if (view.getId() == R.id.imageLoad || view.getId() == R.id.loadButton) {

            DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int i) {
                    takePhoto();
                }
            };

            DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    goToAlbum();
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
            //sendPostToFCM("확인해주세요!");
            //uploadPhoto(selectedImage);
            //uploadPhoto();
            uploadFile();

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

    private void goToAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(LoadImageActivity.this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(LoadImageActivity.this,
                    "org.androidtown.myapplication.provider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, PICK_FROM_CAMERA);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "BORAM_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/BORAM/");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        mlmageCaptureUri = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i].equals(this.permissions[0])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();
                            }
                        } else if (permissions[i].equals(this.permissions[1])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        } else if (permissions[i].equals(this.permissions[2])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        }
                    }
                } else {
                    showNoPermissionToastAndFinish();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode == PICK_FROM_ALBUM) {
            if (data == null) {
                return;
            }
            photoUri = data.getData();
            cropImage();
        } else if (requestCode == PICK_FROM_CAMERA) {
            cropImage();
            // 갤러리에 나타나게
            MediaScannerConnection.scanFile(LoadImageActivity.this,
                    new String[]{photoUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        } else if (requestCode == CROP_FROM_CAMERA) {
            loadImgae.setImageURI(null);
            loadImgae.setImageURI(photoUri);
        }
    }

    private void cropImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.grantUriPermission("com.android.camera", photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            grantUriPermission(list.get(0).activityInfo.packageName, photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        int size = list.size();
        if (size == 0) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(this, "용량이 큰 사진의 경우 시간이 오래 걸릴 수 있습니다.", Toast.LENGTH_SHORT).show();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            File croppedFileName = null;
            try {
                croppedFileName = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File folder = new File(Environment.getExternalStorageDirectory() + "/BORAM/");
            File tempFile = new File(folder.toString(), croppedFileName.getName());

            photoUri = FileProvider.getUriForFile(LoadImageActivity.this,
                    "org.androidtown.myapplication.provider", tempFile);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }

            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

            Intent i = new Intent(intent);
            ResolveInfo res = list.get(0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                grantUriPermission(res.activityInfo.packageName, photoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            startActivityForResult(i, CROP_FROM_CAMERA);
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
        checkType = bundle.getString("Check_type");
        //String habitIdxString = String.valueOf(habitIdx);

        if (checkType.equals("friend")) {
            databaseReferenceForUserName = database.getReference("users/" + UserID);
            //Toast.makeText(getApplicationContext(), "test: " + UserID, Toast.LENGTH_SHORT).show();
            databaseReferenceForUserName.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserName = (String) dataSnapshot.child("NAME").getValue();
                    databaseReferenceHistoryIndex.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int historyIndex = (int) dataSnapshot.getChildrenCount();
                            HISTORYidx = String.valueOf(historyIndex);
                            databaseReferenceHistoryIndex.child(HISTORYidx).child("ImageID").setValue(filename);

                            databaseReferenceForPartner = database.getReference("users/" + UserID + "/habits/current/" + habitIdx);
                            Toast.makeText(getApplicationContext(), "users/" + UserID + "/habits/" + habitIdx, Toast.LENGTH_SHORT).show();
                            databaseReferenceForPartner.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    friendID = (String) dataSnapshot.child("FRIENDID").getValue();
                                    habitTitle = (String) dataSnapshot.child("TITLE").getValue();
                                    Toast.makeText(getApplicationContext(), "friend / " + friendID, Toast.LENGTH_SHORT).show();

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
                                                        notification.put("body", "확인해주세요!");//습관제목?
                                                        notification.put("title", UserName + " 님이 인증을 요청했어요~");
                                                        //notification.put("ImgName", filename);
                                                        notification.put("tag", filename + "," + habitTitle + "," + UserID + "," + habitIdx + "," + HISTORYidx);//filename+habit title+userid+habitIdx+HISTORYidx
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

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else if (checkType.equals("otherPerson")) {

            databaseReferenceForUserName = database.getReference("withOthersList");
            databaseReferenceForUserName.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean nextTemp = false;
                    Iterator<DataSnapshot> userList = dataSnapshot.getChildren().iterator();
                    int i = 0;
                    String firstPersonName = "";
                    while (userList.hasNext()) {
                        DataSnapshot data = userList.next();
                        if (data.getKey().equals(UserID) && !nextTemp) {
                            if (i == 0) {
                                firstPersonName = data.getKey();
                                i = 1;
                            }
                            nextTemp = true;
                            //return;
                        } else {
                            nextPersonName = data.getKey();

                            databaseReferenceForUserName = database.getReference("users/" + nextPersonName);
                            //Toast.makeText(getApplicationContext(), "test: " + UserID, Toast.LENGTH_SHORT).show();
                            databaseReferenceForUserName.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    UserName = (String) dataSnapshot.child("NAME").getValue();
                                    databaseReferenceHistoryIndex.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            int historyIndex = (int) dataSnapshot.getChildrenCount();
                                            HISTORYidx = String.valueOf(historyIndex);
                                            databaseReferenceHistoryIndex.child(HISTORYidx).child("ImageID").setValue(filename);

                                            databaseReferenceForPartner = database.getReference("users/" + UserID + "/habits/current/" + habitIdx);
                                            Toast.makeText(getApplicationContext(), "users/" + UserID + "/habits/" + habitIdx, Toast.LENGTH_SHORT).show();
                                            databaseReferenceForPartner.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    friendID = (String) dataSnapshot.child("FRIENDID").getValue();
                                                    habitTitle = (String) dataSnapshot.child("TITLE").getValue();
                                                    Toast.makeText(getApplicationContext(), "friend / " + friendID, Toast.LENGTH_SHORT).show();

                                                    databaseReferenceForPushMsgTest = database.getReference("users/" + nextPersonName);
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
                                                                        notification.put("body", "확인해주세요!");//습관제목?
                                                                        notification.put("title", "누군가 인증을 요청했어요~");
                                                                        //notification.put("ImgName", filename);
                                                                        notification.put("tag", filename + "," + habitTitle + "," + UserID + "," + habitIdx + "," + HISTORYidx);//filename+habit title+userid+habitIdx+HISTORYidx
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

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            //Toast.makeText(getApplication(), nextPersonName, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    if (!nextTemp) {
                        databaseReferenceForUserName = database.getReference("users/" + firstPersonName);
                        //Toast.makeText(getApplicationContext(), "test: " + UserID, Toast.LENGTH_SHORT).show();
                        databaseReferenceForUserName.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                UserName = (String) dataSnapshot.child("NAME").getValue();
                                databaseReferenceHistoryIndex.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        int historyIndex = (int) dataSnapshot.getChildrenCount();
                                        HISTORYidx = String.valueOf(historyIndex);
                                        databaseReferenceHistoryIndex.child(HISTORYidx).child("ImageID").setValue(filename);

                                        databaseReferenceForPartner = database.getReference("users/" + UserID + "/habits/current/" + habitIdx);
                                        Toast.makeText(getApplicationContext(), "users/" + UserID + "/habits/" + habitIdx, Toast.LENGTH_SHORT).show();
                                        databaseReferenceForPartner.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                friendID = (String) dataSnapshot.child("FRIENDID").getValue();
                                                habitTitle = (String) dataSnapshot.child("TITLE").getValue();
                                                Toast.makeText(getApplicationContext(), "friend / " + friendID, Toast.LENGTH_SHORT).show();

                                                databaseReferenceForPushMsgTest = database.getReference("users/" + nextPersonName);
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
                                                                    notification.put("body", "확인해주세요!");//습관제목?
                                                                    notification.put("title", "누군가 인증을 요청했어요~");
                                                                    //notification.put("ImgName", filename);
                                                                    notification.put("tag", filename + "," + habitTitle + "," + UserID + "," + habitIdx + "," + HISTORYidx);//filename+habit title+userid+habitIdx+HISTORYidx
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

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
        }

    }
    /*private void uploadPhoto(Uri uri) {
        // Reset UI
        //hideDownloadUI();
        Toast.makeText(this, "Uploading...", Toast.LENGTH_SHORT).show();


        final String TAG ="aaa";


        // Upload to Cloud Storage
        //String uuid = UUID.randomUUID().toString();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://mobileproject-57744.appspot.com/").child("images");
        storageRef.putFile(selectedImage)
                .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "uploadPhoto:onSuccess:" +
                                    taskSnapshot.getMetadata().getReference().getPath());
                        }
                        Toast.makeText(LoadImageActivity.this, "Image uploaded",
                                Toast.LENGTH_SHORT).show();

                        //showDownloadUI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "uploadPhoto:onError", e);
                        Toast.makeText(LoadImageActivity.this, "Upload failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }*/

    /*private void uploadPhoto(){
        Uri file = Uri.fromFile(new File(absoultePath));
        //absoultePath //mStorageRef
            if (file != null) {
                final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
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
            }
    }*/

    private void uploadFile() {
        //업로드할 파일이 있으면 수행
        if (mCurrentPhotoPath != null) {
            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("업로드중...");
            progressDialog.show();

            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();

            //Unique한 파일명을 만들자.
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            filename = formatter.format(now) + ".png";
            //storage 주소와 폴더 파일명을 지정해 준다.
            StorageReference storageRef = storage.getReferenceFromUrl("gs://mobileproject-57744.appspot.com/").child("images/" + filename);
            //올라가거라...
            storageRef.putFile(Uri.parse(mCurrentPhotoPath))
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                            Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                            sendPostToFCM("확인해주세요!");
                            finish();
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력해 준다
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }

}