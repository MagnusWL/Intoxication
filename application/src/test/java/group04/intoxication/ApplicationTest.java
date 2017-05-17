package group04.intoxication;

import group04.common.services.IServiceInitializer;
import group04.common.services.IServiceProcessor;
import java.io.File;
import java.io.IOException;
import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import junit.framework.Test;
import org.netbeans.junit.NbModuleSuite;
import org.netbeans.junit.NbTestCase;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

public class ApplicationTest extends NbTestCase {

    public static Test suite() {
        return NbModuleSuite.createConfiguration(ApplicationTest.class).
                gui(false).
                //failOnMessage(Level.WARNING). // works at least in RELEASE71
                //failOnException(Level.INFO).
                enableClasspathModules(false).
                clusters(".*").
                suite(); // RELEASE71+, else use NbModuleSuite.create(NbModuleSuite.createConfiguration(...))
    }

    public ApplicationTest(String n) {
        super(n);
    }

    public void testApplication() {
        // pass if there are merely no warnings/exceptions
        /* Example of using Jelly Tools (additional test dependencies required) with gui(true):
        new ActionNoBlock("Help|About", null).performMenu();
        new NbDialogOperator("About").closeByButton();
         */

        //SETUP 1: Finding the Path
        File file = new File(System.getProperty("user.dir")).getParentFile().getParentFile();
        String actualPath = file.getPath() + "/updatecenter/netbeans_site/updates.xml";
        String withJustCore = file.getPath() + "/test/removedupdates.xml";
        String withCoreAndPlayer = file.getPath() + "/test/updates.xml";
        String copyOfRealUpdates = file.getPath() + "/test/actual.xml";
        System.out.println("HEJ MED DIG: " + withCoreAndPlayer);

        //SETUP 2: ArrayLists
        List<IServiceProcessor> processors = new CopyOnWriteArrayList<>();
        List<IServiceInitializer> plugins = new CopyOnWriteArrayList<>();

        try {
            Thread.sleep(20000);
            waitForUpdate(processors, plugins);
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }

        //PRE ASSERTS
        assertEquals("No processors", 6, processors.size());
        assertEquals("No plugins", 11, plugins.size());
        

        //TEST: Unload Player
        try {
            copy(get(withJustCore), get(actualPath), REPLACE_EXISTING);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        try {
            Thread.sleep(10000);
            waitForUpdate(processors, plugins);
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }

        //ASSERTS: Player Unloaded
        assertEquals("All plugins", 10, plugins.size());
        assertEquals("All processors", 5, processors.size());

        //CLEAN UP
        try {
            copy(get(copyOfRealUpdates), get(actualPath), REPLACE_EXISTING);
            Thread.sleep(10000);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }

    }

    private void waitForUpdate(List<IServiceProcessor> processors, List<IServiceInitializer> plugins) throws InterruptedException {
        int j = 0;
        int h = 0;
        processors.clear();
        processors.addAll(Lookup.getDefault().lookupAll(IServiceProcessor.class));

        for (IServiceProcessor i : Lookup.getDefault().lookupAll(IServiceProcessor.class)) {
            h++;
            System.out.println("Processor: " + i.getClass().getName());
            System.out.println("Number of processors: " + h);
        }

        plugins.clear();
        plugins.addAll(Lookup.getDefault().lookupAll(IServiceInitializer.class));
        for (IServiceInitializer i : Lookup.getDefault().lookupAll(IServiceInitializer.class)) {
            j++;
            System.out.println("Plugin: " + i.getClass().getName());
            System.out.println("Number of plugins: " + j);
        }
    }

}
