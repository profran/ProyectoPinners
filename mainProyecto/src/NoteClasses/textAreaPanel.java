package NoteClasses;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Francesco Silvetti
 */
public class textAreaPanel extends JPanel {

    public personalizedTextArea txaMainTextArea;
    private JCheckBox ckxSelection;

    private Dimension dmnComponentSize = new Dimension(80, 125);

    public textAreaPanel(Note note) {

        this.txaMainTextArea = new personalizedTextArea(note);
        this.ckxSelection = new JCheckBox();

        this.configureComponent();
        this.buildComponent(note);

    }

    private void configureComponent() {

        this.setMaximumSize(this.dmnComponentSize);
        this.setPreferredSize(this.dmnComponentSize);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));

    }

    private void buildComponent(Note note) {

        this.add(new JPanel(new FlowLayout(FlowLayout.LEFT)).add(new JLabel(note.getId())));
        this.add(this.txaMainTextArea);
        this.add(new JPanel(new FlowLayout(FlowLayout.RIGHT)).add(this.ckxSelection));

        this.validate();

    }

    public boolean isSelected() {

        return this.ckxSelection.isSelected();

    }

}
