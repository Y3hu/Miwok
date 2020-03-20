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

public class NumbersActivity extends AppCompatActivity {

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

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("One", "Uno", R.drawable.number_one, R.raw.number_one));
        words.add(new Word("Two", "Dos", R.drawable.number_two, R.raw.number_two));
        words.add(new Word("Three", "Tres", R.drawable.number_three, R.raw.number_three));
        words.add(new Word("Four", "Cuatro", R.drawable.number_four, R.raw.number_four));
        words.add(new Word("Five", "Cinco", R.drawable.number_five, R.raw.number_five));
        words.add(new Word("Six", "Seis", R.drawable.number_six, R.raw.number_six));
        words.add(new Word("Seven", "Siete", R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word("Eight", "Ocho", R.drawable.number_eight, R.raw.number_eight));
        words.add(new Word("Nine", "Nueve", R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word("Ten", "Diez", R.drawable.number_ten, R.raw.number_ten));

        WordAdapter adapter = new WordAdapter(this, words, R.color.category_numbers);

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
                    mediaPlayer = MediaPlayer.create(NumbersActivity.this, words.get(position).getmAudioResourceId());
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
