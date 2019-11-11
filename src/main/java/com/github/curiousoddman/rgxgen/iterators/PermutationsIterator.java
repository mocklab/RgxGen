package com.github.curiousoddman.rgxgen.iterators;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PermutationsIterator implements Iterator<String> {
    private final List<Supplier<Iterator<String>>> aIteratorsSuppliers;
    private final Iterator<String>[]               aIterators;
    private final String[]                         aGeneratedParts;

    public PermutationsIterator(List<Supplier<Iterator<String>>> iteratorsSuppliers) {
        aIteratorsSuppliers = iteratorsSuppliers;
        aIterators = new Iterator[aIteratorsSuppliers.size()];

        aGeneratedParts = new String[aIterators.length];

        for (int i = 0; i < aIterators.length; i++) {
            Iterator<String> iterator = aIteratorsSuppliers.get(i)
                                                           .get();
            aIterators[i] = iterator;
        }

        // Make sure it is null, because it's used for check later
        aGeneratedParts[0] = null;

    }

    public PermutationsIterator(int length, String[] values) {
        this(IntStream.range(0, length)
                      .mapToObj(i -> ((Supplier<Iterator<String>>) () -> Arrays.stream(values)
                                                                               .iterator()))
                      .collect(Collectors.toList()));
    }

    @Override
    public boolean hasNext() {
        return aGeneratedParts[0] == null || Arrays.stream(aIterators)
                                                   .anyMatch(Iterator::hasNext);
    }

    @Override
    public String next() {
        // Initialize all value
        if (aGeneratedParts[0] == null) {
            for (int i = 0; i < aGeneratedParts.length; i++) {
                aGeneratedParts[i] = aIterators[i].next();
            }
        } else {
            // Advance one of iterators
            for (int i = aGeneratedParts.length - 1; i >= 0; --i) {
                if (aIterators[i].hasNext()) {
                    aGeneratedParts[i] = aIterators[i].next();
                    break;
                } else if (i == 0) {
                    // We can only reset other iterators. Head iterator should use all it's values only once
                    throw new NoSuchElementException("No more unique values");
                } else {
                    Iterator<String> iterator = aIteratorsSuppliers.get(i)
                                                                   .get();
                    aIterators[i] = iterator;
                    aGeneratedParts[i] = iterator.next();
                }
            }
        }

        return Arrays.stream(aGeneratedParts.clone())
                     .reduce("", String::concat);
    }

    @Override
    public String toString() {
        return "PermutationsIterator{" +
                "aIteratorsSuppliers=" + aIteratorsSuppliers +
                ", aIterators=" + Arrays.toString(aIterators) +
                ", aGeneratedParts=" + Arrays.toString(aGeneratedParts) +
                '}';
    }
}
