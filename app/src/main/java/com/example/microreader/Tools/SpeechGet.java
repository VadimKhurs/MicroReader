package com.example.microreader.Tools;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;

import androidx.core.app.ActivityCompat;

import com.example.microreader.MainActivity;

public class SpeechGet {
    public boolean isRecording = false;
    AudioManager am = null;
    AudioRecord record = null;
    AudioTrack track = null;

    public SpeechGet(MainActivity context) {
        initRecordAndTrack(context);

        am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        am.setSpeakerphoneOn(true);

        (new Thread() {
            @Override
            public void run() {
                recordAndPlay();
            }
        }).start();

    }

    private void initRecordAndTrack(MainActivity context) {
        int min = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);

        if (ActivityCompat.checkSelfPermission( context, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        record = new AudioRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION, 8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,
                min);
        if (AcousticEchoCanceler.isAvailable())
        {
            AcousticEchoCanceler echoCancler = AcousticEchoCanceler.create(record.getAudioSessionId());
            echoCancler.setEnabled(true);
        }
        int maxJitter = AudioTrack.getMinBufferSize(8000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        track = new AudioTrack(AudioManager.MODE_IN_COMMUNICATION, 8000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, maxJitter,
                AudioTrack.MODE_STREAM);
    }

    public void recordAndPlay() {
        short[] lin = new short[1024];
        int num = 0;
        am.setMode(AudioManager.MODE_IN_COMMUNICATION);
        while (true) {
            if (isRecording){
                num = record.read(lin, 0, 1024);
                track.write(lin, 0, num);
            }
        }
    }

    public void startRecordAndPlay() {
        record.startRecording();
        track.play();
        isRecording = true;
    }

    public void stopRecordAndPlay() {
        record.stop();
        track.pause();
        isRecording = false;
    }
}
