package com.icon.nsales.icontranslate.services;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by nessa on 8/10/16.
 */
public class TextToSpeechService {

    private TextToSpeech mTTS;
    private Context context;

    private boolean ready;

    public TextToSpeechService(Context context) {
        this.context = context;
        this.ready = false;
    }

    public void createOrUpdate(final String language) {
        if (mTTS == null) {
            mTTS = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        Locale loc = new Locale(language);
                        mTTS.setLanguage(loc);
                        ready = true;
                    }
                }
            });
        } else {
            Locale loc = new Locale(language);
            mTTS.setLanguage(loc);
            ready = true;
        }
    }

    public void stop() {
        if (mTTS != null) {
            mTTS.shutdown();
            mTTS = null;
            ready = false;
        }
    }

    public boolean isReady() {
        return ready;
    }

    @SuppressWarnings("deprecation")
    public void speak(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null, "phrase");
        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "phrase");
            mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, map);
        }
    }
}
