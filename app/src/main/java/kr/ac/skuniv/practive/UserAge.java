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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserAge extends AppCompatActivity {
    private EditText et_age;
    private Button btn2;
    private DatabaseReference userDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_age);

        // Firebase Realtime Database 루트 레퍼런스 가져오기
        userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("personalCloset").child("UserAccount");

        btn2 = findViewById(R.id.nextBtn2);
        et_age=findViewById(R.id.et_age);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String age = et_age.getText().toString().trim();
                if (!TextUtils.isEmpty(age)){
                    // 현재 사용자의 고유한 식별자(Uid)를 가져와 해당 사용자의 "age" 속성에 값을 추가
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    userDatabaseRef.child(uid).child("age").setValue(age)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // 값 추가 성공 시, UserPreferences 액티비티로 이동
                                    Intent intent = new Intent(UserAge.this, UserPreferences.class);
                                    startActivity(intent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UserAge.this, "값 추가 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(UserAge.this, "나이를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}