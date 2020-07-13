package com.github.lazyboyl.websocket.constant;

/**
 * 类描述： 类的类型
 *
 * @author linzef
 * @since 2020-07-08
 */
public enum ClassType {

    StringType("java.lang.String"),
    LongType("java.lang.Long"),
    BooleanType("java.lang.Boolean"),
    IntegerType("java.lang.Integer"),
    FloatType("java.lang.Float"),
    DoubleType("java.lang.Double"),
    longType("long"),
    intType("int"),
    booleanType("boolean"),
    floatType("float"),
    doubleType("double"),
    ListType("java.util.List"),
    MapType("java.util.Map");

    private String className;

    ClassType(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public static ClassType getByClassName(String value) {
        for (ClassType classType : values()) {
            if (classType.getClassName().equals(value)) {
                return classType;
            }
        }
        return null;
    }

}
