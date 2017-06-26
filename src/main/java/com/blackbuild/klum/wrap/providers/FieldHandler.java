package com.blackbuild.klum.wrap.providers;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.stmt.Statement;

import static groovyjarjarasm.asm.Opcodes.ACC_FINAL;

public abstract class FieldHandler {

    protected ElementFactory factory;
    protected final FieldNode field;

    public FieldHandler(FieldNode field) {
        this.field = field;
    }

    public ElementFactory getFactory() {
        return factory;
    }

    public void setFactory(ElementFactory factory) {
        this.factory = factory;
    }

    public boolean isValid() {
        return factory != null;
    }

    public abstract ClassNode getElementType();

    public abstract Statement initializeWrapperFieldS();

    public abstract Statement getGetterCode();

    public void modifyField() {
        factory.modifyField();
        doModifyField();
    }

    protected void doModifyField() {
        field.setModifiers(field.getModifiers() | ACC_FINAL);
    }

}
