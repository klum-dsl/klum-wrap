package com.blackbuild.klum.wrap.providers;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.stmt.Statement;

import static com.blackbuild.klum.common.CommonAstHelper.*;
import static com.blackbuild.klum.wrap.ast.WrapAstHelper.getWrappedTypeFor;
import static groovyjarjarasm.asm.Opcodes.ACC_FINAL;

public abstract class FieldHandler {

    protected final ElementFactory factory;
    protected final FieldNode field;

    public FieldHandler(FieldNode field, ElementFactory factory) {
        this.factory = factory;
        this.field = field;
    }

    public abstract Statement initializeWrapperFieldS();

    public abstract Statement getGetterCode();

    public void modifyField() {
        factory.modifyField();
        doModifyField();
    }

    protected void doModifyField() {
        field.setModifiers(field.getModifiers() | ACC_FINAL);
    }

    public static FieldHandler forField(FieldNode field) {
        ClassNode wrappedType = getWrappedTypeFor(field.getType());

        if (wrappedType != null)
            return new SingleField(field, new BasicWrap(field.getType()));

        if (isCollectionOrMap(field.getType())) {
            ClassNode elementType = getElementType(field);

            if (isCollection(field.getType()))
                return new CollectionField(field, new BasicWrap(elementType));
            if (isMap(field.getType()))
                return new MapField(field, new BasicWrap(elementType));
        }

        return null;
    }

}
