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

import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.Expression;

import java.util.List;

import static com.blackbuild.klum.wrap.ast.KlumWrapTransformation.WRAP_ANNOTATION;
import static groovy.transform.Undefined.isUndefined;

public class WrapAstHelper {

    private WrapAstHelper() {}

    public static ClassNode getWrappedTypeFor(ClassNode fieldType) {
        if (fieldType == null)
            return null;
        List<AnnotationNode> annotations = fieldType.getAnnotations(WRAP_ANNOTATION);
        if (annotations.isEmpty())
            return null;

        return getMemberClassValue(annotations.get(0), "value");
    }

    static ClassNode getMemberClassValue(AnnotationNode node, String name) {
        final Expression member = node.getMember(name);
        if (member instanceof ClassExpression && !isUndefined(member.getType()))
            return member.getType();
        return null;
    }

}
