package com.blackbuild.klum.wrap.ast;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.stmt.Statement;

import static com.blackbuild.klum.common.CommonAstHelper.isCollection;
import static com.blackbuild.klum.common.CommonAstHelper.isMap;
import static com.blackbuild.klum.wrap.ast.WrapAstHelper.getWrappedTypeFor;
import static groovyjarjarasm.asm.Opcodes.ACC_FINAL;

/**
 * Generator for fields in the wrapper.
 */
abstract class FieldProvider {

    protected final FieldNode field;

    FieldProvider(FieldNode field) {
        this.field = field;
    }

    abstract Statement getInitializationCode();

    abstract Statement getGetterCode();

    void modifyField() {
        field.setModifiers(field.getModifiers() | ACC_FINAL);
    }

    static FieldProvider forField(FieldNode field) {
        ClassNode wrappedType = getWrappedTypeFor(field.getType());

        if (wrappedType != null)
            return new SingleWrappedField(field);

        if (isCollection(field.getType()))
            return new WrappedCollectionField(field);
        if (isMap(field.getType()))
            return new WrappedMapField(field);


        return null;
    }


}
