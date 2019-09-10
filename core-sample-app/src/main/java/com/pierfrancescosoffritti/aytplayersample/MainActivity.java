package com.pierfrancescosoffritti.aytplayersample;


import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.ui.PlayerUIController;
import com.pierfrancescosoffritti.androidyoutubeplayer.ui.menu.MenuItem;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import java.util.Random;

/**
 * This Activity is used as a starting point for all the sample Activities.
 * You won't find any code example for the library here. You can find those in the "examples"
 * package.
 */
public class MainActivity extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;

    private FullScreenHelper fullScreenHelper = new FullScreenHelper(this);

    private String[] videoIds = {"6JYIGclVQdw", "LvetJ9U_tVY"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        initYouTubePlayerView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfiguration) {
        super.onConfigurationChanged(newConfiguration);
        youTubePlayerView.getPlayerUIController().getMenu().dismiss();
    }

    @Override
    public void onBackPressed() {
        if (youTubePlayerView.isFullScreen()) {
            youTubePlayerView.exitFullScreen();
        } else {
            super.onBackPressed();
        }
    }

    private void initYouTubePlayerView() {
        initPlayerMenu();

        // The player will automatically release itself when the activity is destroyed.
        // The player will automatically pause when the activity is stopped
        // If you don't add YouTubePlayerView as a lifecycle observer, you will have to release it manually.
//        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.initialize(youTubePlayer -> {

            youTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady() {
                    loadVideo(youTubePlayer, videoIds[0]);
                }
            });

            addFullScreenListenerToPlayer(youTubePlayer);
            setPlayNextVideoButtonClickListener(youTubePlayer);

        }, true);
        youTubePlayerView.getPlayerUIController().setSyncButtonClickListener(v -> {
            Toast.makeText(this, "sync", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Shows the menu button in the player and adds an item to it.
     */
    private void initPlayerMenu() {
        youTubePlayerView.getPlayerUIController().showMenuButton(true);
        youTubePlayerView.getPlayerUIController().getMenu().addItem(
                new MenuItem("example", R.drawable.ic_settings_24dp, (view) -> Toast
                        .makeText(this, "item clicked", Toast.LENGTH_SHORT).show())
        );
    }

    private void loadVideo(YouTubePlayer youTubePlayer, String videoId) {
        if (getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
            youTubePlayer.loadVideo(videoId, 0);
        } else {
            youTubePlayer.cueVideo(videoId, 0);
        }

        setVideoTitle(youTubePlayerView.getPlayerUIController(), videoId);
    }

    private void addFullScreenListenerToPlayer(final YouTubePlayer youTubePlayer) {
        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                fullScreenHelper.enterFullScreen();

                addCustomActionToPlayer(youTubePlayer);
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                fullScreenHelper.exitFullScreen();

                removeCustomActionFromPlayer();
            }
        });
    }

    /**
     * This method adds a new custom action to the player.
     * Custom actions are shown next to the Play/Pause button in the middle of the player.
     */
    private void addCustomActionToPlayer(YouTubePlayer youTubePlayer) {
        Drawable customActionIcon = ContextCompat.getDrawable(this, R.drawable.ic_pause_36dp);

        youTubePlayerView.getPlayerUIController().setCustomAction1(customActionIcon, view -> {
            if (youTubePlayer != null) {
                youTubePlayer.pause();
            }
        });
    }

    private void removeCustomActionFromPlayer() {
        youTubePlayerView.getPlayerUIController().showCustomAction1(false);
    }

    private void setPlayNextVideoButtonClickListener(final YouTubePlayer youTubePlayer) {
        Button playNextVideoButton = findViewById(R.id.next_video_button);

        playNextVideoButton.setOnClickListener(view -> {
            String videoId = videoIds[new Random().nextInt(videoIds.length)];
            loadVideo(youTubePlayer, videoId);
        });
    }

    @SuppressLint("CheckResult")
    private void setVideoTitle(PlayerUIController playerUIController, String videoId) {
        playerUIController.setVideoTitle("title title");
    }
}
