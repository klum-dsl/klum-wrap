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
package com.blackbuild.klum.wrap.ast;

import com.blackbuild.klum.wrap.Wrap;
import groovy.lang.Delegate;
import org.codehaus.groovy.ast.*;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.AbstractASTTransformation;
import org.codehaus.groovy.transform.DelegateASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;

import static org.codehaus.groovy.ast.tools.GeneralUtils.*;

@GroovyASTTransformation
public class KlumWrapTransformation extends AbstractASTTransformation {

    public static final ClassNode WRAP_ANNOTATION = ClassHelper.make(Wrap.class);
    public static final String DELEGATE_FIELD_NAME = "delegate";
    public static final ClassNode DELEGATE_ANNOTATION = ClassHelper.make(Delegate.class);
    private ClassNode annotatedClass;
    private AnnotationNode wrapAnnotation;
    private ClassNode delegateClass;
    private FieldNode delegateField;

    @Override
    public void visit(ASTNode[] nodes, SourceUnit source) {
        init(nodes, source);

        annotatedClass = (ClassNode) nodes[1];
        wrapAnnotation = (AnnotationNode) nodes[0];
        delegateClass = getMemberClassValue(wrapAnnotation, "value");

        createDelegateField();
        createConstructor();

        // TODO: delegate methods for explicit fields

        delegateToDelegate();
    }

    private void delegateToDelegate() {
        ASTNode[] astNodes = new ASTNode[] { delegateField.getAnnotations(DELEGATE_ANNOTATION).get(0), delegateField};
        new DelegateASTTransformation().visit(astNodes, sourceUnit);
    }

    private void createConstructor() {
        annotatedClass.addConstructor(
                ACC_PUBLIC,
                params(param(delegateClass, "del")),
                new ClassNode[0],
                assignS(propX(varX("this"), DELEGATE_FIELD_NAME), varX("del"))
        );
    }

    private void createDelegateField() {
        delegateField = annotatedClass.addField(DELEGATE_FIELD_NAME, ACC_PROTECTED | ACC_FINAL, delegateClass, null);
        AnnotationNode delegateAnnotation = new AnnotationNode(DELEGATE_ANNOTATION);
        delegateAnnotation.setMember("parameterAnnotations", constX(true));
        delegateAnnotation.setMember("methodAnnotations", constX(true));
        delegateField.addAnnotation(delegateAnnotation);
    }
}
