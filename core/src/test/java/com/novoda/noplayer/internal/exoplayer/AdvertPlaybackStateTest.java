package com.novoda.noplayer.internal.exoplayer;

import android.net.Uri;
import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.novoda.noplayer.Advert;
import com.novoda.noplayer.AdvertBreak;
import com.novoda.noplayer.AdvertBreakId;
import com.novoda.noplayer.AdvertId;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class AdvertPlaybackStateTest {

    private static final int ONE_SECOND_IN_MICROS = 1000000;
    private static final int TWO_SECONDS_IN_MICROS = 2000000;
    private static final int THREE_SECONDS_IN_MICROS = 3000000;

    private static final AdvertBreakId FIRST_ADVERT_BREAK_ID = new AdvertBreakId("advert_break_one");
    private static final AdvertBreakId SECOND_ADVERT_BREAK_ID = new AdvertBreakId("advert_break_two");
    private static final AdvertBreakId THIRD_ADVERT_BREAK_ID = new AdvertBreakId("advert_break_three");

    private static final AdvertId FIRST_ADVERT_ID = new AdvertId("advert_one");
    private static final AdvertId SECOND_ADVERT_ID = new AdvertId("advert_two");
    private static final AdvertId THIRD_ADVERT_ID = new AdvertId("advert_three");

    private static final int ONE_SECOND_IN_MILLIS = 1000;
    private static final int TWO_SECONDS_IN_MILLIS = 2000;
    private static final int THREE_SECONDS_IN_MILLIS = 3000;

    private static final Uri FIRST_URI = mock(Uri.class);
    private static final Uri SECOND_URI = mock(Uri.class);
    private static final Uri THIRD_URI = mock(Uri.class);

    private static final Advert FIRST_ADVERT = new Advert(FIRST_ADVERT_ID, ONE_SECOND_IN_MILLIS, FIRST_URI);
    private static final Advert SECOND_ADVERT = new Advert(SECOND_ADVERT_ID, TWO_SECONDS_IN_MILLIS, SECOND_URI);
    private static final Advert THIRD_ADVERT = new Advert(THIRD_ADVERT_ID, THREE_SECONDS_IN_MILLIS, THIRD_URI);

    private static final AdvertBreak FIRST_ADVERT_BREAK = new AdvertBreak(
            FIRST_ADVERT_BREAK_ID, ONE_SECOND_IN_MILLIS, Collections.singletonList(FIRST_ADVERT)
    );

    private static final AdvertBreak SECOND_ADVERT_BREAK = new AdvertBreak(
            SECOND_ADVERT_BREAK_ID, TWO_SECONDS_IN_MILLIS, Arrays.asList(FIRST_ADVERT, SECOND_ADVERT)
    );

    private static final AdvertBreak THIRD_ADVERT_BREAK = new AdvertBreak(
            THIRD_ADVERT_BREAK_ID, THREE_SECONDS_IN_MILLIS, Arrays.asList(FIRST_ADVERT, SECOND_ADVERT, THIRD_ADVERT)
    );

    @Test
    public void createsCorrectAdvertPlaybackState() {
        List<AdvertBreak> advertBreaks = Arrays.asList(THIRD_ADVERT_BREAK, SECOND_ADVERT_BREAK, FIRST_ADVERT_BREAK);

        AdvertPlaybackState advertPlaybackState = AdvertPlaybackState.from(advertBreaks);
        AdPlaybackState adPlaybackState = advertPlaybackState.adPlaybackState();

        assertThat(adPlaybackState.adGroupCount).isEqualTo(3);
        assertThat(adPlaybackState.adGroupTimesUs).containsSequence(ONE_SECOND_IN_MICROS, TWO_SECONDS_IN_MICROS, THREE_SECONDS_IN_MICROS);
        assertThatGroupContains(adPlaybackState.adGroups[0], 1, new long[]{ONE_SECOND_IN_MICROS}, new Uri[]{FIRST_URI});
        assertThatGroupContains(adPlaybackState.adGroups[1], 2, new long[]{ONE_SECOND_IN_MICROS, TWO_SECONDS_IN_MICROS}, new Uri[]{FIRST_URI, SECOND_URI});
        assertThatGroupContains(adPlaybackState.adGroups[2], 3, new long[]{ONE_SECOND_IN_MICROS, TWO_SECONDS_IN_MICROS, THREE_SECONDS_IN_MICROS}, new Uri[]{FIRST_URI, SECOND_URI, THIRD_URI});
    }

    @Test
    public void advertBreaksAreReorderedBasedOnStartTime() {
        List<AdvertBreak> advertBreaks = Arrays.asList(THIRD_ADVERT_BREAK, SECOND_ADVERT_BREAK, FIRST_ADVERT_BREAK);

        AdvertPlaybackState advertPlaybackState = AdvertPlaybackState.from(advertBreaks);
        List<AdvertBreak> actualAdvertBreaks = advertPlaybackState.advertBreaks();

        assertThat(actualAdvertBreaks).containsExactly(FIRST_ADVERT_BREAK, SECOND_ADVERT_BREAK, THIRD_ADVERT_BREAK);
    }

    @Test
    public void doesNotChangeTheOrderOfTheInputParameter() {
        List<AdvertBreak> advertBreaks = Arrays.asList(THIRD_ADVERT_BREAK, SECOND_ADVERT_BREAK, FIRST_ADVERT_BREAK);

        AdvertPlaybackState.from(advertBreaks);

        assertThat(advertBreaks).containsExactly(THIRD_ADVERT_BREAK, SECOND_ADVERT_BREAK, FIRST_ADVERT_BREAK);
    }

    private void assertThatGroupContains(AdPlaybackState.AdGroup adGroup, int numberOfAdverts, long[] advertDurations, Uri[] advertUris) {
        assertThat(adGroup.count).isEqualTo(numberOfAdverts);
        assertThat(adGroup.durationsUs).containsSequence(advertDurations);
        assertThat(adGroup.uris).containsExactly(advertUris);
    }
}
