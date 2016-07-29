import models.PokeResponse;
import services.PokeDao;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class PokeTimerTask extends TimerTask {
    private static final Logger logger = Logger.getLogger(PokeTimerTask.class.getName());
    private static final String IN_PROGRESS = "in_progress";
    private static final String SUCCESS = "success";

    private final PokeDao pokeDao;
    private float lat;
    private float lon;
    private int displayDuration;
    private int repollInterval;
    private int pollInterval;

    private PokeTimerTask(final PokeTimerTaskBuilder builder) {
        this.lat = builder.getLatitude();
        this.lon = builder.getLongitude();
        this.pokeDao = builder.getPokeDao();
        this.displayDuration = builder.getDisplayDuration();
        this.repollInterval = builder.getRepollInterval();
        this.pollInterval = builder.getPollInterval();
    }

    public final void setLatitude(final float lat) {
        this.lat = lat;
    }

    public final void setLongitude(final float lon) {
        this.lon = lon;
    }

    public final void start() {
        new Timer().scheduleAtFixedRate(this, 0, this.pollInterval);
    }

    @Override
    public void run() {
        String dataId = null;
        try {
            try {
                final String dataIdResponse = pokeDao.getDataId(lat, lon);
                if (null != dataIdResponse) {
                    dataId = dataIdResponse;
                }
                if (null != dataId) {
                    PokeResponse response = pokeDao.getPokeData(lat, lon, dataId);
                    while (isWaitingForResults(response)) {
                        Thread.sleep(repollInterval);
                        response = pokeDao.getPokeData(lat, lon, dataId);
                    }
                    if (response.getStatus().equals(SUCCESS)) {
                        final PokeDisplayList displayList = PokeDisplayList.fromPokeResponse(response);
                        displayListForDuration(displayList, displayDuration);
                    } else {
                        handleFailedRequest();
                    }
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
            Thread.sleep(pollInterval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void displayListForDuration(final PokeDisplayList displayList, final int displayDuration) {
        displayList.showList();
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(displayDuration);
                    displayList.dispose();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            ;
        }.start();
    }


    private void handleFailedRequest() {
        logger.warning("Request to the server returned an error");
    }

    private boolean isWaitingForResults(final PokeResponse response) {
        return response.getStatus().equals(SUCCESS) &&
                (null != response.getJobStatus() && response.getJobStatus().equals(IN_PROGRESS));
    }

    public static PokeTimerTaskBuilder builder() {
        return new PokeTimerTaskBuilder();
    }

    public static class PokeTimerTaskBuilder {
        private PokeDao pokeDao;
        private float lat;
        private float lon;
        private int displayDuration = 10000;
        private int repollInterval = 5000;
        private int pollInterval = 60 * 1000 * 15;

        private PokeTimerTaskBuilder() {
        }

        public final PokeDao getPokeDao() {
            return this.pokeDao;
        }

        public final float getLatitude() {
            return this.lat;
        }

        public final float getLongitude() {
            return this.lon;
        }

        public final int getDisplayDuration() {
            return this.displayDuration;
        }

        public final int getRepollInterval() {
            return this.repollInterval;
        }

        public final int getPollInterval() {
            return this.pollInterval;
        }

        public final PokeTimerTaskBuilder pokeDao(final PokeDao pokeDao) {
            this.pokeDao = pokeDao;
            return this;
        }

        public final PokeTimerTaskBuilder latitude(final float lat) {
            this.lat = lat;
            return this;
        }

        public final PokeTimerTaskBuilder longitude(final float lon) {
            this.lon = lon;
            return this;
        }

        public final PokeTimerTaskBuilder displayDuration(final int duration) {
            this.displayDuration = duration;
            return this;
        }

        public final PokeTimerTaskBuilder repollInterval(final int duration) {
            this.repollInterval = duration;
            return this;
        }

        public final PokeTimerTaskBuilder pollInterval(final int duration) {
            this.pollInterval = duration;
            return this;
        }

        public PokeTimerTask build() {
            return new PokeTimerTask(this);
        }
    }
}
