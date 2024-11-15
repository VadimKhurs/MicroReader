package com.example.microreader.Tools;

import android.speech.tts.TextToSpeech;

import com.example.microreader.MainActivity;

import java.util.Locale;

public class TextGen{
    public TextToSpeech tts;

    public TextGen(MainActivity context){
        tts = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                // Set language to Russian
                int result = tts.setLanguage(new Locale("ru", "RU"));

                //Increase speed of reading
                tts.setSpeechRate(2);

                //Add error window when Russian language is not found
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    /**
                    Toast.makeText(this, "Russian language not supported on this device", Toast.LENGTH_SHORT).show();
                     **/
                } else {
                    // Ready to use TTS
//                    speak("Привет, мир!");
                }

            } else {
                // Add error window

                //Toast.makeText(this, "TTS initialization failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void speak(String text) {
        if (tts != null) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    public void destroy(){
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}
