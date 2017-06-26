package com.blackbuild.klum.wrap.providers

import com.blackbuild.klum.wrap.AbstractAstSpec
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.FieldNode

class FieldHandlerSpec extends AbstractAstSpec {

    FieldHandler handler

    def 'FieldHandler.create'() {
        given:
        String code = """
class Foo {
    String name
}

@Wrap(Foo)
class Bar {
    BigName name
}

@Wrap(String)
class BigName {}
"""
        withClassCode(code)
        FieldNode field = (nodes[2] as ClassNode).getField("name")

        when:
        handler = FieldHandler.forField(field)

        then:
        handler instanceof SingleField
        handler.factory instanceof BasicWrap

    }

}
