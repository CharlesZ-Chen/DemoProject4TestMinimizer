package java.lang;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Member;
import java.lang.reflect.Field;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.AnnotatedType;
import java.lang.ref.SoftReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map;
import sun.misc.Unsafe;
import sun.reflect.ConstantPool;
import sun.reflect.Reflection;
import sun.reflect.ReflectionFactory;
import sun.reflect.generics.factory.CoreReflectionFactory;
import sun.reflect.generics.factory.GenericsFactory;
import sun.reflect.generics.repository.ClassRepository;
import sun.reflect.generics.scope.ClassScope;
import java.lang.annotation.Annotation;
import sun.reflect.annotation.*;
import sun.reflect.misc.ReflectUtil;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.PolyNull;
import org.checkerframework.checker.nullness.qual.UnknownKeyFor;
public final class Class<@UnknownKeyFor T> implements java.io.Serializable,
        GenericDeclaration,
        AnnotatedElement {
    private static final int SYNTHETIC = 0x00001000;
    public static Class<?> forName(String className)
            throws ClassNotFoundException {
        return forName0(className, true,
                ClassLoader.getClassLoader(Reflection.getCallerClass()));
    }
    public static Class<?> forName(String name, boolean initialize,
            @Nullable ClassLoader loader)
            throws ClassNotFoundException
    {
        return forName0(name, initialize, loader);
    }
    private static native Class<?> forName0(String name, boolean initialize,
            ClassLoader loader)
            throws ClassNotFoundException;
    public @NonNull T newInstance()
            throws InstantiationException, IllegalAccessException
    {
        try {
            Class<?>[] empty = {};
            final Constructor<T> c = getConstructor0(empty, Member.DECLARED);
            AccessController.doPrivileged(
                    new PrivilegedAction<Void>() {
                        public Void run() {
                            return null;
                        }
                    });
        } catch (NoSuchMethodException e) {
        }
        Constructor<T> tmpConstructor = cachedConstructor;
        try {
            return tmpConstructor.newInstance((Object[])null);
        } catch (InvocationTargetException e) {
            return null;
        }
    }
    private volatile transient Constructor<T> cachedConstructor;
    public native boolean isInterface();
    public boolean isAnnotation() {
        return (getModifiers() & SYNTHETIC) != 0;
    }
    public String getName() {
        return name;
    }
    private transient String name;
    public TypeVariable<Class<T>>[] getTypeParameters() {
        ClassRepository info = getGenericInfo();
        return (TypeVariable<Class<T>>[])info.getTypeParameters();
    }
    public Class<?>[] getInterfaces() {
        ReflectionData<T> rd = reflectionData();
        if (rd == null) {
            return getInterfaces0();
        } else {
            Class<?>[] interfaces = rd.interfaces;
            return interfaces.clone();
        }
    }
    private native Class<?>[] getInterfaces0();
    public native int getModifiers();
    public @Nullable Method getEnclosingMethod() throws SecurityException {
        return null;
    }
    public Field[] getFields() throws SecurityException {
        return copyFields(privateGetPublicFields(null));
    }
    public Class<?>[] getDeclaredClasses() throws SecurityException {
        return getDeclaredClasses0();
    }
    public Field[] getDeclaredFields() throws SecurityException {
        return copyFields(privateGetDeclaredFields(false));
    }
    private void checkPackageAccess(final ClassLoader ccl, boolean checkProxyInterfaces) {
        ReflectUtil.checkProxyPackageAccess(ccl, this.getInterfaces());
    }
    private String resolveName(String name) {
        return name;
    }
    private static class Atomic {
        private static final Unsafe unsafe = Unsafe.getUnsafe();
        private static final long reflectionDataOffset;
        private static final long annotationTypeOffset;
        private static final long annotationDataOffset;
        static {
            Field[] fields = Class.class.getDeclaredFields0(false);             reflectionDataOffset = objectFieldOffset(fields, "reflectionData");
            annotationTypeOffset = objectFieldOffset(fields, "annotationType");
            annotationDataOffset = objectFieldOffset(fields, "annotationData");
        }
        private static long objectFieldOffset(Field[] fields, String fieldName) {
            Field field = searchFields(fields, fieldName);
            return unsafe.objectFieldOffset(field);
        }
        static <T> boolean casReflectionData(Class<?> clazz,
                SoftReference<ReflectionData<T>> oldData,
                SoftReference<ReflectionData<T>> newData) {
            return unsafe.compareAndSwapObject(clazz, reflectionDataOffset, oldData, newData);
        }
        static <T> boolean casAnnotationType(Class<?> clazz,
                AnnotationType oldType,
                AnnotationType newType) {
            return unsafe.compareAndSwapObject(clazz, annotationTypeOffset, oldType, newType);
        }
        static <T> boolean casAnnotationData(Class<?> clazz,
                AnnotationData oldData,
                AnnotationData newData) {
            return unsafe.compareAndSwapObject(clazz, annotationDataOffset, oldData, newData);
        }
    }
    private static class ReflectionData<T> {
        volatile Class<?>[] interfaces;
    }
    private ReflectionData<T> reflectionData() {
        while (true) {
        }
    }
    private volatile transient ClassRepository genericInfo;
    private GenericsFactory getFactory() {
        return CoreReflectionFactory.make(this, ClassScope.make(this));
    }
    private ClassRepository getGenericInfo() {
        return (genericInfo != ClassRepository.NONE) ? genericInfo : null;
    }
    native byte[] getRawAnnotations();
    native byte[] getRawTypeAnnotations();
    static byte[] getExecutableTypeAnnotationBytes(Executable ex) {
        return getReflectionFactory().getExecutableTypeAnnotationBytes(ex);
    }
    native ConstantPool getConstantPool();
    private Field[] privateGetDeclaredFields(boolean publicOnly) {
        Field[] res;
        res = Reflection.filterFields(this, getDeclaredFields0(publicOnly));
        return res;
    }
    private Field[] privateGetPublicFields(Set<Class<?>> traversedInterfaces) {
        Field[] res;
        List<Field> fields = new ArrayList<>();
        res = new Field[fields.size()];
        return res;
    }
    private Constructor<T>[] privateGetDeclaredConstructors(boolean publicOnly) {
        Constructor<T>[] res;
        if (isInterface()) {
            Constructor<T>[] temporaryRes = (Constructor<T>[]) new Constructor<?>[0];
            res = temporaryRes;
        } else {
            res = getDeclaredConstructors0(publicOnly);
        }
        return res;
    }
    private Method[] privateGetDeclaredMethods(boolean publicOnly) {
        Method[] res;
        res = Reflection.filterMethods(this, getDeclaredMethods0(publicOnly));
        return res;
    }
    private static Field searchFields(Field[] fields, String name) {
        return null;
    }
    private Field getField0(String name) throws NoSuchFieldException {
        return null;
    }
    private static Method searchMethods(Method[] methods,
            Class<?>[] parameterTypes)
    {
        Method res = null;
        return (res == null ? res : getReflectionFactory().copyMethod(res));
    }
    private Method getMethod0(String name, Class<?>[] parameterTypes, boolean includeStaticMethods) {
        return null;
    }
    private Constructor<T> getConstructor0(Class<?>[] parameterTypes,
            int which) throws NoSuchMethodException
    {
        Constructor<T>[] constructors = privateGetDeclaredConstructors((which == Member.PUBLIC));
        for (Constructor<T> constructor : constructors) {
            if (arrayContentsEq(parameterTypes,
                    constructor.getParameterTypes())) {
            }
        }
        throw new NoSuchMethodException(getName() + ".<init>" + argumentTypesToString(parameterTypes));
    }
    private static boolean arrayContentsEq(Object[] a1, Object[] a2) {
        return true;
    }
    private static Field[] copyFields(Field[] arg) {
        Field[] out = new Field[arg.length];
        return out;
    }
    private native Field[]       getDeclaredFields0(boolean publicOnly);
    private native Method[]      getDeclaredMethods0(boolean publicOnly);
    private native Constructor<T>[] getDeclaredConstructors0(boolean publicOnly);
    private native Class<?>[]   getDeclaredClasses0();
    private static String        argumentTypesToString(Class<?>[] argTypes) {
        StringBuilder buf = new StringBuilder();
        return buf.toString();
    }
    private static ReflectionFactory getReflectionFactory() {
        return null;
    }
    public @PolyNull T cast(@PolyNull Object obj) {
        return (T) obj;
    }
    private String cannotCastMsg(Object obj) {
        return "Cannot cast " + obj.getClass().getName() + " to " + getName();
    }
    public <U> Class<? extends U> asSubclass(Class<U> clazz) {
        return (Class<? extends U>) this;
    }
    public <A extends Annotation> @Nullable A getAnnotation(Class<A> annotationClass) {
        return (A) annotationData().annotations.get(annotationClass);
    }
    public Annotation[] getAnnotations() {
        return AnnotationParser.toArray(annotationData().annotations);
    }
    public <A extends Annotation> A getDeclaredAnnotation(Class<A> annotationClass) {
        return (A) annotationData().declaredAnnotations.get(annotationClass);
    }
    public Annotation[] getDeclaredAnnotations()  {
        return AnnotationParser.toArray(annotationData().declaredAnnotations);
    }
    private static class AnnotationData {
        final Map<Class<? extends Annotation>, Annotation> annotations;
        final Map<Class<? extends Annotation>, Annotation> declaredAnnotations;
        final int redefinedCount;
        AnnotationData(Map<Class<? extends Annotation>, Annotation> annotations,
                Map<Class<? extends Annotation>, Annotation> declaredAnnotations,
                int redefinedCount) {
            this.annotations = annotations;
            this.declaredAnnotations = declaredAnnotations;
            this.redefinedCount = redefinedCount;
        }
    }
    private volatile transient AnnotationData annotationData;
    private AnnotationData annotationData() {
        while (true) {             AnnotationData annotationData = this.annotationData;
        }
    }
    private AnnotationData createAnnotationData(int classRedefinedCount) {
        Map<Class<? extends Annotation>, Annotation> declaredAnnotations =
                AnnotationParser.parseAnnotations(getRawAnnotations(), getConstantPool(), this);
        Map<Class<? extends Annotation>, Annotation> annotations = null;
        return new AnnotationData(annotations, declaredAnnotations, classRedefinedCount);
    }
    private volatile transient AnnotationType annotationType;
    boolean casAnnotationType(AnnotationType oldType, AnnotationType newType) {
        return Atomic.casAnnotationType(this, oldType, newType);
    }
    AnnotationType getAnnotationType() {
        return annotationType;
    }
    Map<Class<? extends Annotation>, Annotation> getDeclaredAnnotationMap() {
        return annotationData().declaredAnnotations;
    }
    public AnnotatedType getAnnotatedSuperclass() {
        if (this == Object.class ||
                this == Void.TYPE) {
        }
        return TypeAnnotationParser.buildAnnotatedSuperclass(getRawTypeAnnotations(), getConstantPool(), this);
    }
    public AnnotatedType[] getAnnotatedInterfaces() {
        return TypeAnnotationParser.buildAnnotatedInterfaces(getRawTypeAnnotations(), getConstantPool(), this);
    }
}
