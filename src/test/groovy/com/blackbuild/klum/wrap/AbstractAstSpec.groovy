package com.blackbuild.klum.wrap

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.ErrorCollector
import org.codehaus.groovy.control.SourceUnit
import spock.lang.Specification

class AbstractAstSpec extends Specification {

    List<ASTNode> nodes
    ErrorCollector errorCollector = Mock(ErrorCollector)
    SourceUnit sourceUnit = Stub(SourceUnit) {
        getErrorCollector() >> errorCollector
    }

    def withClassCode(String text) {
        def textWithImports = 'import com.blackbuild.klum.wrap.*\n' + text

        nodes = new AstBuilder().buildFromString(CompilePhase.INSTRUCTION_SELECTION, textWithImports)
    }



}