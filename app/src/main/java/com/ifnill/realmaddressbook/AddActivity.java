package com.ifnill.realmaddressbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ifnill.realmaddressbook.dto.User;

import io.realm.Realm;

public class AddActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText nameEdit, phoneEdit;
    private Button registerBtn;
    private Realm realm;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        realm = Realm.getDefaultInstance();

        nameEdit = (EditText) findViewById(R.id.name_edit);
        phoneEdit = (EditText) findViewById(R.id.phone_edit);
        registerBtn = (Button) findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(this);

        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("phone");
        if(phoneNumber != null){
            User user = realm.where(User.class).equalTo("phone",phoneNumber).findFirst();
            nameEdit.setText(user.getName());
            phoneEdit.setText(user.getPhone());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_btn:
                registerUser();
                finish();
                break;
        }
    }

    public void registerUser(){
        final String name = nameEdit.getText().toString();
        final String phone = phoneEdit.getText().toString();
        final User user = new User();
        user.setName(name);
        user.setPhone(phone);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User mUser = realm.where(User.class).equalTo("phone",phoneNumber).findFirst();
                if(mUser != null){
                    mUser.setName(name);
                    mUser.setPhone(phone);
                }else{
                    realm.copyToRealm(user);
                }
            }
        });
    }
}
