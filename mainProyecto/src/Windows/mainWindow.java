package Windows;

/**
 *
 * @author Francesco Silvetti
 */
import KeyListeners.actionLogger;
import NoteClasses.HtmlNote;
import NoteClasses.Note;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import NoteClasses.personalizedTextArea;
import NoteClasses.textAreaPanel;
import com.itextpdf.text.Anchor;
import java.awt.Font;
import java.io.FileOutputStream;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfWriter;

public class mainWindow extends JFrame implements ActionListener {

    private Container ctrMainContainer;
    private JPanel pnlMainPanel, pnlNoteDisplayer;
    private JMenuBar mbrMainMenuBar;
    private JMenu mnuFile, mnuTools, mnuSession;
    private JMenuItem mitSave, mitRefresh, mitExitApp, mitToHtml, mitToPdf;
    private JToolBar tbrMainToolBar;
    private JButton btnAdd, btnDelete;
    private JScrollPane scpScrollPane;

    private Thread keyLogger;
    private String userName;

    public ArrayList<personalizedTextArea> floatingWindowArray = new ArrayList<personalizedTextArea>();
    private final Dimension dmnWindowSize = new Dimension(500, 400);

    public mainWindow(String userName) {

        super("Notas");

        this.buildComponents();
        this.buildMainPanel();
        this.configureWindow();

        this.userName = userName;

        this.keyLogger = new actionLogger(this);
        this.keyLogger.start();

        this.refreshNotePanelFromJSON();

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(this.mitSave)) {

            try {

                this.saveToJSON();

            } catch (ParseException ex) {

                Logger.getLogger(mainWindow.class.getName()).log(Level.SEVERE, null, ex);

            }

        } else if (e.getSource().equals(this.mitRefresh)) {

            System.out.println("Refreshing...");

            this.refreshNotePanel();

        } else if (e.getSource().equals(this.btnAdd)) {

            this.createNewFloatingWindow();

        } else if (e.getSource().equals(this.btnDelete)) {
            this.deleteSelectedNotes();
            //this.eliminateFloatingWindow();
        } else if (e.getSource().equals(this.mitExitApp)) {

            this.exitApp();

        } else if ((e.getSource().equals(this.mitToHtml))) {

            this.createHtmlFile();

        } else if ((e.getSource().equals(this.mitToPdf))) {

            try {
                this.createPdfFile();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(mainWindow.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DocumentException ex) {
                Logger.getLogger(mainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {

            System.out.println("Nope...");

        }

    }

    private void configureWindow() {

        this.setResizable(false);
        this.setSize(dmnWindowSize);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        this.setJMenuBar(this.mbrMainMenuBar);

    }

    private void buildComponents() {

        //*Build JContainers
        this.ctrMainContainer = this.getContentPane();

        //*Build JPanels
        this.pnlMainPanel = new JPanel();
        this.pnlMainPanel.setLayout(new BoxLayout(this.pnlMainPanel, BoxLayout.Y_AXIS));
        this.pnlMainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        this.pnlNoteDisplayer = new JPanel();
        this.pnlNoteDisplayer.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.pnlNoteDisplayer.setPreferredSize(this.dmnWindowSize);
        this.pnlNoteDisplayer.setBorder(BorderFactory.createLineBorder(Color.black, 1));

        //*Build JMenuItems
        this.mitSave = new JMenuItem("Save");
        this.mitSave.addActionListener(this);

        //this.mitRefresh = new JMenuItem("Refresh");
        //this.mitRefresh.addActionListener(this);

        this.mitExitApp = new JMenuItem("Exit");
        this.mitExitApp.addActionListener(this);

        this.mitToHtml = new JMenuItem("Create .HTML");
        this.mitToHtml.addActionListener(this);

        this.mitToPdf = new JMenuItem("Create .PDF");
        this.mitToPdf.addActionListener(this);

        //*Build JMenus
        this.mnuFile = new JMenu("File");
        this.mnuFile.add(this.mitSave);
        //this.mnuFile.add(this.mitRefresh);

        this.mnuTools = new JMenu("Tools");
        this.mnuTools.add(this.mitToHtml);
        this.mnuTools.add(this.mitToPdf);

        this.mnuSession = new JMenu("Session");
        this.mnuSession.add(this.mitExitApp);

        //*Build JMenuBars
        this.mbrMainMenuBar = new JMenuBar();
        this.mbrMainMenuBar.add(this.mnuFile);
        this.mbrMainMenuBar.add(this.mnuTools);
        this.mbrMainMenuBar.add(this.mnuSession);

        //*Build JButtons
        this.btnAdd = new JButton(" + ");
        this.btnAdd.addActionListener(this);

        this.btnDelete = new JButton(" - ");
        this.btnDelete.addActionListener(this);

        //*Build JToolBars
        this.tbrMainToolBar = new JToolBar();
        this.tbrMainToolBar.add(this.btnAdd);
        this.tbrMainToolBar.add(this.btnDelete);

        //*Build JScrollPanes
        this.scpScrollPane = new JScrollPane();
        //this.scpScrollPane.add(this.pnlNoteDisplayer);

    }

    private void buildMainPanel() {

        this.pnlMainPanel.add(this.tbrMainToolBar);
        this.pnlMainPanel.add(this.pnlNoteDisplayer);

        this.ctrMainContainer.add(this.pnlMainPanel);

    }

    public void createNewFloatingWindow() {

        //floatingWindow y = new floatingWindow(String.valueOf(this.floatingWindowArray.size()), this);
        FloatingWindowMaterial y = new FloatingWindowMaterial(this);

    }

    private void createNewFloatingWindow(JSONObject jsonObject) {

        //floatingWindow y = new floatingWindow(String.valueOf(this.floatingWindowArray.size()), jsonObject, this);
    }

    public void saveToWindowArray(personalizedTextArea textArea) {

        this.floatingWindowArray.add(textArea);
        this.addNoteToPanel(textArea.getText());

    }

//    private void eliminateFloatingWindow() {
//
//        this.floatingWindowArray.get(this.floatingWindowArray.size() - 1).dispose();
//        this.floatingWindowArray.remove(this.floatingWindowArray.size() - 1);
//
//    }
    public ArrayList<personalizedTextArea> getFloatingWindowArray() {
        return floatingWindowArray;
    }

    private void saveToJSON() throws ParseException {

        JSONParser parser = new JSONParser();

        try (FileWriter file = new FileWriter(this.userName + ".json")) {

            JSONObject obj = new JSONObject();

            int noteCounter = 0;

            for (personalizedTextArea x : this.floatingWindowArray) {

                JSONArray aux = new JSONArray();
                aux.add(x.getNoteID());
                aux.add(x.getText());

                obj.put(("Floater " + x.getNoteID()), aux);

                noteCounter++;

            }

            obj.put("noteCount", noteCounter);

            file.write(obj.toJSONString());
            file.flush();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    //This is for saving an JSONObject into a .JSON file(not finished yet)
    private void saveToJSON(JSONObject jsonObject) throws ParseException {

        JSONParser parser = new JSONParser();

        try (FileWriter file = new FileWriter(this.userName + ".json")) {

            JSONObject obj = new JSONObject();

            int noteCounter = 0;

            for (personalizedTextArea x : this.floatingWindowArray) {

                JSONArray aux = new JSONArray();
                aux.add(x.getNoteID());
                aux.add(x.getText());

                obj.put(("Floater " + x.getNoteID()), aux);

                noteCounter++;

            }

            obj.put("noteCount", noteCounter);

            file.write(obj.toJSONString());
            file.flush();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public void refreshNotePanel() {

        for (int x = 0; x < this.floatingWindowArray.size(); x++) {
            
            textAreaPanel name = (textAreaPanel) this.pnlNoteDisplayer.getComponent(x);
            name.txaMainTextArea.disable();
            this.pnlNoteDisplayer.remove(x);
            
        }
        
        this.pnlNoteDisplayer.removeAll();

        for (int x = 0; x < this.floatingWindowArray.size(); x++) {

            this.pnlNoteDisplayer.add(this.floatingWindowArray.get(x));

            this.pnlNoteDisplayer.validate();

        }

    }

    private void refreshNotePanelFromJSON() {

        ///* 
        try {

            JSONParser parser = new JSONParser();

            Object obj = parser.parse(new FileReader(this.userName + ".json"));

            JSONObject jsonObject = (JSONObject) obj;

            try {

                this.pnlNoteDisplayer.removeAll();

                for (long x = 0; x < (long) jsonObject.get("noteCount"); x++) {

                    JSONArray aux = (JSONArray) jsonObject.get(("Floater " + String.valueOf(x)));
                    Object[] auxList = aux.toArray();
                    textAreaPanel aux_2 = new textAreaPanel(new Note((String) auxList[1], (String) auxList[0]));
                    this.pnlNoteDisplayer.add(aux_2);

                    this.floatingWindowArray.add(aux_2.txaMainTextArea);

                    this.pnlNoteDisplayer.validate();
                    //this.scpScrollPane.validate();

                }

            } catch (ArrayIndexOutOfBoundsException e) {

                e.printStackTrace();

            } catch (NullPointerException e) {

                e.printStackTrace();
                //System.out.println(this.pnlMainPanel.getComponents());

            }

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (ParseException e) {

            e.printStackTrace();

        }
        //*/

    }

    public void exitApp() {
        //Ask user if he/she want's to exit
        System.exit(1);

    }

    private void deleteSelectedNotes() {

        for (int x = 0; x < this.pnlNoteDisplayer.getComponents().length; x++) {

            textAreaPanel aux = (textAreaPanel) this.pnlNoteDisplayer.getComponent(x);

            if (aux.isSelected()) {

                aux.setVisible(false);
                this.pnlNoteDisplayer.remove(x);
                this.floatingWindowArray.remove(x);

            }

        }

        this.pnlNoteDisplayer.validate();

    }

    private void addNoteToPanel(String text) {

        this.pnlNoteDisplayer.add(new textAreaPanel(new Note(text, String.valueOf(this.pnlNoteDisplayer.getComponents().length))));
        this.pnlNoteDisplayer.validate();

    }

    private void createHtmlFile() {

        for (int x = 0; x < this.pnlNoteDisplayer.getComponents().length;) {

            textAreaPanel aux = (textAreaPanel) this.pnlNoteDisplayer.getComponent(x);

            if (aux.isSelected()) {

                try (FileWriter file = new FileWriter(aux.txaMainTextArea.getNoteID() + ".html")) {

                    file.write(new HtmlNote(aux.txaMainTextArea.note).generateHtmlString());
                    file.flush();

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }

            x++;
        }

    }

    private void createPdfFile() throws FileNotFoundException, DocumentException {

        for (int x = 0; x < this.pnlNoteDisplayer.getComponents().length;) {

            textAreaPanel aux = (textAreaPanel) this.pnlNoteDisplayer.getComponent(x);

            if (aux.isSelected()) {

                Document document = new Document(PageSize.A6, 50, 50, 50, 50);

                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(aux.txaMainTextArea.note.getId() + ".pdf"));

                document.open();

                Anchor anchorTarget = new Anchor("");
                anchorTarget.setName("Top");
                Paragraph paragraph1 = new Paragraph("Note " + aux.txaMainTextArea.note.getId() + ":", FontFactory.getFont(FontFactory.COURIER, 26, Font.BOLD, new CMYKColor(255, 255, 255, 0)));

                paragraph1.setSpacingBefore(50);

                paragraph1.add(anchorTarget);
                document.add(paragraph1);

                document.add(new Paragraph(aux.txaMainTextArea.note.getText(), FontFactory.getFont(FontFactory.COURIER, 14, Font.PLAIN, new CMYKColor(255, 255, 255, 0))));

                document.close();

            }

            x++;
        }

    }

}
