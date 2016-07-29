import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class LocationForm extends JPanel {
    private volatile float lat;
    private volatile float lon;

    private ActionListener updateCallback;

    final private JTextField latValue = new JTextField();
    final private JTextField lonValue = new JTextField();

    public LocationForm() {
        this.initialComponents();
    }

    public LocationForm(final float lat, final float lon) {
        this.lat = lat;
        this.lon = lon;
        latValue.setText(String.valueOf(lat));
        lonValue.setText(String.valueOf(lon));

        this.initialComponents();
    }

    public final float getLatitude() {
        return this.lat;
    }

    public final float getLongitude() {
        return this.lon;
    }

    private void initialComponents() {
        final JLabel latLabel = new JLabel();
        final JLabel lonLabel = new JLabel();
        final JButton updateButton = new JButton();

        this.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        latLabel.setText("Lat: ");
        lonLabel.setText("Lon: ");

        updateButton.setText("Update");
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                locationUpdated(evt);
            }
        });

        final GroupLayout mainPanelLayout = new GroupLayout(this);
        this.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
                mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                                        .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(latLabel)
                                                .addComponent(lonLabel))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(latValue)
                                                        .addComponent(lonValue,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                195,
                                                                Short.MAX_VALUE)
                                                        .addComponent(updateButton)
                                        )
                        )
        );
        mainPanelLayout.setVerticalGroup(
                mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(latLabel)
                                        .addComponent(latValue,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lonLabel)
                                        .addComponent(lonValue,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addComponent(updateButton)
                                .addContainerGap(80, Short.MAX_VALUE))
        );
    }



    private void locationUpdated(final ActionEvent event) {
        try {
            final float lat = Float.parseFloat(this.latValue.getText());
            final float lon  = Float.parseFloat(this.lonValue.getText());
            this.lat = lat;
            this.lon = lon;
            if (null != this.updateCallback) {
                this.updateCallback.actionPerformed(event);
            }
        } catch (final NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void addUpdateListener(final ActionListener actionListener) {
        this.updateCallback = actionListener;
    }
}