package com.github.curiousoddman.rgxgen.generator.visitors;

import com.github.curiousoddman.rgxgen.generator.nodes.*;
import com.github.curiousoddman.rgxgen.iterators.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

public class UniqueGenerationVisitor implements NodeVisitor {
    private List<Supplier<Iterator<String>>> aIterators = new ArrayList<>();

    private static Iterator<String> permutationsOrFlat(List<Supplier<Iterator<String>>> itSupp) {
        if (itSupp.size() == 1) {
            return itSupp.get(0)
                         .get();
        } else {
            return new PermutationsIterator(itSupp);
        }
    }

    @Override
    public void visit(AnySymbol node) {
        aIterators.add(() -> new ArrayIterator(AnySymbol.ALL_SYMBOLS));
    }

    @Override
    public void visit(Choice node) {
        List<List<Supplier<Iterator<String>>>> nodeIterators = new ArrayList<>(node.getNodes().length);
        for (Node n : node.getNodes()) {
            UniqueGenerationVisitor v = new UniqueGenerationVisitor();
            n.visit(v);
            nodeIterators.add(v.aIterators);
        }

        aIterators.add(() -> new ChoiceIterator(nodeIterators));
    }

    @Override
    public void visit(FinalSymbol node) {
        aIterators.add(() -> new SingleValueIterator(node.getValue()));
    }

    @Override
    public void visit(Repeat node) {
        // Getting all possible sub node contents
        UniqueGenerationVisitor v = new UniqueGenerationVisitor();
        node.getNode()
            .visit(v);

        List<Supplier<Iterator<String>>> iterators = v.aIterators;

        // (a|b){1} -> "a", "b" --> "a", "b"
        // (a|b){2} -> "a", "b" --> "aa", "ab", "ba", "bb"
        // (a|b){1,2} -> "a", "b" --> "a", "b", "aa", "ab", "ba", "bb"
        // (a|b){0,2} -> "a", "b" --> "", "a", "b", "aa", "ab", "ba", "bb"


        // Take 0 from list
        // Take 1 from list
        // Take and concatenate 2 from list
        // ...

        aIterators.add(() -> new IncrementalLengthIterator(() -> permutationsOrFlat(v.aIterators), node.getMin(), node.getMax()));
    }

    @Override
    public void visit(Sequence node) {
        for (Node n : node.getNodes()) {
            n.visit(this);
        }
    }

    public Iterator<String> getUniqueStrings() {
        return permutationsOrFlat(aIterators);
    }
}
