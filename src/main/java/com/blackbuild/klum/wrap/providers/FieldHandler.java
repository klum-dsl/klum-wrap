/**
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
package com.blackbuild.klum.wrap.providers;

import com.blackbuild.klum.common.CommonAstHelper;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.stmt.Statement;

import static com.blackbuild.klum.common.CommonAstHelper.getNullSafeMemberStringValue;
import static groovyjarjarasm.asm.Opcodes.ACC_FINAL;

public abstract class FieldHandler {

    private final AnnotationNode wrappedFieldAnnotation;
    protected ElementFactory factory;
    protected final FieldNode field;

    public FieldHandler(FieldNode field) {
        this.field = field;
        wrappedFieldAnnotation = CommonAstHelper.getAnnotation(field, WrappedFieldFactory.WRAPPED_FIELD_ANNOTATION);
    }

    public AnnotationNode getWrappedFieldAnnotation() {
        return wrappedFieldAnnotation;
    }

    public String getSourceFieldName() {
        return getNullSafeMemberStringValue(wrappedFieldAnnotation, "sourceField", field.getName());
    }

    public FieldNode getField() {
        return field;
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
