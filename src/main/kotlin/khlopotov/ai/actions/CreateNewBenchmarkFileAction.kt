package khlopotov.ai.actions

import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.ide.actions.CreateFromTemplateAction
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaDirectoryService
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiDirectory
import khlopotov.ai.CanShowGuttersActivity.Companion.canShowGutters
import khlopotov.ai.highlighting.benchmarkIcon


class CreateNewBenchmarkFileAction : CreateFromTemplateAction<PsiClass>(
    "JMHBenchmark Class",
    "Create new JMH benchmark class",
    benchmarkIcon
), DumbAware {
    override fun createFile(name: String, templateName: String, dir: PsiDirectory): PsiClass {
        val clazz = JavaDirectoryService.getInstance().createClass(dir, name, templateName, true)
        JavaPsiFacade.getInstance(dir.project).elementFactory.apply {
            clazz.containingFile.addBefore(createImportStatementOnDemand("org.openjdk.jmh.annotations"), clazz)
            clazz.add(createMethodFromText("@Setup public void setup() {}", null))
            clazz.add(createMethodFromText("@Benchmark public void benchmark() {}", null))
        }
        return clazz
    }

    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder.setTitle("Create new benchmark class").addKind("Class name", benchmarkIcon, "Class")
    }

    override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?) = "JMHBenchmark Class"

    override fun isAvailable(dataContext: DataContext?) = canShowGutters
}