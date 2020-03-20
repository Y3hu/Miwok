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

public class FamilyActivity extends AppCompatActivity {
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
        words.add(new Word("Padre", "Father", R.drawable.family_father, R.raw.family_father));
        words.add(new Word("Madre", "Mother", R.drawable.family_mother, R.raw.family_mother));
        words.add(new Word("Hijo", "Son", R.drawable.family_son, R.raw.family_son));
        words.add(new Word("Hija", "Daughter", R.drawable.family_daughter, R.raw.family_daughter));
        words.add(new Word("Hermano Mayor", "Older Brother", R.drawable.family_older_brother, R.raw.family_older_brother));
        words.add(new Word("Hermano Menor", "Younger Brother", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        words.add(new Word("Hermana Mayor", "Older Sister", R.drawable.family_older_sister, R.raw.family_older_sister));
        words.add(new Word("Hermana Menor", "Younger Sister", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        words.add(new Word("Abuela", "Grandmother", R.drawable.family_grandmother, R.raw.family_grandmother));
        words.add(new Word("Abuelo", "Grandfather", R.drawable.family_grandfather, R.raw.family_grandfather));

        WordAdapter adapter = new WordAdapter(this, words, R.color.category_family);

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
                    mediaPlayer = MediaPlayer.create(FamilyActivity.this, words.get(position).getmAudioResourceId());
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
