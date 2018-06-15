package NoteClasses;

/**
 *
 * @author Francesco Silvetti
 */
public class Note {

    private String text, id;

    public Note(String text, String id) {

        this.text = text;
        this.id = id;

    }

    public String getText() {
        return text;
    }

    public String getId() {
        return id;
    }

}
