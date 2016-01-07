/**
 * Copyright (C) 2012 Red Hat, Inc. (jdcasey@commonjava.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.commonjava.rwx.binding.testutil;

import static org.commonjava.rwx.binding.testutil.recipe.RecipeEventUtils.parameter;

import org.commonjava.rwx.binding.anno.Request;
import org.commonjava.rwx.binding.mapping.ArrayMapping;
import org.commonjava.rwx.binding.mapping.FieldBinding;
import org.commonjava.rwx.binding.mapping.Mapping;
import org.commonjava.rwx.estream.model.Event;
import org.commonjava.rwx.estream.model.RequestEvent;
import org.commonjava.rwx.vocab.ValueType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Request( method = "getUser" )
public class InheritedPersonRequest
    extends SimplePersonRequest
{
    @Override
    public Map<Class<?>, Mapping<?>> recipes()
    {
        final Map<Class<?>, Mapping<?>> recipes = new HashMap<Class<?>, Mapping<?>>();

        final ArrayMapping recipe = new ArrayMapping( InheritedPersonRequest.class, new Integer[0] );
        recipe.addFieldBinding( 0, new FieldBinding( "userId", String.class ) );
        recipes.put( InheritedPersonRequest.class, recipe );

        return recipes;
    }

    @Override
    public List<Event<?>> events()
    {
        final List<Event<?>> check = new ArrayList<Event<?>>();

        check.addAll( Arrays.asList( new RequestEvent( true ), new RequestEvent( "getUser" ) ) );

        check.addAll( parameter( 0, "foo", ValueType.STRING ) );
        check.add( new RequestEvent( false ) );

        return check;
    }

}
