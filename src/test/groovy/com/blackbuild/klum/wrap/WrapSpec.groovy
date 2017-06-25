/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2017 Stephan Pauxberger
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.blackbuild.klum.wrap

import com.blackbuild.klum.wrap.ast.KlumWrapTransformation

import static groovyjarjarasm.asm.Opcodes.ACC_FINAL
import static groovyjarjarasm.asm.Opcodes.ACC_PUBLIC

class WrapSpec extends AbstractWrapSpec {

    def "Delegate class has a single parameter constructor"() {
        when:
        createClass '''
package pk

@Wrap(String)
class DecoString {}
'''
        then:
        clazz.declaredConstructors.size() == 1
        clazz.getDeclaredConstructor(String).modifiers & ACC_PUBLIC
    }

    def "Delegate field is final"() {
        when:
        createClass '''
package pk

@Wrap(String)
class DecoString {}
'''
        then:
        clazz.getDeclaredField(KlumWrapTransformation.DELEGATE_FIELD_NAME).modifiers & ACC_FINAL
    }

    def "Wrap delegates to delegate"() {
        given:
        createClass '''
package pk

@Wrap(String)
class DecoString {}
'''
        when:
        instance = clazz.newInstance('Bla')

        then:
        instance.length() == 3
    }

    def "Inner object is wrapped"() {
        given:
        createClass '''
package pk

class Config {
    String name
}

@Wrap(Config)
class EnhancedConfig {
    BigName name
}

@Wrap(String)
class BigName {
}
'''

        def model = getClass("pk.Config").newInstance()
        model.name = "bla"

        when:
        def wrap = getClass("pk.EnhancedConfig").newInstance(model)

        then:
        noExceptionThrown()
        wrap.name.class.name == "pk.BigName"
    }

    def "Setters are not delegated"() {
        given:
        createClass '''
package pk

class Config {
    String name
}

@Wrap(Config)
class EnhancedConfig {
}
'''

        def model = getClass("pk.Config").newInstance()
        model.name = "bla"
        def wrap = getClass("pk.EnhancedConfig").newInstance(model)

        when:
        wrap.name = "can't override"

        then:
        thrown(ReadOnlyPropertyException)
    }

    def "Inner list is wrapped"() {
        given:
        createClass '''
package pk

class Config {
    List<String> names = ["bla", "blub"]
}

@Wrap(Config)
class EnhancedConfig {
    List<BigName> names
}

@Wrap(String)
class BigName {
    String getValue() {
        delegate.toUpperCase()
    }
}
'''

        def model = getClass("pk.Config").newInstance()

        when:
        def wrap = getClass("pk.EnhancedConfig").newInstance(model)

        then:
        noExceptionThrown()
        wrap.names[0].class.name == "pk.BigName"
        wrap.names.collect { it.value } == ["BLA", "BLUB"]
        wrap.names.collect { it.length() } == [3, 4]
    }

    def "Inner map is wrapped"() {
        given:
        createClass '''
package pk

class Config {
    Map<String, String> names = [a:"bla", b:"blub"]
}

@Wrap(Config)
class EnhancedConfig {
    Map<String, BigName> names
}

@Wrap(String)
class BigName {
    String getValue() {
        delegate.toUpperCase()
    }
}
'''

        def model = getClass("pk.Config").newInstance()

        when:
        def wrap = getClass("pk.EnhancedConfig").newInstance(model)

        then:
        noExceptionThrown()
        wrap.names.a.class.name == "pk.BigName"
        wrap.names.a.value == "BLA"
        wrap.names.a.length() == 3
    }




}
