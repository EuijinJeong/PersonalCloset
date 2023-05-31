package kr.ac.skuniv.practive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
* 로그인 화면 액티비티를 구성하는 자바 클래스
*
* <구현된 기능>
* 파이어베이스 연동을 통한 로그인 기능
* 자동 로그인 기능
*/

public class SignIn extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth; // 파이어베이스 인증처리 (객체명/변수명)
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스
    private EditText emailEt, pwdEt; // 회원가입 입력필드
    private Button login_button;
    private CheckBox autologin;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("personalCloset");

        emailEt = findViewById(R.id.EmailEt);
        pwdEt = findViewById(R.id.PwdEt);
        autologin = findViewById(R.id.checkbox_autologin);
        login_button = findViewById(R.id.login_button);
        autologin = findViewById(R.id.checkbox_autologin);

        // 자동로그인을 처리하기 위한 객체 생성
        pref = getSharedPreferences("loginInfo", MODE_PRIVATE);

        // auto_login이라는 키값에 해당하는 boolean 값을 반환합니다. 만약 해당 키값에 저장된 값이 없다면, 두 번째 인자로 전달된 false를 반환합니다.
        boolean isAutoLogin = pref.getBoolean("auto_login", false);
        autologin.setChecked(isAutoLogin);

        // 자동 로그인 체크박스 상태가 변경될 때마다 SharedPreferences에 저장
        autologin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("auto_login", isChecked);
                editor.apply();
            }
        });

        // 자동 로그인 체크박스가 체크되어 있으면, 저장된 이메일과 비밀번호 정보를 가져와서 Firebase 인증 기능을 사용하여 자동 로그인 수행
        if (autologin.isChecked()) {
            String email = pref.getString("email", "");
            String password = pref.getString("password", "");

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                mFirebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // 로그인 성공 시, 날씨추천 화면으로 이동
                                    Intent intent = new Intent(SignIn.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } else {
                                    // 로그인 실패 시, 에러 메시지 출력
                                    Toast.makeText(SignIn.this, "로그인 실패: " + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }

        // 로그인 버튼을 눌렀을 때 작동하는 코드
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 로그인 요청
                String strEmail = emailEt.getText().toString();
                String strPwd = pwdEt.getText().toString();

                mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // 자동 로그인 체크박스가 체크되어 있으면, 로그인 정보를 SharedPreferences에 저장
                            if (autologin.isChecked()) {
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("email", strEmail);
                                editor.putString("password", strPwd);
                                editor.apply();
                            }
                            // 로그인 성공
                            Intent intent = new Intent(SignIn.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // 현재 액티비티 파괴
                        }
                        else {
                            Toast.makeText(SignIn.this,"로그인 실패",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // 회원가입 버튼을 눌렀을 때 작동하는 코드
        Button signup_button = findViewById(R.id.signup_button);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 버튼 클릭 시, 회원가입 화면으로 이동함.
                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
            }
        });
    }
}