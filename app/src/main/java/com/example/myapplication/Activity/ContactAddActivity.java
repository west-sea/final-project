package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.util.ArrayList;

public class ContactAddActivity extends AppCompatActivity {

    private ArrayList<String> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_add);

        contactList = new ArrayList<>();

        EditText nameEditText = findViewById(R.id.nameEditText);
        EditText numberEditText = findViewById(R.id.numberEditText);
        Button addContactButton = findViewById(R.id.addContactButton);

        Intent intent = getIntent();
        if (intent.hasExtra("contactInfo")) {
            // 수정 모드로 진입한 경우 기존 정보 표시
            String contactInfo = intent.getStringExtra("contactInfo");
            String[] parts = contactInfo.split(":");
            String name = parts[0].trim();
            String number = parts[1].trim();

            nameEditText.setText(name);
            numberEditText.setText(number);
        }

        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String number = numberEditText.getText().toString();

                if (!name.isEmpty() && !number.isEmpty()) {
                    String newContact = name + ": " + number;

                    Intent resultIntent = new Intent();  // 새로운 Intent를 생성

                    if (getIntent().hasExtra("contactInfo")) {
                        // 수정 모드로 진입한 경우
                        int editedPosition = getIntent().getIntExtra("editedPosition", -1);

                        if (editedPosition != -1) {
                            Log.d("ContactAddActivity", "Edit mode - Edited position: " + editedPosition);
                            Log.d("ContactAddActivity", "Edit mode - New contact: " + newContact);
                            // 기존 위치의 데이터를 수정
                            resultIntent.putExtra("editedPosition", editedPosition);
                            resultIntent.putExtra("editedContact", newContact);
                        }
                    } else {
                        Log.d("ContactAddActivity", "Add mode - Added contact: " + newContact);
                        // 추가 모드로 진입한 경우
                        resultIntent.putExtra("addedContact", newContact);
                    }

                    setResult(RESULT_OK, resultIntent);

                    // 현재 액티비티 종료
                    finish();
                }
            }
        });
    }
}
