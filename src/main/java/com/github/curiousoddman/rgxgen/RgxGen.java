package com.github.curiousoddman.rgxgen;

import com.github.curiousoddman.rgxgen.generator.nodes.Node;
import com.github.curiousoddman.rgxgen.generator.visitors.GenerationVisitor;
import com.github.curiousoddman.rgxgen.generator.visitors.UniqueGenerationVisitor;
import com.github.curiousoddman.rgxgen.generator.visitors.UniqueValuesCountingVisitor;
import com.github.curiousoddman.rgxgen.parsing.DefaultTreeBuilder;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * String values generator based on regular expression pattern
 */
public class RgxGen {
    private final String aPattern;
    private final Node   aNode;

    public RgxGen(String pattern) {
        aPattern = pattern;
        aNode = new DefaultTreeBuilder(aPattern).get();
    }

    /**
     * Returns estimation of unique values that can be generated with the pattern.
     * This might not be accurate, though. For example the pattern  "(a{0,2}|b{0,2})" will estimate to 6,
     * though actual count is only 5, because right and left part of group can yield same value
     *
     * @return number of unique values or null, if infinite
     */
    public BigInteger numUnique() {
        UniqueValuesCountingVisitor v = new UniqueValuesCountingVisitor();
        aNode.visit(v);
        return v.getCount();
    }

    /**
     * Stream values generated with the pattern.
     * Values may repeat.
     *
     * @return stream of generated strings
     */
    public Stream<String> stream() {
        return Stream.generate(this::generate);
    }

    /**
     * Stream *UNIQUE* values generated by pattern
     *
     * @return stream of unique generated strings
     */
    public Iterator<String> iterateUnique() {
        UniqueGenerationVisitor ugv = new UniqueGenerationVisitor();
        aNode.visit(ugv);
        return ugv.getUniqueStrings();
    }

    /**
     * Generate another string from the pattern.
     *
     * @return generated string.
     */
    public String generate() {
        GenerationVisitor gv = new GenerationVisitor();
        aNode.visit(gv);
        return gv.getString();
    }
}
