package com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners;


import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;

import android.support.annotation.NonNull;

public interface YouTubePlayerInitListener {
    void onInitSuccess(@NonNull YouTubePlayer youTubePlayer);
}
