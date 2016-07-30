import services.PokeDao;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Logger;

public class PokeAlert extends JFrame {

    private static final Logger logger = Logger.getLogger(PokeAlert.class.getName());

    private static final int TASK_BAR_HEIGHT = 50;
    private static final int REPOLL_INTERVAL = 5000;
    private static final int POLL_INTERVAL = 60 * 1000 * 10;
    private static final int DISPLAY_DURATION = 15000;
    private final PokeDao pokeDao;
    private final PokeTimerTask pollingTask;

    private static final float DEFAULT_LATITUDE = (float) 35.34891059;
    private static final float DEFAULT_LONGITUDE = (float) -75.5044537;

    public static void main(String[] args) throws IOException {
        new PokeAlert(new PokeDao(), DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
    }

    PokeAlert(final PokeDao pokeDao) throws IOException {
        this(pokeDao, 0, 0);
    }

    PokeAlert(final PokeDao pokeDao, final float lat, final float lon) throws IOException {
        super();

        this.pokeDao = pokeDao;

        final Image image = new ImageIcon(
                ImageIO.read(ClassLoader.getSystemResourceAsStream("images/pokeball.png"))).getImage();

        this.setLookAndFeel();

        this.addIconToSystemTray(image);
        this.setIconImage(image);

        this.setLayout(new BorderLayout());
        this.add(this.buildMainPanel(lat, lon), BorderLayout.NORTH);

        this.setUndecorated(true);
        this.setVisible(true);

        this.pack();
        final GraphicsDevice defaultScreen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        final Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
        final int x = (int) rect.getMaxX() - this.getWidth();
        final int y = (int) rect.getMaxY() - this.getHeight() - TASK_BAR_HEIGHT;
        this.setLocation(x, y);

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        this.pollingTask = PokeTimerTask.builder()
                .pokeDao(this.pokeDao)
                .latitude(lat)
                .longitude(lon)
                .displayDuration(DISPLAY_DURATION)
                .repollInterval(REPOLL_INTERVAL)
                .pollInterval(POLL_INTERVAL)
                .build();

        this.pollingTask.start();
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.warning("Unable to set LookAndFeel");
        }
    }

    private void addIconToSystemTray(final Image image) throws IOException {
        if (SystemTray.isSupported()) {
            final SystemTray tray = SystemTray.getSystemTray();
            final TrayIcon trayIcon = new TrayIcon(image, "SystemTray Demo", this.buildPopupMenu());
            try {
                tray.add(trayIcon);
            } catch (final AWTException e1) {
                e1.printStackTrace();
            }
            trayIcon.setImageAutoSize(true);
        } else {
            logger.warning("system tray not supported");
        }
    }

    private PopupMenu buildPopupMenu() {
        final MenuItem exitItem = new MenuItem("Exit");
        final ActionListener exitListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.warning("Exiting....");
                System.exit(0);
            }
        };
        exitItem.addActionListener(exitListener);

        final MenuItem openItem = new MenuItem("Open");
        openItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                setVisible(true);
                setExtendedState(JFrame.NORMAL);
            }
        });

        final PopupMenu popup = new PopupMenu();
        popup.add(exitItem);
        popup.add(openItem);
        return popup;
    }

    private JPanel buildMainPanel(final float lat, final float lon) {
        final JPanel mainPanel = new JPanel(new BorderLayout());
        final LocationForm form = new LocationForm(lat, lon);
        form.addUpdateListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                pollingTask.setLatitude(form.getLatitude());
                pollingTask.setLongitude(form.getLongitude());
                pollingTask.runNow();
            }
        });
        mainPanel.add(this.buildCloseButtonPanel(), BorderLayout.EAST);

        mainPanel.add(form);
        return mainPanel;
    }

    private JPanel buildCloseButtonPanel() {
        final JButton closeButton = new JButton(new AbstractAction("x") {
            @Override
            public void actionPerformed(final ActionEvent e) {
                setVisible(false);
            }
        });
        closeButton.setMargin(new Insets(1, 4, 1, 4));
        closeButton.setFocusable(false);


        final JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);
        return buttonPanel;
    }
}