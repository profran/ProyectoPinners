package NoteClasses;

import java.awt.Dimension;
import javax.swing.JTextArea;

/**
 *
 * @author Francesco Silvetti
 */
public class personalizedTextArea extends JTextArea {

    private Dimension dmnComponentSize = new Dimension(75, 100);
    public Note note;

    public personalizedTextArea(Note note) {

        super();

        this.note = note;

        this.setText(note.getText());
        this.configureComponent();

    }

    private void configureComponent() {

        this.setMaximumSize(this.dmnComponentSize);
        this.setPreferredSize(this.dmnComponentSize);
        this.setEditable(false);

    }

    public String getNoteID() {

        return this.note.getId();
    }
    
}
//NullPointerException CORREGIR!!!
