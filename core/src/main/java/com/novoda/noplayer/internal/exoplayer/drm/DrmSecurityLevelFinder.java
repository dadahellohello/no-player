package com.novoda.noplayer.internal.exoplayer.drm;

import android.media.MediaDrm;
import android.os.Build;

import com.novoda.noplayer.internal.utils.Optional;

class DrmSecurityLevelFinder {

    private static final boolean DO_NOT_RETRY_ON_FAIL = false;
    private static final boolean RETRY_ON_FAIL = true;

    DrmSecurityLevel findSecurityLevel(Optional<MediaDrm> mediaDrm) {
        return queryDrmSecurityLevel(RETRY_ON_FAIL, mediaDrm);
    }

    private DrmSecurityLevel queryDrmSecurityLevel(boolean retry, Optional<MediaDrm> mediaDrmOptional) throws IllegalStateException {
        return mediaDrmOptional.isPresent() ? getDrmSecurityLevel(mediaDrmOptional.get(), retry) : DrmSecurityLevel.UNKNOWN;
    }

    private DrmSecurityLevel getDrmSecurityLevel(MediaDrm mediaDrm, boolean retry) {
        try {
            return getDrmSecurityLevel(mediaDrm);
        } catch (IllegalStateException exception) { // IllegalStateException as MediaDrmResetException is api 23+
            if (retry) {
                return queryDrmSecurityLevel(DO_NOT_RETRY_ON_FAIL, Optional.of(mediaDrm));
            } else {
                return DrmSecurityLevel.UNKNOWN;
            }
        }
    }

    private DrmSecurityLevel getDrmSecurityLevel(MediaDrm mediaDrm) {
        return is23MarshmallowOrOver() ? new MarshmallowSecurityLevelFinder().findSecurityLevel(mediaDrm)
                : new KitKatAndLollipopSecurityLevelFinder().findSecurityLevel(mediaDrm);
    }

    private boolean is23MarshmallowOrOver() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}