package khlopotov.ai.highlighting

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.testFramework.builders.JavaModuleFixtureBuilder
import com.intellij.testFramework.fixtures.JavaCodeInsightFixtureTestCase
import com.intellij.util.PathUtil
import khlopotov.ai.CanShowGuttersActivity
import khlopotov.ai.file.templates.jmhBenchmarkFileTemplate
import org.junit.Test
import org.openjdk.jmh.annotations.Benchmark

class JMHLineMarkerTest : JavaCodeInsightFixtureTestCase() {

    @Test fun test() {
        CanShowGuttersActivity.canShowGutters = true
        myFixture.configureByText("JavaBenchmarkTemplate.java", jmhBenchmarkFileTemplate("unit.test"))
        val gutter = myFixture.findAllGutters().single() as LineMarkerInfo.LineMarkerGutterIconRenderer<*>

        with(gutter.lineMarkerInfo) {
            assertTrue(this is BenchmarkLineMarkerInfo)
            assertEquals(lineMarkerTooltip, "Run JavaBenchmarkTemplate benchmark")
            assertEquals(element!!.text,"JavaBenchmarkTemplate")
        }
    }

    override fun tuneFixture(moduleBuilder: JavaModuleFixtureBuilder<*>) {
        moduleBuilder.addLibrary("jmh-core", PathUtil.getJarPathForClass(Benchmark::class.java))
    }
}