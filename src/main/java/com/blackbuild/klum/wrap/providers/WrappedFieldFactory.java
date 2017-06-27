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

import com.blackbuild.klum.wrap.WrappedField;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.transform.AbstractASTTransformation;

import java.util.List;

import static org.codehaus.groovy.ast.ClassHelper.make;

public abstract class WrappedFieldFactory extends ElementFactory {

    public static final ClassNode WRAPPED_FIELD_ANNOTATION = make(WrappedField.class);

    public static boolean enrichIfValid(FieldHandler result) {
        List<AnnotationNode> annotations = result.getField().getAnnotations(WRAPPED_FIELD_ANNOTATION);
        if (annotations.isEmpty())
            return false;

        AnnotationNode annotation = annotations.get(0);

        Expression member = annotation.getMember("factory");
        String methodName = AbstractASTTransformation.getMemberStringValue(annotation, "method", "create");

        if (member instanceof ClassExpression) {
            ClassNode memberType = member.getType();
            result.setFactory(new ClassBasedFactory(memberType, methodName));
        } else if (member instanceof ClosureExpression){
            result.setFactory(new ClosureBasedFactory((ClosureExpression) member));
        }
        return result.getFactory() != null;
    }

    @Override
    public void modifyField() {

    }
}
