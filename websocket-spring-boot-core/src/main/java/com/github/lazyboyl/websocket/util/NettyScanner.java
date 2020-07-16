package com.github.lazyboyl.websocket.util;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author linzf
 * @since 2020/6/30
 * 类描述： 用于扫描netty注解的类
 */
public class NettyScanner {

    /**
     * 定义被扫描的类的集合
     */
    private Set<Class<?>> classes = new HashSet<>(256);


    /**
     * 功能描述： 找到所有实现了当前接口的类
     *
     * @param interfaceClass 接口类
     * @return 类的集合
     */
    public <A extends Annotation> Set<Class<?>> getInterfaceClasses(Class interfaceClass) {
        Set<Class<?>> interfaces = new HashSet<>();
        if (classes != null && classes.size() > 0) {
            for (Class<?> cls : classes) {
                Class[] is = cls.getInterfaces();
                for (Class i : is) {
                    if (i.getName().equals(interfaceClass.getName())) {
                        interfaces.add(cls);
                    }
                }
            }
        }
        return interfaces;
    }


    /**
     * 功能描述： 找到被某个注解所注解的类
     *
     * @param annotationClass 注解类
     * @return 类的集合
     */
    public <A extends Annotation> Set<Class<?>> getAnnotationClasses(Class annotationClass) {
        //找用了annotationClass注解的类
        Set<Class<?>> controllers = new HashSet<>();
        if (classes != null && classes.size() > 0) {
            for (Class<?> cls : classes) {
                if (cls.getAnnotation(annotationClass) != null) {
                    controllers.add(cls);
                }
            }
        }
        return controllers;
    }

    /**
     * 从包package中获取所有的Class
     *
     * @param packageName 需要扫描的包的名称
     * @return 返回扫描成功的类
     */
    public Set<Class<?>> initClasses(String packageName) {
        // 获取包的名字 并进行替换
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
                System.out.println("protocol:" + protocol);
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    addClass(classes, filePath, packageName);
                } else if ("jar".equals(protocol)) {
                    // 定义一个JarFile
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
                                            // 添加到classes
                                            classes.add(Class.forName(packageName + '.' + className));
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

    /**
     * @param classes     类的集合
     * @param filePath    类的路径
     * @param packageName 包名称
     * @throws Exception 出错信息
     */
    protected void addClass(Set<Class<?>> classes, String filePath, String packageName) throws Exception {
        File[] files = new File(filePath).listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                String path = f.getPath();
                addClass(classes, f.getPath(), packageName + "." + path.split("\\\\")[path.split("\\\\").length - 1]);
            } else if (f.getName().endsWith(".class")) {
                String fileName = f.getName();
                if (f.isFile()) {
                    String classsName = fileName.substring(0, fileName.lastIndexOf("."));
                    if (!packageName.isEmpty()) {
                        classsName = packageName + "." + classsName;
                    }
                    doAddClass(classes, classsName);
                }
            }
        }
    }

    /**
     * @param classes    类的集合
     * @param classsName 类的名称
     * @throws Exception
     */
    protected void doAddClass(Set<Class<?>> classes, final String classsName) throws Exception {
        ClassLoader classLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                return super.loadClass(name);
            }
        };
        classes.add(classLoader.loadClass(classsName));
    }


}
