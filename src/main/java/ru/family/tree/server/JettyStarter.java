package ru.family.tree.server;

import org.eclipse.jetty.server.Server;
import org.springframework.beans.factory.annotation.Required;

/**
 * @author scorpion@yandex-team on 14.04.15.
 */
public class JettyStarter {
    private Server jettyServer;

    public void start() throws Exception {
        jettyServer.start();
    }

    @Required
    public void setJettyServer(final Server jettyServer) {
        this.jettyServer = jettyServer;
    }
}
