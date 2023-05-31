package kr.ac.skuniv.practive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserInfo extends AppCompatActivity {

    // 객체 선언부
    private CheckBox cb_man, cb_woman;
    private Button btn1;
    private DatabaseReference userDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        // Firebase Realtime Database 루트 레퍼런스 가져오기
        userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("personalCloset").child("UserAccount");

        cb_man = findViewById(R.id.cb_man);
        cb_woman = findViewById(R.id.cb_woman);
        btn1 = findViewById(R.id.nextBtn1);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb_man.isChecked() || cb_woman.isChecked()) {
                    String gender = cb_man.isChecked() ? "남성" : "여성";

                    // 현재 사용자의 고유한 식별자(Uid)를 가져와 해당 사용자의 "gender" 속성에 값을 추가
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    userDatabaseRef.child(uid).child("gender").setValue(gender)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // 값 추가 성공 시, UserAge 액티비티로 이동
                                    Intent intent = new Intent(UserInfo.this, UserAge.class);
                                    startActivity(intent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UserInfo.this, "값 추가 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(UserInfo.this, "체크박스를 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}