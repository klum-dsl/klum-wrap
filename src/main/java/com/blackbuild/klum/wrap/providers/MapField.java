package com.blackbuild.klum.wrap.providers;

import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.GenericsType;
import org.codehaus.groovy.ast.stmt.ForStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.ast.tools.GenericsUtils;

import java.util.Map;

import static com.blackbuild.klum.wrap.ast.KlumWrapTransformation.DELEGATE_FIELD_NAME;
import static org.codehaus.groovy.ast.ClassHelper.make;
import static org.codehaus.groovy.ast.tools.GeneralUtils.*;

public class MapField extends MultipleField {

    public MapField(FieldNode field, ElementFactory factory) {
        super(field, factory);
    }

    @Override
    public Statement initializeWrapperFieldS() {
        return new ForStatement(
                param(GenericsUtils.makeClassSafeWithGenerics(make(Map.Entry.class), field.getType().getGenericsTypes()[0], new GenericsType(wrappedType)), "$next"),
                propX(varX(DELEGATE_FIELD_NAME), field.getName()),
                stmt(callX(varX(field.getName()), "put", args(propX(varX("$next"), "key"), factory.fromDelegateX(propX(varX("$next"), "value")))))
        );
    }

}
