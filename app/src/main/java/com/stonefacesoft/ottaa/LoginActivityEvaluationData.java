package com.stonefacesoft.ottaa;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.stonefacesoft.ottaa.utils.RemoteConfigUtils;

public class LoginActivityEvaluationData extends AppCompatActivity {
    private RemoteConfigUtils remoteConfigUtils;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_information);
        TextView textView = findViewById(R.id.description);
        textView.setMovementMethod(new ScrollingMovementMethod());
        Button btn = findViewById(R.id.closeButton);
        remoteConfigUtils = RemoteConfigUtils.getInstance();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginActivityEvaluationData.this,LoginActivity2Step2.class);
                startActivity(intent);
                finishAfterTransition();
            }
        });
        remoteConfigUtils.setActivateDeactivateConfig(this, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    textView.setText(remoteConfigUtils.getStringByName("informativeDescription"));
                }
            }
        });
    }
}
