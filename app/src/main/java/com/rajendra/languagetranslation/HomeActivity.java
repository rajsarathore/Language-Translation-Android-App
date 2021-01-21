package com.rajendra.languagetranslation;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    ImageButton mikeButton;
    final int REQ_CODE_SPEECH_INPUT=99;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mikeButton=(ImageButton)findViewById(R.id.MikeButton);
        mikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecognisation();


            }
        });
    }
    public void startRecognisation(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        try {

            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(this,"Device does not supporting speech recognition",
                    Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Log.e("error",e.getMessage()+"");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQ_CODE_SPEECH_INPUT:{
                if (resultCode == RESULT_OK && null != data) {


                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    translateText(result);


                }


            }

            break;
        }
    }

    private void translateText(ArrayList<String> result) {
        String text=result.get(0);
        String requestURL =
                "https://translation.googleapis.com/language/translate/v2";
        // This List in Kotlin language
        List<kotlin.Pair<String,String>> params = new ArrayList<>();
// Add API key
        params.add(new kotlin.Pair<String, String>("key",
                getResources().getString(R.string.mykey)));

// Set source and target languages
        params.add(new kotlin.Pair<String, String>("source", "en"));
        params.add(new kotlin.Pair<String, String>("target", "hi"));
        params.add(new kotlin.Pair<String, String>("q", text));
        Fuel.get(requestURL, params).responseString(new Handler<String>() {
            @Override
            public void success(@NotNull Request request,
                                @NotNull Response response,
                                String data) {
                // Access the translations array
                JSONArray translations = null;
                try {
                    translations = new JSONObject(data)
                            .getJSONObject("data")
                            .getJSONArray("translations");


                // Loop through the array and extract the translatedText
        // key for each item
                String result = "";
                for(int i=0;i<translations.length();i++) {
                    result += translations.getJSONObject(i)
                            .getString("translatedText") + "\n";
                }
                    Intent intent = new Intent(HomeActivity.this, ShowTextResult.class);
                    intent.putExtra("result", result);
                    startActivity(intent);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(@NotNull Request request,
                                @NotNull Response response,
                                @NotNull FuelError fuelError) { }
        });




    }
}
