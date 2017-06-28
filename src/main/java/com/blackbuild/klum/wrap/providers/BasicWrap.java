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

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.Expression;

import static com.blackbuild.klum.wrap.ast.WrapAstHelper.getWrappedTypeFor;
import static org.codehaus.groovy.ast.tools.GeneralUtils.ctorX;

public class BasicWrap extends ElementFactory {

    private final ClassNode type;

    public BasicWrap(ClassNode type) {
        this.type = type;
    }

    static boolean enrichIfValid(FieldHandler result) {
        ClassNode wrappedType = getWrappedTypeFor(result.getElementType());
        if (wrappedType != null) {
            result.setFactory(new BasicWrap(result.getElementType()));
            return true;
        }
        return false;
    }

    @Override
    public void modifyField() {

    }

    @Override
    public Expression fromDelegateX(Expression source) {
        return ctorX(type, source);
    }

}
