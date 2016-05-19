package com.zzc.androidtrain.tts;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.zzc.androidtrain.R;
import com.zzc.androidtrain.app.BaseActivity;

import java.util.Locale;

public class TTSActivity extends BaseActivity implements TextToSpeech.OnInitListener{

    EditText etContent = null;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tts);
        etContent = (EditText)findViewById(R.id.et_voice_content);
        tts = new TextToSpeech(this, this);
    }

    public void onSayBtnClick(View view) {
        String content = etContent.getText().toString().trim();
        if(!TextUtils.isEmpty(content)) {
            tts.speak(content, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onInit(int status) {
        tts.setLanguage(Locale.ENGLISH);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tts.shutdown();
    }
}
