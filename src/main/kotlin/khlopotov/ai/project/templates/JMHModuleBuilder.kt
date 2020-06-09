package khlopotov.ai.project.templates

import com.intellij.ide.projectWizard.ProjectSettingsStep
import com.intellij.ide.util.EditorHelper
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.ProjectJdkStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.application.runInEdt
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.roots.ui.configuration.ModulesProvider
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import khlopotov.ai.file.templates.jmhBenchmarkFileTemplate
import khlopotov.ai.file.templates.jmhPomFileTemplate
import khlopotov.ai.highlighting.benchmarkIcon
import org.jetbrains.idea.maven.model.MavenConstants
import org.jetbrains.idea.maven.project.MavenProjectsManager
import org.jetbrains.idea.maven.utils.MavenUtil
import org.jetbrains.idea.maven.wizards.AbstractMavenModuleBuilder
import org.jetbrains.idea.maven.wizards.MavenStructureWizardStep
import org.jetbrains.idea.maven.wizards.SelectPropertiesStep
import java.io.File


class JMHModuleBuilder : AbstractMavenModuleBuilder() {
    private var wizardContext: WizardContext? = null

    override fun getBuilderId() = "jmh.module.builder"
    override fun getNodeIcon() = benchmarkIcon
    override fun getGroupName() = JMHTemplateFactory.JMH_GROUP_NAME
    override fun isOpenProjectSettingsAfter() = false

    override fun createWizardSteps(
        wizardContext: WizardContext,
        modulesProvider: ModulesProvider
    ): Array<out ModuleWizardStep>? {
        this.wizardContext = wizardContext
        return arrayOf(
            ProjectJdkStep(wizardContext),
            MavenStructureWizardStep(this, wizardContext),
            SelectPropertiesStep(wizardContext.project, this)
        )
    }

    override fun setupRootModel(rootModel: ModifiableRootModel) {
        val project = createAndGetContentEntry()
        rootModel.addContentEntry(project)

        MavenUtil.runWhenInitialized(rootModel.project) {
            val groupId = projectId.groupId ?: "org.example"
            val artifactId = projectId.artifactId ?: "example"
            val version = projectId.version ?: "1.0-SNAPSHOT"
            runInEdt {
                runWriteAction {
                    val pomFile = project.createChildData(this, MavenConstants.POM_XML)
                    pomFile.setBinaryContent(
                        jmhPomFileTemplate(
                            groupId,
                            artifactId,
                            version,
                            "1.23",//todo find better solution
                            rootModel.sdk?.versionString ?: "1.8"
                        ).toByteArray()
                    )

                    val sourceDir = VfsUtil.createDirectories(
                        "${project.path}/src/main/java/${groupId.replace(".", "/")}"
                    )
                    val exampleFile = sourceDir.createChildData(this, "JavaBenchmarkTemplate.java")
                    exampleFile.setBinaryContent(
                        jmhBenchmarkFileTemplate(
                            groupId
                        ).toByteArray())

                    MavenProjectsManager.getInstance(rootModel.project)
                        .forceUpdateAllProjectsOrFindAllAvailablePomFiles()

                    // execute when current dialog is closed (e.g. Project Structure)
                    MavenUtil.invokeLater(rootModel.project, ModalityState.NON_MODAL) {
                        val psiFile = PsiManager.getInstance(rootModel.project).findFile(exampleFile)!!
                        EditorHelper.openInEditor(psiFile)
                    }
                }
            }
        }
    }

    private fun createAndGetContentEntry(): VirtualFile {
        val path = FileUtil.toSystemIndependentName(contentEntryPath!!)
        File(path).mkdirs()
        return LocalFileSystem.getInstance().refreshAndFindFileByPath(path)!!
    }

    override fun getIgnoredSteps(): MutableList<Class<out ModuleWizardStep>> {
        return (super.getIgnoredSteps() + listOf(ProjectSettingsStep::class.java)).toMutableList()
    }
}