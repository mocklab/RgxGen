package com.github.curiousoddman.rgxgen;

import com.github.curiousoddman.rgxgen.generator.nodes.Node;
import com.github.curiousoddman.rgxgen.generator.visitors.GenerationVisitor;
import com.github.curiousoddman.rgxgen.generator.visitors.UniqueGenerationVisitor;
import com.github.curiousoddman.rgxgen.generator.visitors.UniqueValuesCountingVisitor;
import com.github.curiousoddman.rgxgen.parsing.dflt.DefaultTreeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

@RunWith(Parameterized.class)
public class CombinedTests {
    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object> data() {
        return Arrays.asList(TestPattern.values());
    }

    @Parameterized.Parameter
    public TestPattern aTestPattern;

    private Pattern aPattern;

    @Before
    public void setUp() {
        aPattern = Pattern.compile(aTestPattern.aPattern);
    }

    @Test
    public void parseTest() {
        DefaultTreeBuilder defaultTreeBuilder = new DefaultTreeBuilder(aTestPattern.aPattern);
        Node node = defaultTreeBuilder.get();
        assertEquals(aTestPattern.aResultNode.toString(), node.toString());
    }

    @Test
    public void countTest() {
        assumeTrue(aTestPattern.hasEstimatedCound());

        UniqueValuesCountingVisitor v = new UniqueValuesCountingVisitor();
        aTestPattern.aResultNode.visit(v);
        assertEquals(aTestPattern.aEstimatedCount, v.getCount());
    }

    private boolean validateGenerated(String value) {
        if (aTestPattern.useFindForMatching()) {
            return aPattern.matcher(value)
                           .find();
        } else {
            return aPattern.matcher(value)
                           .matches();
        }
    }

    @Test
    public void generateTest() {
        for (int i = 0; i < 100; i++) {
            GenerationVisitor generationVisitor = new GenerationVisitor();
            aTestPattern.aResultNode.visit(generationVisitor);
            boolean result = validateGenerated(generationVisitor.getString());
            assertTrue("Text: '" + generationVisitor.getString() + "'does not match pattern " + aTestPattern.aPattern, result);
        }
    }

    @Test
    public void generateUniqueTest() {
        assumeTrue(aTestPattern.hasAllUniqueValues());

        UniqueGenerationVisitor v = new UniqueGenerationVisitor();
        aTestPattern.aResultNode.visit(v);
        assertEquals(aTestPattern.aAllUniqueValues, TestingUtilities.iteratorToList(v.getUniqueStrings()));
    }
}