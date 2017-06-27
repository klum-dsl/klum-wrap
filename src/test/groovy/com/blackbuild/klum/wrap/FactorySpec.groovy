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
