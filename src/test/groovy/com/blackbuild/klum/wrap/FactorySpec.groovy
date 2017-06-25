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
        return inner.chars()
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

}
