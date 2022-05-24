package com.example.musicplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.example.musicplay.databinding.ActivityMainBinding;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    boolean isTouch = false;
    private ActivityMainBinding binding;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.cry_for_love);
        binding.seekBar.setVisibility(ProgressBar.VISIBLE);
        binding.seekBar.setMax(mediaPlayer.getDuration());



        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
                int m = progress / 60000;
                int s = (progress % 60000) / 1000;
                String strTime = String.format("%02d:%02d",m,s);
                binding.text.setText(strTime);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isTouch = false;
            }
        });

        binding.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                    try
                    {
                        mediaPlayer.prepare();
                    }
                    catch(IllegalStateException e)
                    {
                        e.printStackTrace();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                    binding.playButton.setBackgroundResource(R.drawable.ic_play);
                }
                else
                {
                    mediaPlayer.start();
                    binding.playButton.setBackgroundResource(R.drawable.ic_pause);

                    Thread();
                }
            }

            public void Thread(){
                Runnable task = new Runnable() {
                    @Override
                    public void run() {
                        while (mediaPlayer.isPlaying()){
                            try{
                                Thread.sleep(1000);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                            binding.seekBar.setProgress(mediaPlayer.getCurrentPosition());
                        }
                    }
                };
                Thread thread = new Thread(task);
                thread.start();
            }
        });
    }
}

