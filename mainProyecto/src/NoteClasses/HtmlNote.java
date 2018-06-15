package NoteClasses;

/**
 *
 * @author Francesco Silvetti
 */
public class HtmlNote {
    
    private Note note;

    public HtmlNote(Note note) {
        
        this.note = note;
        
    }
    
    public String generateHtmlString() {
    
        return new String("<meta charset=\"UTF-8\"><html><body><H1>Note " 
                + this.note.getId() + "</H1><br><p>" 
                + this.note.getText() + "</p></body></html>");
    
    }
    
}
