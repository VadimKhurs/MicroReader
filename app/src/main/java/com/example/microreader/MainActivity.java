package com.example.microreader;

import android.media.AudioManager;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.microreader.Tools.LlmConnection;
import com.example.microreader.Tools.SpeechGet;
import com.example.microreader.Tools.TTS;

public class MainActivity extends AppCompatActivity{
    public TTS tts;
    SpeechGet getSpeech;
    LlmConnection llm;

    TextView helloTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);
        setVolumeControlStream(AudioManager.MODE_IN_COMMUNICATION);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        helloTextView = (TextView) findViewById(R.id.textView);
        helloTextView.setText("l;akjlsjkaflkja");

        tts = new TTS(this);
        getSpeech = new SpeechGet(this);;
        llm = new LlmConnection();

        Button startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getSpeech.isRecording) {
                    getSpeech.startRecordAndPlay();
                }
            }
        });

        Button stopButton = (Button) findViewById(R.id.stop_button);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSpeech.isRecording) {
                    getSpeech.stopRecordAndPlay();
                }
            }
        });

        Button speakButton = (Button) findViewById(R.id.speak_button);
        speakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              tts.speak("Мы любим животных и стараемся поддерживать тех из них, кому не посчастливилось иметь ласковых хозяев и тёплый кров. Один из проверенных способов это сделать — помочь благотворительному фонду «Луч Добра». Благодаря их труду ежегодно сотни питомцев находят свой новый дом.");
                llm.togetherAi(MainActivity.this);

            }
        });

    }

    @Override
    protected void onDestroy() {
        tts.destroy();

        super.onDestroy();
    }

}