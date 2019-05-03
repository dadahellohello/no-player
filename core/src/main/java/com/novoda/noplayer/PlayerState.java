package com.novoda.noplayer;

public interface PlayerState {

    /**
     * Returns {@code true} after {@link NoPlayer#play()} is called.
     *
     * @return true if player is playing or buffering content.
     */
    boolean isPlaying();

    /**
     * Returns {@code true} when player is prepared to start playing and adverts are loaded
     * (see {@link PlayerBuilder#withAdverts(AdvertsLoader)}.
     *
     * @return true when player is prepared to play adverts.
     */
    boolean isSetToPlayAdvert();

    /**
     * Returns {@code true} when player is prepared to start playing content.
     *
     * @return true when player is prepared to play content.
     */
    boolean isSetToPlayContent();

    /**
     * Width of the video.
     * <p>
     * To be notified when the size changes, use {@link NoPlayer.VideoSizeChangedListener}.
     *
     * @return video width in pixels.
     */
    int videoWidth();

    /**
     * Height of the video.
     * <p>
     * To be notified when the size changes, use {@link NoPlayer.VideoSizeChangedListener}.
     *
     * @return video height in pixels.
     */
    int videoHeight();

    /**
     * Position in an advert break that is being played in milliseconds.
     * <p>
     * If the advert break contains multiple adverts then the durations of played adverts will be accumulated
     * with the progress of the advert that is being played.
     *
     * @return position in advert break or 0 if the player is not playing an advert.
     */
    long positionInAdvertBreakInMillis();

    /**
     * Duration of an advert break that is being played in milliseconds.
     * <p>
     * Consists of accumulated lengths of all adverts in the advert break.
     *
     * @return duration of advert break or 0 if the player is not playing an advert.
     */
    long advertBreakDurationInMillis();

    /**
     * Position in the video that is being played in milliseconds.
     * <p>
     * If the player is playing an advert then this value will be a position inside a single advert.
     * To get the position in the advert break use {@link #positionInAdvertBreakInMillis()}.
     *
     * @return position in media that is being played.
     */
    long playheadPositionInMillis();

    /**
     * Duration of the video that is being played in milliseconds.
     * <p>
     * If the player is playing an advert then this value will be a duration of a single advert.
     * To get the duration of the whole advert break use {@link #advertBreakDurationInMillis()}.
     *
     * @return duration of media that is being played.
     */
    long mediaDurationInMillis();

    /**
     * Buffered percentage of the video that is being played.
     * <p>
     * If the player is playing an advert break then this value will represent a buffer of each individual advert
     * in the advert break and not the whole advert break.
     *
     * @return percentage value between 0 and 100.
     */
    int bufferPercentage();
}
