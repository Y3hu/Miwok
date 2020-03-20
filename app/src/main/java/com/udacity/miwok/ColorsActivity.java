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

public class ColorsActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
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

        //Create and setup the {@link AudioManager} to request audio focus
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("Rojo", "Red", R.drawable.color_red, R.raw.color_red));
        words.add(new Word("Verde", "Green", R.drawable.color_green, R.raw.color_green));
        words.add(new Word("Café", "Brown", R.drawable.color_brown, R.raw.color_brown));
        words.add(new Word("Gris", "Gris", R.drawable.color_gray, R.raw.color_gray));
        words.add(new Word("Negro", "Black", R.drawable.color_black, R.raw.color_black));
        words.add(new Word("Blanco", "White", R.drawable.color_white, R.raw.color_white));
        words.add(new Word("Amarillo", "Yellow", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        words.add(new Word("Mostaza", "Mustard Yellow", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));

        WordAdapter adapter = new WordAdapter(this, words, R.color.category_colors);

        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMedia();

                //Request audio focus for playback

                int result = audioManager.requestAudioFocus(onAudioFocusChangeListener,
                        // Use the music stream
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // We have audio focus now
                    mediaPlayer = MediaPlayer.create(ColorsActivity.this, words.get(position).getmAudioResourceId());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMedia();
    }

    private void releaseMedia(){
        if(mediaPlayer != null) mediaPlayer.release();
        audioManager.abandonAudioFocus(onAudioFocusChangeListener);
    }
}
