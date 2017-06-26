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
