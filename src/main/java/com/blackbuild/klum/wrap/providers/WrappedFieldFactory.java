package com.blackbuild.klum.wrap.providers;

import com.blackbuild.klum.common.CommonAstHelper;
import com.blackbuild.klum.wrap.WrappedField;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.transform.AbstractASTTransformation;

import java.util.List;

import static com.blackbuild.klum.wrap.ast.WrapAstHelper.getWrappedTypeFor;
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
            return true;

        } else if (member instanceof ClosureExpression){
            // not yet
        }
        return false;
    }

    @Override
    public void modifyField() {

    }
}
