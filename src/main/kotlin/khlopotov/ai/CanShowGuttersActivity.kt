package khlopotov.ai

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import org.jetbrains.idea.maven.project.MavenProjectsManager

class CanShowGuttersActivity : StartupActivity.Background {
    override fun runActivity(project: Project) {
        MavenProjectsManager.getInstance(project).addManagerListener(
            object : MavenProjectsManager.Listener {
                override fun importAndResolveScheduled() {
                    canShowGutters(project)
                }
            }
        )
        canShowGutters(project)
    }

    private fun canShowGutters(project: Project) {
        val mavenProject = MavenProjectsManager.getInstance(project).projects.firstOrNull() ?: return
        val dep = mavenProject.findDependencies("org.openjdk.jmh", "jmh-core").isNotEmpty()
        val shadePlugin = mavenProject.findPlugin("org.apache.maven.plugins", "maven-shade-plugin")
            ?.getGoalConfiguration("shade")
            ?.getChild("transformers")
            ?.children
            ?.any {
                it.getChildText("mainClass") == "org.openjdk.jmh.Main"
            } != null
        canShowGutters = dep && shadePlugin
    }

    companion object {
        var canShowGutters: Boolean = false
    }
}