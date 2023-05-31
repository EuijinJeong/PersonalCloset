package kr.ac.skuniv.practive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import kr.ac.skuniv.practive.UserAccount;

/**
* 이 클래스는 회원가입 액티비티를 구성하는 자바 클래스입니다.
* SignIn 클래스에서 정보를 받아 사용자의 회원가입 처리를 도와줍니다.
* 저장된 회원 정보는 파이어베이스 데이터베이스에 자동적으로 저장됩니다.
 * */

public class SignUp extends AppCompatActivity {

    // 객체 선언부
    private FirebaseAuth mFirebaseAuth; // 파이어베이스 인증처리 (객체명/변수명)
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스
    private EditText nameEt, emailEt, pwdEt; // 회원가입 입력필드
    private Button joinBtn; // 회원가입 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //xml 아이디 버튼
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("personalCloset");
        nameEt = findViewById(R.id.NameEt);
        emailEt = findViewById(R.id.EmailEt);
        pwdEt = findViewById(R.id.PwdEt);
        joinBtn = findViewById(R.id.joinBtn);

        // 회원가입 버튼 이벤트 처리 함수
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 처리 시작
                String strName = nameEt.getText().toString();
                String strEmail = emailEt.getText().toString();
                String strPwd = pwdEt.getText().toString();

                // 회원가입 유효성 검사
                if (TextUtils.isEmpty(strEmail)) { // 이메일 주소가 비어있는 경우
                    Toast.makeText(SignUp.this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(strPwd)) { // 비밀번호가 비어있는 경우
                    Toast.makeText(SignUp.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (strPwd.length() < 6) { // 비밀번호가 최소 길이보다 짧은 경우
                    Toast.makeText(SignUp.this, "비밀번호는 6자리 이상으로 설정해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Firebase Auth 진행
                mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){ // 로그인 성공했을 경우
                            // 회원가입 된 유저를 가지고 옴
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                            UserAccount account = new UserAccount();
                            account.setIdToken(firebaseUser.getUid());
                            account.setEmailId(firebaseUser.getEmail());
                            account.setPassword(strPwd);

                            // setValue: database에 insert
                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                            Toast.makeText(SignUp.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                            // 로그인 화면으로 이동
                            Intent intent = new Intent(SignUp.this, SignIn.class);
                            startActivity(intent);
                            finish(); // 현재 액티비티 종료
                        }
                        else{
                            String errorMsg = task.getException().getMessage();
                            Toast.makeText(SignUp.this, "회원가입에 실패하셨습니다. (" + errorMsg + ")", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}