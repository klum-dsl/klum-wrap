package com.blackbuild.klum.wrap.providers;

import org.codehaus.groovy.ast.FieldNode;

import static com.blackbuild.klum.common.CommonAstHelper.isCollection;
import static com.blackbuild.klum.common.CommonAstHelper.isMap;

public class FieldHandlerFactory {

    private FieldHandlerFactory() {}

    public static FieldHandler createFor(FieldNode field) {

        FieldHandler result = getHandlerForCardinality(field);

        if (BasicWrap.enrichIfValid(result))
            return result;

        return null;
    }

    private static FieldHandler getHandlerForCardinality(FieldNode field) {
        if (isCollection(field.getType()))
            return new CollectionField(field);
        if (isMap(field.getType()))
            return new MapField(field);
        return new SingleField(field);
    }
}
