package kr.ac.skuniv.practive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserPreferences extends AppCompatActivity {

    private ProgressBar progressBar;
    private DatabaseReference progressDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_preferences);

        progressBar = findViewById(R.id.progressBar);
        progressDatabaseRef = FirebaseDatabase.getInstance().getReference().child("personalCloset").child("UserAccount");

        // ProgressBar의 저장 정도를 데이터베이스에 추가
        int progress = progressBar.getProgress();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        progressDatabaseRef.child(uid).child("progress").setValue(progress)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // 값 추가 성공시 MainActivity로 이동
                        Intent intent = new Intent(UserPreferences.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // 값 추가 실패
                        Toast.makeText(UserPreferences.this, "값 추가 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}