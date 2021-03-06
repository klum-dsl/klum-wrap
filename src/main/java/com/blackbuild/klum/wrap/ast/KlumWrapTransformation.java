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
import com.blackbuild.klum.wrap.providers.FieldHandler;
import com.blackbuild.klum.wrap.providers.FieldHandlerFactory;
import groovy.lang.Delegate;
import org.codehaus.groovy.ast.*;
import org.codehaus.groovy.ast.expr.ListExpression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.classgen.Verifier;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.AbstractASTTransformation;
import org.codehaus.groovy.transform.DelegateASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;

import java.util.Collection;

import static org.codehaus.groovy.ast.ClassHelper.makeWithoutCaching;
import static org.codehaus.groovy.ast.tools.GeneralUtils.*;

@GroovyASTTransformation
public class KlumWrapTransformation extends AbstractASTTransformation {

    public static final ClassNode WRAP_ANNOTATION = ClassHelper.make(Wrap.class);
    public static final String DELEGATE_FIELD_NAME = "delegate";
    public static final ClassNode DELEGATE_ANNOTATION = ClassHelper.make(Delegate.class);
    public static final ClassNode COLLECTION_TYPE = makeWithoutCaching(Collection.class);

    private ClassNode annotatedClass;
    private AnnotationNode wrapAnnotation;
    private ClassNode delegateClass;
    private FieldNode delegateField;
    private ListExpression delegateExcludes;
    // private List<PropertyNode>

    @Override
    public void visit(ASTNode[] nodes, SourceUnit source) {
        init(nodes, source);

        if (!(nodes[1] instanceof ClassNode))
            return;

        annotatedClass = (ClassNode) nodes[1];
        wrapAnnotation = (AnnotationNode) nodes[0];
        delegateClass = getMemberClassValue(wrapAnnotation, "value");

        createDelegateField();
        createConstructor();

        delegateToDelegate();
        removeDelegatedSetters();
    }

    private void removeDelegatedSetters() {
        for (PropertyNode delegatedProperty : getAllProperties(delegateClass)) {
            MethodNode setter = annotatedClass.getSetterMethod("set" + Verifier.capitalize(delegatedProperty.getName()));
            if (setter != null)
                annotatedClass.removeMethod(setter);
        }
    }

    private void createDelegateField() {
        delegateField = annotatedClass.addField(DELEGATE_FIELD_NAME, ACC_PROTECTED | ACC_FINAL, delegateClass, null);
        AnnotationNode delegateAnnotation = new AnnotationNode(DELEGATE_ANNOTATION);
        delegateAnnotation.setMember("parameterAnnotations", constX(true));
        delegateAnnotation.setMember("methodAnnotations", constX(true));
        delegateExcludes = new ListExpression();
        delegateAnnotation.setMember("excludes", delegateExcludes);
        delegateField.addAnnotation(delegateAnnotation);
    }

    private void createConstructor() {
        BlockStatement constructorBody = block(assignS(propX(varX("this"), DELEGATE_FIELD_NAME), varX(DELEGATE_FIELD_NAME)));

        for (PropertyNode property : annotatedClass.getProperties()) {
            handleProperty(property, constructorBody);
        }

        annotatedClass.addConstructor(
                ACC_PUBLIC,
                params(param(delegateClass, DELEGATE_FIELD_NAME)),
                new ClassNode[0],
                constructorBody
        );
    }

    private void handleProperty(PropertyNode property, BlockStatement constructorBody) {

        FieldHandler handler = FieldHandlerFactory.createFor(property.getField());

        if (handler == null)
            return;

        constructorBody.addStatement(handler.initializeWrapperFieldS());

        annotatedClass.addMethod(
                "get" + Verifier.capitalize(property.getName()),
                ACC_PUBLIC,
                property.getField().getType(),
                Parameter.EMPTY_ARRAY,
                ClassNode.EMPTY_ARRAY,
                handler.getGetterCode()
        );

        handler.modifyField();
    }

    private void delegateToDelegate() {
        ASTNode[] astNodes = new ASTNode[] { delegateField.getAnnotations(DELEGATE_ANNOTATION).get(0), delegateField };
        new DelegateASTTransformation().visit(astNodes, sourceUnit);
    }

}
