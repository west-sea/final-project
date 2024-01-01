package com.example.myapplication.Activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.R;

import java.util.ArrayList;

public class LinkNumActivity extends AppCompatActivity {

    private static final int READ_CONTACTS_PERMISSION_REQUEST = 1;

    private ListView listView;
    private ArrayList<String> contactList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num);
        listView = findViewById(R.id.list_items);


        /**
        Button mainButton = findViewById(R.id.mainbut);
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        **/

        Button addContactButton = findViewById(R.id.addContactButton);
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openContactDialog();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                openEditDialog(selectedItem, position);
            }
        });


        // READ_CONTACTS 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없을 경우 권한 요청
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE},
                    READ_CONTACTS_PERMISSION_REQUEST);
        } else {
            // 권한이 이미 허용된 경우 연락처 가져오기
            getContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        contactList = new ArrayList<>();

        if (requestCode == READ_CONTACTS_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 허용된 경우 연락처 가져오기
                getContacts();
            } else {
                // Permission denied, you may want to show a message to the user
            }
        }
    }

    private static final int ADD_CONTACT_REQUEST_CODE = 123;

    private void openContactDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        builder.setView(dialogView);

        AlertDialog contactDialog = builder.create();

        EditText nameEditText = dialogView.findViewById(R.id.nameEditText);
        EditText numberEditText = dialogView.findViewById(R.id.numberEditText);
        Button saveButton = dialogView.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                String number = numberEditText.getText().toString().trim();

                if (!name.isEmpty() && !number.isEmpty()) {
                    String newContact = name + ": " + number;
                    contactList.add(newContact);

                    ((MyAdapter) listView.getAdapter()).notifyDataSetChanged();

                    contactDialog.dismiss();
                } else {
                    Toast.makeText(LinkNumActivity.this, "이름과 전화번호를 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        contactDialog.show();
    }

    private void openEditDialog(String contactInfo, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.edit_dialog_layout, null);
        builder.setView(dialogView);

        AlertDialog editDialog = builder.create();

        EditText editNameEditText = dialogView.findViewById(R.id.editNameEditText);
        EditText editNumberEditText = dialogView.findViewById(R.id.editNumberEditText);
        Button updateButton = dialogView.findViewById(R.id.updateButton);

        // 기존 데이터 표시
        String[] parts = contactInfo.split(":");
        String name = parts[0].trim();
        String number = parts[1].trim();

        editNameEditText.setText(name);
        editNumberEditText.setText(number);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = editNameEditText.getText().toString().trim();
                String newNumber = editNumberEditText.getText().toString().trim();

                if (!newName.isEmpty() && !newNumber.isEmpty()) {
                    String editedContact = newName + ": " + newNumber;
                    contactList.set(position, editedContact);

                    ((MyAdapter) listView.getAdapter()).notifyDataSetChanged();

                    editDialog.dismiss();
                } else {
                    Toast.makeText(LinkNumActivity.this, "이름과 전화번호를 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        editDialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_CONTACT_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.hasExtra("addedContact")) {
                    // 추가 모드일 경우
                    String addedContact = data.getStringExtra("addedContact");
                    contactList.add(addedContact);
                    Log.d("LinkNumActivity", "Added contact: " + addedContact);

                } else if (data.hasExtra("editedPosition")) {
                    // 수정 모드일 경우
                    int editedPosition = data.getIntExtra("editedPosition", -1);
                    String editedContact = data.getStringExtra("editedContact");

                    if (editedPosition != -1) {
                        Log.d("LinkNumActivity", "Edited position: " + editedPosition);
                        Log.d("LinkNumActivity", "Edited contact: " + editedContact);
                        // 기존 위치의 데이터를 수정
                        contactList.set(editedPosition, editedContact);
                    }
                }

                ((MyAdapter) listView.getAdapter()).notifyDataSetChanged();
            }
        }
    }

    private void getContacts() {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

        if (cursor != null) {
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            while (cursor.moveToNext()) {
                String name = cursor.getString(nameIndex);
                String number = cursor.getString(numberIndex);
                contactList.add(name + ": " + number);
            }
            cursor.close();
        }

        // 연락처를 리스트뷰에 표시
        MyAdapter adapter = new MyAdapter();
        listView.setAdapter(adapter);
    }

    private void dialPhoneNumber(String selectedItem) {
        // 전화 번호 추출
        String[] parts = selectedItem.split(":");
        String phoneNumber = parts[1].trim();

        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(dialIntent);
    }

    private void sendSMS(String selectedItem, String message)
    {
        String[] parts = selectedItem.split(":");
        String phoneNumber = parts[1].trim();

        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        smsIntent.setData(Uri.parse("smsto:" + phoneNumber));

        smsIntent.putExtra("sms_body", "안녕하세요!");

        startActivity(smsIntent);
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            // 연락처 리스트 크기 반환
            return contactList.size();
        }

        @Override
        public Object getItem(int position) {
            // 해당 위치의 아이템 반환
            return contactList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // 아이템의 ID 반환
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(LinkNumActivity.this);
                view = inflater.inflate(R.layout.list_item, parent, false);
            }

            TextView textView = view.findViewById(R.id.textView);
            Button button1 = view.findViewById(R.id.button1);
            Button button2 = view.findViewById(R.id.button2);

            String contactInfo = getItem(position).toString();
            textView.setText(contactInfo);

            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 버튼1 클릭 시 수행할 동작
                    Toast.makeText(LinkNumActivity.this, "Button 1 Clicked: " + contactInfo, Toast.LENGTH_SHORT).show();
                    dialPhoneNumber(contactInfo);
                }
            });

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 버튼2 클릭 시 수행할 동작
                    Toast.makeText(LinkNumActivity.this, "Button 2 Clicked: " + contactInfo, Toast.LENGTH_SHORT).show();
                    sendSMS(contactInfo, "문자 전송");
                }
            });

            return view;
        }
    }
}