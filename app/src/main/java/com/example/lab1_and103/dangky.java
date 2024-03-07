package com.example.lab1_and103;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class dangky extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            // neu user da dang nhap vao tu phien truoc thi su dung user luon
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangky);

        EditText edtuser, edtpass;
        Button btndk;

        edtuser = findViewById(R.id.edtuse_dn);
        edtpass = findViewById(R.id.edtmk_dn);
        btndk = findViewById(R.id.btn_dn);

        btndk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = edtuser.getText().toString().trim();
                String password = edtpass.getText().toString().trim();

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(dangky.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Đăng ký thành công, cập nhật giao diện người dùng với thông tin người dùng đã đăng nhập
                                    Log.d("Main", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent = new Intent(dangky.this, dangnhap.class);
                                    startActivity(intent);
//

                                    Toast.makeText(getApplicationContext(), user.getEmail(), Toast.LENGTH_LONG).show();
                                    // Cập nhật giao diện người dùng (nếu cần)
                                } else {
                                    // Nếu đăng ký thất bại, hiển thị thông báo cho người dùng
                                    Log.w("Main", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(dangky.this, "Đăng ký không thành công.",
                                            Toast.LENGTH_SHORT).show();
                                    // Cập nhật giao diện người dùng (nếu cần)
                                }
                            }
                        });
            }
        });


    }
}