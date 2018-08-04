package com.rajendra.languagetranslation;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ShowTextResult extends AppCompatActivity {
    TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_text_result);
        textViewResult=(TextView)findViewById(R.id.textViewResult);

        textViewResult.setText(getIntent().getStringExtra("result"));
    }
}
