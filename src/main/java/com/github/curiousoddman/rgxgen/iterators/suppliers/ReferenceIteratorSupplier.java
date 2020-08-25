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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ReferenceIteratorSupplier implements Supplier<StringIterator> {
    private final Map<Integer, List<ReferenceIterator>> aReferenceIteratorMap;
    private final Map<Integer, StringIterator>          aGroupIteratorsMap;
    private final int                                   aIndex;

    public ReferenceIteratorSupplier(Map<Integer, List<ReferenceIterator>> referenceIteratorMap, Map<Integer, StringIterator> groupIteratorsMap, int index) {
        aReferenceIteratorMap = referenceIteratorMap;
        aGroupIteratorsMap = groupIteratorsMap;
        aIndex = index;
    }

    @Override
    public StringIterator get() {
        ReferenceIterator referenceIterator = new ReferenceIterator();
        final StringIterator stringIterator = aGroupIteratorsMap.get(aIndex);
        if (stringIterator != null) {
            referenceIterator.setOther(stringIterator);
        }

        aReferenceIteratorMap.computeIfAbsent(aIndex, i -> new ArrayList<>())
                             .add(referenceIterator);

        return referenceIterator;
    }

    @Override
    public String toString() {
        return "ReferenceIteratorSupplier{" +
                "aReferenceIteratorMap=" + aReferenceIteratorMap +
                ", aGroupIteratorsMap=" + aGroupIteratorsMap +
                ", aIndex=" + aIndex +
                '}';
    }
}
