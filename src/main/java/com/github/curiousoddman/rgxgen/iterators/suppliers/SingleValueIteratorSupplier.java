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

import com.github.curiousoddman.rgxgen.iterators.SingleValueIterator;
import com.github.curiousoddman.rgxgen.iterators.StringIterator;

import java.util.function.Supplier;

public class SingleValueIteratorSupplier implements Supplier<StringIterator> {

    private final String aValue;

    public SingleValueIteratorSupplier(String value) {
        aValue = value;
    }

    @Override
    public StringIterator get() {
        return new SingleValueIterator(aValue);
    }

    @Override
    public String toString() {
        return "SingleValueIteratorSupplier{" + aValue + '}';
    }
}
