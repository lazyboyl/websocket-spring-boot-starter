package com.github.lazyboyl.websocket.util;

import com.github.lazyboyl.websocket.constant.ClassType;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * @author linzf
 * @since 2020/7/16
 * 类描述：
 */
public class ClassUtil {

    /**
     * 功能描述： 获取当前的类型的值
     * @param typeName field的类型
     * @param envVal 当前需要注入的值
     * @return 转义以后的值
     */
    public static Object getFieldValue(String typeName, String envVal) {
        switch (ClassType.getByClassName(typeName)) {
            case StringType:
                return envVal;
            case LongType:
                return Long.parseLong(envVal);
            case longType:
                return Long.parseLong(envVal);
            case BooleanType:
                if ("true".equals(envVal)) {
                    return true;
                } else {
                    return false;
                }
            case booleanType:
                if ("true".equals(envVal)) {
                    return true;
                } else {
                    return false;
                }
            case FloatType:
                return Float.parseFloat(envVal);
            case floatType:
                return Float.parseFloat(envVal);
            case DoubleType:
                return Double.parseDouble(envVal);
            case doubleType:
                return Double.parseDouble(envVal);
            case IntegerType:
                return Integer.parseInt(envVal);
            case intType:
                return Integer.parseInt(envVal);
            case MapType:
                return JsonUtils.jsonToMap(envVal.replaceAll("\"", "").replaceAll("'", "\""));
            case ListType:
                return Arrays.stream(envVal.split(",")).collect(Collectors.toList());
            default:
                return null;
        }
    }

    /**
     * 功能描述： 根据类的全路径来获取class
     *
     * @param classFullName 类的全路径
     * @return 返回相应的class对象
     */
    public static Class getClass(String classFullName) {
        String[] classSplit = classFullName.split("\\.");
        String packageName = "";
        for (int i = 0; i < classSplit.length - 1; i++) {
            if (i == 0) {
                packageName = classSplit[i];
            } else {
                packageName = packageName + "." + classSplit[i];
            }
        }
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    return Class.forName(classFullName);
                } else if ("jar".equals(protocol)) {
                    JarFile jar;
                    try {
                        // 获取jar
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        // 从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        // 同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // 如果是以/开头的
                            if (name.charAt(0) == '/') {
                                // 获取后面的字符串
                                name = name.substring(1);
                            }
                            // 如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                // 如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    // 获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                // 如果可以迭代下去 并且是一个包
                                if ((idx != -1) || true) {
                                    // 如果是一个.class文件 而且不是目录
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        // 去掉后面的".class" 获取真正的类名
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            if ((packageName + '.' + className).equals(classFullName)) {
                                                return Class.forName(packageName + '.' + className);
                                            }
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
