package com.udacity.miwok;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.udacity.miwok.adapter.WordAdapter;
import com.udacity.miwok.model.Word;

import java.util.ArrayList;

public class PhrasesActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    private AudioManager audioManager;

    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                //PAUSE PLAYBACK
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            }else if(focusChange == AudioManager.AUDIOFOCUS_GAIN){
                // RESUME PLAYBACK
                mediaPlayer.start();
            }else if(focusChange == AudioManager.AUDIOFOCUS_LOSS){
                // STOP PLAYBACK AND CLEAN UP RESOURCE
                releaseMedia();
            }
        }
    };

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMedia();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("¿Para dónde vas?", "Where are you going?", R.raw.phrase_where_are_you_going));
        words.add(new Word("¿Cuál es tu nombre?", "What is your name?", R.raw.phrase_what_is_your_name));
        words.add(new Word("Mi nombre es...", "My name is...", R.raw.phrase_my_name_is));
        words.add(new Word("¿Cómo te sientes?", "How are you feeling?", R.raw.phrase_how_are_you_feeling));
        words.add(new Word("Me siento bien.", "I’m feeling good.", R.raw.phrase_im_feeling_good));
        words.add(new Word("¿Vas a venir?", "Are you coming?", R.raw.phrase_are_you_coming));
        words.add(new Word("Si, si voy a ir.", "Yes, I’m coming.", R.raw.phrase_yes_im_coming));
        words.add(new Word("Voy a ir.", "I’m coming.", R.raw.phrase_im_coming));
        words.add(new Word("Vamos.", "Let’s go.", R.raw.phrase_lets_go));
        words.add(new Word("Vení.", "Come here.", R.raw.phrase_come_here));

        WordAdapter adapter = new WordAdapter(this, words, R.color.category_phrases);

        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMedia();

                int result = audioManager.requestAudioFocus(onAudioFocusChangeListener,
                        // Use the music stream
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mediaPlayer = MediaPlayer.create(PhrasesActivity.this, words.get(position).getmAudioResourceId());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });
    }

    private void releaseMedia(){
        if(mediaPlayer != null) mediaPlayer.release();
        audioManager.abandonAudioFocus(onAudioFocusChangeListener);
    }
}
