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

class FactorySpec extends AbstractWrapSpec {

    def "Create member wrapper via explicit factory"() {
        given:
        createClass '''
package pk

class Config {
    String name
}

@Wrap(Config)
class EnhancedConfig {
    @WrappedField(factory = CharArrayFactory)
    char[] name
}

class CharArrayFactory {
    static char[] create(String inner) {
            return inner.chars
    }
}

'''

        def model = getClass("pk.Config").newInstance()
        model.name = "bla"

        when:
        def wrap = getClass("pk.EnhancedConfig").newInstance(model)

        then:
        noExceptionThrown()
        wrap.name instanceof char[]
    }

    def "Create member wrapper via explicit factory and custom name"() {
        given:
        createClass '''
package pk

class Config {
    String name
}

@Wrap(Config)
class EnhancedConfig {
    @WrappedField(factory = CharArrayFactory, method = 'make')
    char[] name
}

class CharArrayFactory {
    static char[] make(String inner) {
            return inner.chars
    }
}

'''

        def model = getClass("pk.Config").newInstance()
        model.name = "bla"

        when:
        def wrap = getClass("pk.EnhancedConfig").newInstance(model)

        then:
        noExceptionThrown()
        wrap.name instanceof char[]
    }

    def "Create member wrapper via factory closure"() {
        given:
        createClass '''
package pk

class Config {
    String name
}

@Wrap(Config)
class EnhancedConfig {
    @WrappedField(factory = { it.chars })
    char[] name
}
'''

        def model = getClass("pk.Config").newInstance()
        model.name = "bla"

        when:
        def wrap = getClass("pk.EnhancedConfig").newInstance(model)

        then:
        noExceptionThrown()
        wrap.name instanceof char[]
    }

    def "Create member wrapper via factory closure and explicit variable"() {
        given:
        createClass '''
package pk

class Config {
    String name
}

@Wrap(Config)
class EnhancedConfig {
    @WrappedField(factory = { inner -> inner.chars })
    char[] name
}
'''

        def model = getClass("pk.Config").newInstance()
        model.name = "bla"

        when:
        def wrap = getClass("pk.EnhancedConfig").newInstance(model)

        then:
        noExceptionThrown()
        wrap.name instanceof char[]
    }

}
