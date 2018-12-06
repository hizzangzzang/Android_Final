package com.example.hwanghuijeong.mytastyroad;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;

    // 이메일과 비밀번호
    private EditText registEmail;
    private EditText registPassword, registphone, registname;
    private Button registbutton;

    private String remail = "";
    private String rpassword = "";

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        registEmail = findViewById(R.id.r_email);
        registPassword = findViewById(R.id.r_password);
        registphone = findViewById(R.id.r_phone);
        registname = findViewById(R.id.r_name);
        registbutton = findViewById(R.id.r_Button);

        registbutton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                singUp();
                saveUserInformation();
            }
        });
    }

    private void saveUserInformation(){

        String name = registname.getText().toString().trim();
        String phone = registphone.getText().toString().trim();

        UserInformation userInformation = new UserInformation(name,phone);
        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference.child(user.getUid()).setValue(userInformation);

    }



    public void singUp() {
        remail = registEmail.getText().toString();
        rpassword = registPassword.getText().toString();

        if (TextUtils.isEmpty(remail) || TextUtils.isEmpty((rpassword))) {
            Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_SHORT).show();

        }else {
            firebaseAuth.createUserWithEmailAndPassword(remail, rpassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // 회원가입 성공
                                Toast.makeText(RegisterActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(i);
                            }
                        }
                    });
        }
    }



    // 이메일 유효성 검사
    private boolean isValidEmail() {
        if (remail.isEmpty()) {
            // 이메일 공백
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(remail).matches()) {
            // 이메일 형식 불일치
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd() {
        if (rpassword.isEmpty()) {
            // 비밀번호 공백
            return false;
        } else if (!PASSWORD_PATTERN.matcher(rpassword).matches()) {
            // 비밀번호 형식 불일치
            return false;
        } else {
            return true;
        }
    }

}
