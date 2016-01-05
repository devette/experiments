package nl.yeex.knowledge.online;

import java.io.File;

import nl.yeex.knowledge.offline.generators.GeneratorContext;

import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is the main entry point for starting the generation of the HTML
 * files from the OWL file.
 * 
 */
public class OWL2KnowledgeServer {

    private static final Logger LOG = LoggerFactory.getLogger(OWL2KnowledgeServer.class);

    /**
     * Start generation.
     * 
     * @param context
     */
    public void start(GeneratorContext context) {
        LOG.info("----------- Start Knowlegde Server -----------\n");

        try {
            // Create a basic jetty server object that will listen on port 8080.
            // Note that if you set this to port 0 then a randomly available
            // port
            // will be assigned that you can either look in the logs for the
            // port,
            // or programmatically obtain it for use in test cases.
            Server server = new Server(8080);

            // The WebAppContext is the entity that controls the environment in
            // which a web application lives and breathes. In this example the
            // context path is being set to "/" so it is suitable for serving
            // root
            // context requests and then we see it setting the location of the
            // war.
            // A whole host of other configurations are available, ranging from
            // configuring to support annotation scanning in the webapp (through
            // PlusConfiguration) to choosing where the webapp will unpack
            // itself.
            WebAppContext webapp = new WebAppContext();
            webapp.setContextPath("/OWL2HTML-web");
            File warFile = new File(
                    "/Users/herman/Documents/develop/gitrepo/LocalDevelopment/OWL2HTML-web/target/OWL2HTML-web");
            webapp.setWar(warFile.getAbsolutePath());

            // A WebAppContext is a ContextHandler as well so it needs to be set
            // to
            // the server so it is aware of where to send the appropriate
            // requests.
            server.setHandler(webapp);

            // Configure a LoginService
            // Since this example is for our test webapp, we need to setup a
            // LoginService so this shows how to create a very simple hashmap
            // based
            // one. The name of the LoginService needs to correspond to what is
            // configured in the webapp's web.xml and since it has a lifecycle
            // of
            // its own we register it as a bean with the Jetty server object so
            // it
            // can be started and stopped according to the lifecycle of the
            // server
            // itself.
            HashLoginService loginService = new HashLoginService();
            loginService.setName("Knowledge Realm");
            loginService.setConfig("src/test/resources/realm.properties");
            server.addBean(loginService);

            // Start things up!
            server.start();

            // The use of server.join() the will make the current thread join
            // and
            // wait until the server is done executing.
            // See
            // http://docs.oracle.com/javase/7/docs/api/java/lang/Thread.html#join()
            server.join();
        } catch (Exception e) {
            LOG.error("Starting server failed.", e);
        }
        LOG.info("\n----------- Knowlegde Server Started -----------\n");

    }

}
