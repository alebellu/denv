package org.ssoup.denv.cli;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.ssoup.denv.core.model.conf.application.ApplicationConfiguration;
import org.ssoup.denv.core.model.runtime.Application;
import org.ssoup.denv.core.model.runtime.Environment;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collection;

/**
 * User: ALB
 * Date: 14/09/14 18:26
 */
@Service
@Scope("singleton")
public class DenvConsole {

    private PrintStream out = System.out;
    private PrintStream err = System.err;

    private ByteArrayOutputStream localOutputStream;
    private ByteArrayOutputStream localErrorStream;

    public void setUseLocalStreams(boolean useLocalStreams) {
        if (useLocalStreams) {
            localOutputStream = new ByteArrayOutputStream();
            localErrorStream = new ByteArrayOutputStream();
            out = new PrintStream(localOutputStream);
            err = new PrintStream(localErrorStream);
        } else {
            out = System.out;
            err = System.err;
        }
    }

    public void println(String str) {
        out.println(str);
    }

    public void error(String str) {
        err.println(str);
    }

    public void error(Exception ex) {
        err.print(ex.getMessage());
    }

    public void printEnvs(Collection<? extends Environment> envs) {
        // see http://stackoverflow.com/questions/15215326/how-can-i-create-ascii-table-in-java-in-a-console
        String leftAlignTitleFormat = "%-20s %-12s %-12s %-12s %n";
        String leftAlignFormat      = "%-20s %-12d %-12d %-12d %n";

        out.format(leftAlignTitleFormat, "ENVIRONMENT ID", "APPS", "DEPLOYED", "RUNNING");
        for (Environment env : envs) {
            int totalApps = 0, deployedApps = 0, runningApps = 0;
            if (env.getApplications() != null) {
                totalApps = env.getApplications().size();
                for (Application app : env.getApplications()) {
                    if (app.isDeployed()) deployedApps ++;
                    if (app.isStarted()) runningApps ++;
                }
            }
            out.format(leftAlignFormat, env.getId(), totalApps, deployedApps, runningApps);
        }
    }

    public void printApps(Collection<? extends ApplicationConfiguration> apps) {
        // see http://stackoverflow.com/questions/15215326/how-can-i-create-ascii-table-in-java-in-a-console
        String leftAlignTitleFormat = "%-20s %-30s %n";
        String leftAlignFormat      = "%-20s %-30s %n";

        out.format(leftAlignTitleFormat, "APPLICATION ID", "DESCRIPTION");
        for (ApplicationConfiguration app : apps) {
            out.format(leftAlignFormat, app.getId(), app.getDescription());
        }
    }

    public ByteArrayOutputStream getLocalOutputStream() {
        return localOutputStream;
    }

    public ByteArrayOutputStream getLocalErrorStream() {
        return localErrorStream;
    }
}
