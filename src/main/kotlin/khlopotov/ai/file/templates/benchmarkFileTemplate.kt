package khlopotov.ai.file.templates

import org.intellij.lang.annotations.Language

@Language("JAVA") fun jmhBenchmarkFileTemplate(pgk: String) =
"""
package ${pgk};

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

public class JavaBenchmarkTemplate {

    @Setup
    public void setup() {
    
    }

    @Benchmark 
    public void benchmark(Blackhole bh) {
    
    }
}
""".trimIndent()