package com.github.curiousoddman.rgxgen.parsing;

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

import com.github.curiousoddman.rgxgen.nodes.Node;
import com.github.curiousoddman.rgxgen.validation.Validator;

import java.util.Optional;

/**
 * Interface for the parser/nodes builder.
 */
public interface NodeTreeBuilder {

    /**
     * @return Root node for the parsed pattern
     */
    Node get();

    /**
     * @return list of validators that should be applied to determine if the generated text satisfies them all.
     */
    Optional<Validator> getValidator();
}
