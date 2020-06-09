package khlopotov.ai.run.configuration;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.jar.JarApplicationConfiguration;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.List;

//It's impossible to inherit directly JarApplicationConfiguration in kotlin due to declarations clash
public class JmhConfiguration extends JarApplicationConfiguration {
    public JmhConfiguration(Project project, ConfigurationFactory factory, String name) {
        super(project, factory, name);
    }

    @Override @SuppressWarnings("unchecked")
    public void setBeforeRunTasks(@NotNull List value) {
        super.setBeforeRunTasks(value);
    }
}
