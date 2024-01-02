package com.example.myapplication.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ImageAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ImgActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private GridView imageGridView;
    private ImageAdapter imageAdapter;
    private ArrayList<Uri> imageList = new ArrayList<>();
    private ArrayList<JSONObject> imageInfoList = new ArrayList<>(); // 이미지 정보를 저장할 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);
        imageInfoList = new ArrayList<>();

        // JSON으로 이미지 정보를 저장할 ArrayList
        imageInfoList = new ArrayList<>();

        Button callCamButton = findViewById(R.id.imgCam);
        Button btnSelectImage = findViewById(R.id.btnSelectImage);
        imageGridView = findViewById(R.id.imageGridView);

        // 어댑터 초기화
        imageAdapter = new ImageAdapter(this, imageList);
        imageGridView.setAdapter(imageAdapter);

        //이미지 클릭 시 팝업 띄우기
        // 그리드뷰에서 이미지를 클릭하여 이름을 수정할 수 있는 기능 추가

        // 이미지 클릭 시 팝업 띄우기
        // 그리드뷰에서 이미지를 클릭하여 이름을 수정할 수 있는 기능 추가
        imageGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject imageInfo = imageInfoList.get(position);
                String imageName = getImageName(imageInfo);
                showRenameDialog(position, imageName, imageInfoList);
            }
        });

        callCamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionAndOpenGallery();
            }
        });

        // 그리드뷰에서 롱클릭하여 이미지 삭제
        imageGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                imageList.remove(position);
                imageAdapter.notifyDataSetChanged();
                Toast.makeText(ImgActivity.this, "이미지가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    // 이미지의 파일 이름 가져오는 메서드
    private String getFileName(Uri uri) {
        String fileName = null;
        String[] projection = {MediaStore.Images.Media.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            fileName = cursor.getString(columnIndex);
            cursor.close();
        }
        return fileName;
    }
    // 이미지의 이름 가져오는 메서드
    private String getImageName(JSONObject imageInfo) {
        try {
            return imageInfo.getString("imageName");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "input";
    }


    // 이름 수정 팝업창을 띄우는 메서드
    // 이름 수정 팝업창을 띄우는 메서드
    private void showRenameDialog(final int position, String currentName, final ArrayList<JSONObject> imageInfoList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("NAME");
        final EditText input = new EditText(this);
        input.setText(currentName);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = input.getText().toString();
                try {
                    JSONObject imageInfo = imageInfoList.get(position);
                    imageInfo.put("imageName", newName);
                    imageAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // 수정된 코드: 팝업창을 띄움
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }




    // 이미지 이름을 수정하는 메서드
    // ImgActivity 클래스 내 renameImage() 메서드 수정
    private void renameImage(int position, String newName) {
        imageList.set(position, renameUri(imageList.get(position), newName));
        imageAdapter.notifyDataSetChanged();
    }

    private Uri renameUri(Uri uri, String newName) {
        // 파일 이름 가져오기
        String fileName = getFileName(uri);
        // 파일 경로에서 파일 이름을 새 이름으로 교체
        String newUriString = uri.toString().replace(fileName, newName);
        return Uri.parse(newUriString);
    }



    private void checkPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_REQUEST_CODE);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);

    }


    private void checkPermissionAndOpenGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);
            } else {
                openGallery();
            }
        } else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private Uri getImageUri(ImgActivity imgActivity, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    // 갤러리에서 이미지를 선택한 후 호출되는 메서드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null) {
                Uri selectedImageUri = data.getData();
                imageList.add(selectedImageUri);
                // 이미지 정보 생성 및 추가
                JSONObject imageInfo = new JSONObject();
                try {
                    imageInfo.put("imageName", "input");
                    // 이미지 정보를 리스트에 추가
                    imageInfoList.add(imageInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                imageAdapter.notifyDataSetChanged();
            } else if (requestCode == CAMERA_REQUEST_CODE && data != null) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    Uri imageUri = getImageUri(this, imageBitmap);
                    imageList.add(imageUri);
                    // 이미지 정보 생성 및 추가
                    JSONObject imageInfo = new JSONObject();
                    try {
                        imageInfo.put("imageName", "input");
                        // 이미지 정보를 리스트에 추가
                        imageInfoList.add(imageInfo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    imageAdapter.notifyDataSetChanged();
                }
            }
        }
    }

}