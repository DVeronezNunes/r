/*
 * SfwInstaladorScriptApp.java
 */
package sfwinstaladorscript;

import java.util.Locale;
import javax.swing.JOptionPane;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class SfwInstaladorScriptApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        show(new SfwInstaladorScriptView(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of SfwInstaladorScriptApp
     */
    public static SfwInstaladorScriptApp getApplication() {
        return Application.getInstance(SfwInstaladorScriptApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {

        String v_string_error;
        String v_string_java_version = System.getProperty("java.version");
        String[] v_string_version_array = v_string_java_version.split("\\.");

        if ((Integer.parseInt(v_string_version_array[0]) < 1)
                || (Integer.parseInt(v_string_version_array[0]) == 1 && Integer.parseInt(v_string_version_array[1]) < 5)) {

            v_string_error = String.format(Utils.getDefaultBundle().getString("App.javaversionerror"), v_string_java_version, System.getProperty("java.home"));

            System.out.println(v_string_error);
            JOptionPane.showMessageDialog(null, v_string_error, "Erro", JOptionPane.ERROR_MESSAGE);
        } else {
            if (args.length > 0) {
                if (args[0].equals("validar")) {
                    Utils.setObjects_validade(true);
                }
            }

            Install.set_default_location(Locale.getDefault());
            Install.set_us_location(Locale.US);
            launch(SfwInstaladorScriptApp.class, args);
        }
    }
}
