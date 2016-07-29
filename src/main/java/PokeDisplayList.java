import com.google.common.collect.Lists;
import models.PokeResponse;
import models.Pokemon;
import services.PokeDao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;

public class PokeDisplayList extends JFrame {
    private static final Logger logger = Logger.getLogger(PokeDisplayList.class.getName());
    private static final String GOOGLE_MAP = "http://maps.google.com/?q=";

    private final List<Pokemon> pokemonCollection = Lists.newArrayList();
    private final PokeDao pokeDao;
    private PokeDisplayList(final List<Pokemon> pokemon, final PokeDao pokeDao) throws IOException {
        if (null != pokemon) {
            this.pokemonCollection.addAll(pokemon);
        }

        this.pokeDao = pokeDao;

        this.initializeComponents();
    }

    private void initializeComponents() throws IOException {
        this.setUndecorated(true);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        JButton closeButton = new JButton(new AbstractAction("x") {
            @Override
            public void actionPerformed(final ActionEvent e) {
                dispose();
            }
        });
        closeButton.setMargin(new Insets(1, 4, 1, 4));
        closeButton.setFocusable(false);
        this.add(closeButton);

        for (final Pokemon pokemon : this.pokemonCollection) {
            final ImageIcon icon = pokeDao.getIconForId(pokemon.getPokemonId());

            JButton button = new JButton(new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent event) {
                    try {
                        final URI uri = new URI(GOOGLE_MAP + pokemon.getLatitude() + "," + pokemon.getLongitude());
                        open(uri);
                    } catch (final URISyntaxException e) {
                        logger.warning("failed to create URI: " + e.getLocalizedMessage());
                    }
                }
            });
            button.setOpaque(false);
            button.setIcon(icon);

            this.add(button);
        }

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
    }

    public final void showList() {
        this.setVisible(true);
    }

    public final void hideList() {
        this.setVisible(false);
    }

    private void open(final URI uri) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(uri);
            } catch (final IOException e) {
                logger.warning("Error opening browser: " + e.getLocalizedMessage());
            }
        } else {
            logger.warning("Desktop not found, cannot open browser.");
        }
    }

    public static PokeDisplayList fromPokeResponse(final PokeResponse response) throws IOException {
        return new PokeDisplayList(response.getPokemon(), new PokeDao());
    }
}
