package com.github.curiousoddman.rgxgen.iterators.suppliers;

/* **************************************************************************
   Copyright 2019 Vladislavs Varslavans

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
/* **************************************************************************/

import com.github.curiousoddman.rgxgen.iterators.ReferenceIterator;
import com.github.curiousoddman.rgxgen.iterators.StringIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class GroupIteratorSupplier implements Supplier<StringIterator> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupIteratorSupplier.class);

    private final Supplier<StringIterator>              aIteratorSupplier;
    private final Map<Integer, List<ReferenceIterator>> aReferenceIteratorMap;
    private final Map<Integer, StringIterator>          aGroupIteratorsMap;
    private final int                                   aIndex;

    public GroupIteratorSupplier(Supplier<StringIterator> iteratorSupplier, Map<Integer, List<ReferenceIterator>> referenceIteratorMap, Map<Integer, StringIterator> groupIteratorsMap, int index) {
        aIteratorSupplier = iteratorSupplier;
        aReferenceIteratorMap = referenceIteratorMap;
        aGroupIteratorsMap = groupIteratorsMap;
        aIndex = index;
        LOGGER.trace("Creating idx {}\n\tsrc: {}\n\trefs: {}\n\tgrps: {}", index, iteratorSupplier, referenceIteratorMap, groupIteratorsMap);
    }

    @Override
    public StringIterator get() {
        LOGGER.trace("Getting idx {}\n\trefs: {}\n\tgrps: {}", aIndex, aReferenceIteratorMap, aGroupIteratorsMap);
        final StringIterator stringIterator = aIteratorSupplier.get();
        aGroupIteratorsMap.put(aIndex, stringIterator);
        final List<ReferenceIterator> orDefault = aReferenceIteratorMap.getOrDefault(aIndex, Collections.emptyList());
        LOGGER.debug("ReferenceIterators to connect: {}", orDefault);
        for (ReferenceIterator referenceIterator : orDefault) {
            LOGGER.debug("GroupRef[{}] connecting to group {} ", aIndex, stringIterator);
            referenceIterator.setOther(stringIterator);
        }
        return stringIterator;
    }

    @Override
    public String toString() {
        return "GroupIteratorSupplier{" +
                "aIteratorSupplier=" + aIteratorSupplier +
                ", aReferenceIteratorMap=" + aReferenceIteratorMap +
                ", aGroupIterators=" + aGroupIteratorsMap +
                ", aIndex=" + aIndex +
                '}';
    }
}
