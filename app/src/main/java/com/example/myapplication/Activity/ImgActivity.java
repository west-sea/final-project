package com.example.myapplication.Activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.myapplication.Class.ImageDetails;
import com.example.myapplication.R;
import com.example.myapplication.adapter.ImageAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ImgActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;

    private static final int PERMISSION_REQUEST_CODE = 100;
    private SharedPreferences sharedPreferences;

    private GridView imageGridView;
    private ImageAdapter imageAdapter;
    private ArrayList<Uri> imageList = new ArrayList<>();
    private ArrayList<ImageDetails> imageDetailsList = new ArrayList<>();

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

        JSONObject imageInfo = new JSONObject();
        ImageDetails imageDetails = new ImageDetails("input", null, "");
        imageDetailsList.add(imageDetails);
        imageAdapter.notifyDataSetChanged();
        sharedPreferences = getPreferences(MODE_PRIVATE);


        loadImageDetails();

        //이미지 클릭 시 팝업 띄우기
        // 그리드뷰에서 이미지를 클릭하여 이름을 수정할 수 있는 기능 추가

        // 이미지 클릭 시 팝업 띄우기
        // 그리드뷰에서 이미지를 클릭하여 이름을 수정할 수 있는 기능 추가
        imageGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("ImgActivity", "Clicked position: " + position);
                Log.d("ImgActivity", "ImageDetailsList size: " + imageDetailsList.size());

                if (position < imageDetailsList.size()) { // 리스트의 크기를 확인하여 유효한 인덱스에만 접근
                    ImageDetails imageDetails = imageDetailsList.get(position);
                    showRenameDialog(position, imageDetails);
                }
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

    private void loadImageDetails() {
        String detailsString = sharedPreferences.getString("imageDetails", "");
        if (!detailsString.isEmpty()) {
            try {
                JSONArray jsonArray = new JSONArray(detailsString);
                imageDetailsList.clear(); // 기존 정보 지우기

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String imageName = jsonObject.getString("imageName");
                    String selectedDate = jsonObject.optString("selectedDate", null);
                    String description = jsonObject.optString("description", null);

                    ImageDetails imageDetails = new ImageDetails(imageName, parseDateString(selectedDate), description);
                    imageDetails.setIdFromJson(jsonObject);
                    imageDetailsList.add(imageDetails);
                }

                imageAdapter.notifyDataSetChanged(); // 어댑터 갱신
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void addImageDetails(String imageName, Date selectedDate, String description) {
        ImageDetails imageDetails = new ImageDetails(imageName, selectedDate, description);
        imageDetailsList.add(imageDetails);
    }

    private void saveImageDetails() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        try {
            // ArrayList를 JSONArray로 변환하여 저장
            JSONArray jsonArray = new JSONArray();
            for (ImageDetails details : imageDetailsList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", details.getId());
                jsonObject.put("imageName", details.getImageName());

                // Adding date information (if available)
                if (details.getSelectedDate() != null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String dateString = dateFormat.format(details.getSelectedDate());
                    jsonObject.put("selectedDate", dateString);
                }

                // Adding description information (if available)
                if (details.getDescription() != null) {
                    jsonObject.put("description", details.getDescription());
                }

                // Add other information if needed

                jsonArray.put(jsonObject);
            }
            editor.putString("imageDetails", jsonArray.toString());
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onDestroy() {
        // 추가: 액티비티가 종료될 때 이미지 정보 저장
        saveImageDetails();

        super.onDestroy();
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



    private void showRenameDialog(final int position, ImageDetails imageDetails) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Image Details");

        // Inflate the custom layout
        View dialogView = getLayoutInflater().inflate(R.layout.img_dialog_layout, null);
        builder.setView(dialogView);

        // Find the views in the custom layout
        EditText editImageName = dialogView.findViewById(R.id.editImageName);
        Button showDatePickerButton = dialogView.findViewById(R.id.showDatePickerButton);
        EditText editSelectedDate = dialogView.findViewById(R.id.editSelectedDate);
        EditText editDescription = dialogView.findViewById(R.id.editDescription);

        // Set initial values
        editImageName.setText(imageDetails.getImageName());

        String initialDateString = formatDateString(imageDetails.getSelectedDate());
        editSelectedDate.setText(initialDateString);
        editDescription.setText(imageDetails.getDescription());

        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            // Update the EditText with the selected date
            String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            editSelectedDate.setText(selectedDate);
        };

        showDatePickerButton.setOnClickListener(v -> {
            // Show the DatePickerDialog when the button is clicked
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
            // Add the following code to set a listener to handle the date pick action
            datePickerDialog.setOnDateSetListener((view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                // Update the EditText with the selected date
                String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDayOfMonth;
                editSelectedDate.setText(selectedDate);
            });
            datePickerDialog.show();
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retrieve updated values from EditTexts
                String newName = editImageName.getText().toString();
                String dateString = editSelectedDate.getText().toString();
                Date newDate = parseDateString(dateString);
                String newDescription = editDescription.getText().toString();

                // Update the ImageDetails object
                ImageDetails updatedDetails = imageDetailsList.get(position);
                updatedDetails.setImageName(newName);
                updatedDetails.setSelectedDate(newDate);
                updatedDetails.setDescription(newDescription);

                // 저장된 이미지 정보 갱신
                saveImageDetails();

                imageAdapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Show the dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private String formatDateString(Date date) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return dateFormat.format(date);
        }
        return "";
    }
    private Date parseDateString(String dateString) {
        if (dateString != null && !dateString.isEmpty()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            try {
                // SimpleDateFormat을 사용하여 문자열을 Date 객체로 파싱
                return dateFormat.parse(dateString);
            } catch (ParseException e) {
                // 파싱 오류가 발생할 경우 처리
                e.printStackTrace(); // 또는 로그 출력 등의 적절한 오류 처리
                return null; // 오류 발생 시 null 반환
            }
        }
        return null; // dateString이 null 또는 빈 문자열인 경우 null 반환
    }


    // 이름 수정 팝업창을 띄우는 메서드
    // 이름 수정 팝업창을 띄우는 메서드




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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 권한 요청 결과를 처리
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 허용된 경우 카메라 열기
                Log.d("ImgActivity", "Camera permission granted. Opening camera...");
                openCamera();
            } else {
                // 권한이 거부된 경우 사용자에게 설명이나 다른 조치를 요구할 수 있습니다.
                Log.d("ImgActivity", "Camera permission denied.");
                showPermissionRequestDialog();
            }
        }
    }

    private void showPermissionRequestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("카메라 권한이 필요합니다.")
                .setMessage("이 앱에서 카메라를 사용하려면 권한이 필요합니다. 설정에서 권한을 부여해주세요.")
                .setPositiveButton("설정으로 이동", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 설정으로 이동하는 인텐트 생성
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        // 설정으로 이동
                        startActivity(intent);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 사용자가 권한 부여를 거부한 경우 처리
                        Toast.makeText(ImgActivity.this, "권한이 거부되어 카메라를 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }
    private void checkPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Log.d("ImgActivity", "Opening camera...");

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_PERMISSION_REQUEST_CODE);
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

        // 기존 코드에서 수정된 부분
        String path = MediaStore.Images.Media.insertImage(imgActivity.getContentResolver(), inImage, "Title", null);

        if (path != null) {
            return Uri.parse(path);
        } else {
            // handle null case, show an error toast or log the error
            Log.e("ImgActivity", "Failed to insert image. Path is null.");
            // You may want to show a Toast or perform other error handling here
            return null;
        }
    }

    /**
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null) {
                Uri selectedImageUri = data.getData();
                Log.d("ImgActivity", "Image picked from gallery...");
                imageList.add(selectedImageUri);

                // 기본값으로 null, ""를 사용했으니 필요에 따라 수정
                Date selectedDate = null;
                String description = "";

                // 이미지 정보 생성 및 추가
                addImageDetails(selectedImageUri.toString(), selectedDate, description);

                // 추가: 액티비티가 종료될 때 이미지 정보 저장
                saveImageDetails();

                // 어댑터 갱신
                imageAdapter.notifyDataSetChanged();
            } else if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && data != null) {
                Bundle extras = data.getExtras();
                Log.d("ImgActivity", "Image captured from camera...");
                if (extras != null) {
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    Uri imageUri = getImageUri(this, imageBitmap);
                    imageList.add(imageUri);

                    // 기본값으로 null, ""를 사용했으니 필요에 따라 수정
                    Date selectedDate = null;
                    String description = "";

                    // 이미지 정보 생성 및 추가
                    addImageDetails(imageUri.toString(), selectedDate, description);

                    // 추가: 액티비티가 종료될 때 이미지 정보 저장
                    saveImageDetails();

                    // 어댑터 갱신
                    imageAdapter.notifyDataSetChanged();
                }
            }
        }
    }
    **/
    // 갤러리에서 이미지를 선택한 후 호출되는 메서드



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null) {
                Uri selectedImageUri = data.getData();
                imageList.add(selectedImageUri);

                // 기본값으로 null, ""를 사용했으니 필요에 따라 수정
                Date selectedDate = null;
                String description = "";

                // 이미지 정보 생성 및 추가
                addImageDetails(selectedImageUri.toString(), selectedDate, description);

                // 추가: 액티비티가 종료될 때 이미지 정보 저장
                saveImageDetails();

                // 어댑터 갱신
                imageAdapter.notifyDataSetChanged();
            } else if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && data != null) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    Uri imageUri = getImageUri(this, imageBitmap);
                    imageList.add(imageUri);

                    // 기본값으로 null, ""를 사용했으니 필요에 따라 수정
                    Date selectedDate = null;
                    String description = "";

                    // 이미지 정보 생성 및 추가
                    addImageDetails(imageUri.toString(), selectedDate, description);

                    // 추가: 액티비티가 종료될 때 이미지 정보 저장
                    saveImageDetails();

                    // 어댑터 갱신
                    imageAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}