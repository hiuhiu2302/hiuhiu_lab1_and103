package com.example.lab1_and103;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class dangnhapsdt extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    EditText edtsdt ,edtotp;
    Button btnsdt ,btnotp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangnhapsdt);
        mAuth= FirebaseAuth.getInstance();
        edtsdt=findViewById(R.id.edtsdt);
        edtotp=findViewById(R.id.edtopt);
        btnsdt=findViewById(R.id.btn_sdt);
        btnotp=findViewById(R.id.btn_opt);
        mCallbacks= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                edtotp.setText(phoneAuthCredential.getSmsCode());
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(dangnhapsdt.this, "Đã vượt quá số lượt gửi mã. vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId=verificationId;
            }
        };

        btnsdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = edtsdt.getText().toString().trim();
                if (phone.isEmpty()) {
                    Toast.makeText(dangnhapsdt.this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
                } else if (!phone.matches("[0-9]+")) {
                    Toast.makeText(dangnhapsdt.this, "Số điện thoại chỉ được chứa các kí tự số", Toast.LENGTH_SHORT).show();
                } else {
                    getOTP(phone);
                    Toast.makeText(dangnhapsdt.this, "Đã nhận đc số điện thoại", Toast.LENGTH_SHORT).show();

                }
            }
        });


        btnotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userOTP = edtotp.getText().toString();

                if (userOTP.isEmpty()) {
                    Toast.makeText(dangnhapsdt.this, "Vui lòng nhập OTP!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Gọi hàm verifyOTP để xác nhận OTP
                verifyOtp(userOTP);
            }
        });


    }

    private void getOTP (String phoneNumber ){
        PhoneAuthOptions options=
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84"+phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
    private void verifyOtp (String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(dangnhapsdt.this, "Đăng Nhập Thành Công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(dangnhapsdt.this, MainActivity.class);
                            startActivity(intent);

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            edtotp.setError("OTP Không Đúng");
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
}