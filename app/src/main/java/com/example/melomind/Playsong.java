package com.example.melomind;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.io.File;
import java.util.ArrayList;
import android.content.Context;
import android.media.AudioManager;

public class Playsong extends AppCompatActivity {
    TextView textView, currentduration, totalduration;
    ImageView play, previous, next;
    ArrayList<File> song;
    MediaPlayer mediaPlayer;
    String textContent;
    int position;
    SeekBar seekBar;
    Thread updateSeek;
    Toolbar toolbar;
    AudioManager audioManager;



    public void Repeat(){
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                mediaPlayer.seekTo(0);
                mediaPlayer.start();

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        if (updateSeek != null && updateSeek.isAlive()) {
            updateSeek.interrupt();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playsong);
        ImageView node = findViewById(R.id.node);


        // Load and display the GIF with Glide
        Glide.with(this)
                .asGif()
                .load(R.raw.node)
                .into(node);
        textView = findViewById(R.id.pause);
        currentduration = findViewById(R.id.currentduration);
        totalduration = findViewById(R.id.totalduration);
        play = findViewById(R.id.play);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        seekBar = findViewById(R.id.seekBar);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       try {
           getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       }
       catch(Exception n){
           Log.d(null, "Null pointer exception ");
       }
       toolbar.setTitle("track");
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        song = (ArrayList) bundle.getParcelableArrayList("songList");
        textContent = intent.getStringExtra("currentsong");
        textView.setText(textContent);
        textView.setSelected(true);
        position = intent.getIntExtra("Position", 0);
        Uri uri = Uri.parse(song.get(position).toString());
        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                seekBar.setMax(mediaPlayer.getDuration());

            }
        });


            mediaPlayer.start();
            Repeat();







        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                currentduration.setText(formatDuration(mediaPlayer.getCurrentPosition()));
                int totalDurationMillis = mediaPlayer.getDuration();
                totalduration.setText(formatDuration(totalDurationMillis));


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                currentduration.setText(formatDuration(mediaPlayer.getCurrentPosition()));
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
            }
        });
        // Define a handler to post updates to the main (UI) thread
        Handler handler = new Handler(Looper.getMainLooper());


     updateSeek = new Thread() {
         public void run() {
             try {
                 while (mediaPlayer != null){
                     if(mediaPlayer.isPlaying()) {
                         final int currentPosition = mediaPlayer.getCurrentPosition();

                         // Post the update to the main thread using the handler
                         handler.post(new Runnable() {
                             @Override
                             public void run() {
                                 // Update the seek bar on the main thread
                                 seekBar.setProgress(currentPosition);
                                 currentduration.setText(formatDuration(mediaPlayer.getCurrentPosition()));
                             }
                         });
                     }

                     sleep(300);
                 }
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
     };


        updateSeek.start();

       // ... (your existing code)
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    play.setImageResource(R.drawable.play);

                    mediaPlayer.pause();


                } else {
                    play.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                }
            }
        });


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
                mediaPlayer.stop();
                mediaPlayer.release();
                if (position != 0) {
                    position = position - 1;
                } else {
                    position = song.size() - 1;
                }
                Uri uri = Uri.parse(song.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);


                play.setImageResource(R.drawable.pause);
                mediaPlayer.start();

                textContent = song.get(position).getName().toString();
                textView.setText(textContent);
                seekBar.setProgress(0);

                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        seekBar.setMax(mediaPlayer.getDuration());
                        mediaPlayer.seekTo(0);


                    }
                });
                Repeat();



            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
                mediaPlayer.stop();
                mediaPlayer.release();
                if (position != song.size() - 1) {
                    position = position + 1;
                } else {
                    position = 0;
                }
                Uri uri = Uri.parse(song.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);


                mediaPlayer.start();
                play.setImageResource(R.drawable.pause);

                textContent = song.get(position).getName().toString();
                textView.setText(textContent);
                seekBar.setProgress(0);

                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        seekBar.setMax(mediaPlayer.getDuration());
                        mediaPlayer.seekTo(0);


                    }
                });
                Repeat();
            }

        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.share):

               Uri fileuri = Uri.parse(song.get(position).toString());
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("audio/*");  // Specify the MIME type for audio files

// Set the URI of the music file to be shared
                shareIntent.putExtra(Intent.EXTRA_STREAM, fileuri);
                shareIntent.putExtra(Intent.EXTRA_TITLE,textContent);

//  add a message

                shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this awesome music!");

             // Start the activity with the intent
                startActivity(Intent.createChooser(shareIntent, textContent));

                default:
                return super.onOptionsItemSelected(item);
        }

    }
    private String formatDuration(int milliseconds) {
        int seconds = milliseconds / 1000;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }


}
