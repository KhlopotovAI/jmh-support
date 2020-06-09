package khlopotov.ai.run.configuration

import com.intellij.execution.configurations.SimpleConfigurationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NotNullLazyValue
import khlopotov.ai.highlighting.benchmarkIcon
import javax.swing.Icon


class JmhConfigurationType : SimpleConfigurationType(
    "JMH",
    "JMH Benchmark",
    "Configuration to run a JMH Benchmark",
    NotNullLazyValue.createValue { benchmarkIcon }
) {
    override fun createTemplateConfiguration(project: Project) =
        object : JmhConfiguration(project, this, "JMH") {
            override fun getIcon(): Icon = benchmarkIcon
        }
}



