package KeyListeners;

import Windows.mainWindow;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;
import org.jnativehook.mouse.NativeMouseMotionListener;

/**
 * @author Francesco Silvetti, (Originally written by: javaQuery Global Keyboard
 * Listener)
 */
public class actionLogger extends Thread implements NativeKeyListener {

    private mainWindow mwwMainWindow;

    public actionLogger(mainWindow a) {

        super("keyLoggerProcess");

        this.mwwMainWindow = a;

    }

    /* Key Pressed */
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {

        System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));

        if (e.getKeyCode() == NativeKeyEvent.VC_F8) {

            this.mwwMainWindow.createNewFloatingWindow();
            //new floatingWindow(String.valueOf(this.mwwMainWindow.getFloatingWindowArray().size() + 1));

        }

        if (e.getKeyCode() == NativeKeyEvent.VC_F9) {

            if (this.mwwMainWindow.isVisible()) {
                
                this.mwwMainWindow.setVisible(false);
                
            }else {
            
                this.mwwMainWindow.setVisible(true);
                
            }
            //new floatingWindow(String.valueOf(this.mwwMainWindow.getFloatingWindowArray().size() + 1));

        }

    }

    /* Key Released */
    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {

    }

    /* I can't find any output from this call */
    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {

        System.out.println("Testing 2");

    }

    public void nativeMouseClicked(NativeMouseEvent e) {

        GlobalScreen.removeNativeMouseListener((NativeMouseListener) this);
        GlobalScreen.removeNativeMouseMotionListener((NativeMouseMotionListener) this);

    }

    public void run() {

        try {

            /* Register jNativeHook */
            GlobalScreen.registerNativeHook();

        } catch (NativeHookException ex) {

            /* Its error */
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);

        }

        /* Construct the example object and initialze native hook. */
        GlobalScreen.addNativeKeyListener(new actionLogger(this.mwwMainWindow));

    }

}
