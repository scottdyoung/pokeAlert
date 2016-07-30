package services;

import com.google.gson.Gson;
import models.PokeResponse;
import models.ScanResponse;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public class PokeDao {
    private static final String ICON_URL = "https://ugc.pokevision.com/images/pokemon/";
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";
    private static final String SCAN_URL = "https://pokevision.com/map/data/"; // lat/lon/id
    private static final String DATA_ID_URL = "https://pokevision.com/map/scan/"; // lat/lon;

    private static final Gson gson = new Gson();
    private static final Logger logger = Logger.getLogger(PokeDao.class.getName());

    public ScanResponse getScanResponse(final float lat, final float lon) throws IOException {
        final URL url = new URL(DATA_ID_URL + lat + "/" + lon);
        return gson.fromJson(this.getHtmlResponseAsString(url), ScanResponse.class);
    }

    public PokeResponse getPokeData(final float lat, final float lon, final String dataId) throws IOException {
        final URL url = new URL(SCAN_URL + lat + "/" + lon + "/" + dataId);
        return gson.fromJson(this.getHtmlResponseAsString(url), PokeResponse.class);
    }

    public ImageIcon getIconForId(final int id) throws IOException {
        final URL url = new URL(ICON_URL + id + ".png");
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", USER_AGENT);
        return new ImageIcon(ImageIO.read(connection.getInputStream()));
    }

    public String getHtmlResponseAsString(final URL url) throws IOException {
        logger.info("Fetching URL: " + url);
        final StringBuilder result = new StringBuilder();
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestMethod("GET");
        final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        reader.close();
        logger.info("Results: " + result.toString());
        return result.toString();
    }
}
