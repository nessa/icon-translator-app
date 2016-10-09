package com.icon.nsales.icontranslate.services;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Text to speech service class.
 * Author: Noelia Sales Montes, noelia.salesmontes <at> gmail.com
 *
 * Needed to create a new TTS service and make the TTS translate to voice.
 */
public class TextToSpeechService {

    private TextToSpeech mTTS;
    private TextToSpeech checkTTS;
    private Context context;

    private boolean ready;

    /**
     * Constructor.
     * @param context Needed to create the TTS
     */
    public TextToSpeechService(Context context) {
        this.context = context;
        this.ready = false;
    }

    /**
     * Create new TTS or update its locale if it's created.
     * @param language Language code to set as TTS locale
     */
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

    /**
     * Shutdown the TTS if it's not undefined.
     */
    public void stop() {
        if (mTTS != null) {
            mTTS.shutdown();
            mTTS = null;
            ready = false;
        }
    }

    /**
     * Return a boolean that indicates if the TTS is ready to speak.
     * @return Boolean
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * Translate the given text to voice use TTS methods.
     * @param text Text to translate
     */
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

    /**
     * Check if user needs to download language packages. If so, call an intent to show the
     * download view. Otherwise, show a snack bar message.
     * @param language Language code
     * @param country Country code
     * @param layout Layout in which show the snack bar
     * @param message Message to show in the snack bar
     */
    public void downloadLanguagePackages(final String language, final String country,
                                         final CoordinatorLayout layout, final String message) {

        checkTTS = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    Locale loc = new Locale(language, country);

                    if (checkTTS.isLanguageAvailable(loc) == TextToSpeech.LANG_AVAILABLE) {
                        if (layout != null && message != null) {
                            Snackbar.make(layout, message, Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    } else {
                        // Launch an intent to load languages packages download view
                        Intent installTTSIntent = new Intent();
                        installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                        ArrayList<String> languages = new ArrayList<>();
                        languages.add(language);
                        installTTSIntent.putStringArrayListExtra(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA,
                                languages);
                        installTTSIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(installTTSIntent);
                    }
                }
            }
        });
    }
}
