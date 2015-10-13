package com.nexacro.spring.data.support;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.nexacro.spring.data.convert.ConvertDefinition;
import com.nexacro.spring.data.convert.NexacroConvertException;
import com.nexacro.spring.data.support.NexacroTestUtil.StaticPropertyBean;
import com.nexacro.spring.data.support.bean.DefaultBean;
import com.nexacro.xapi.data.ColumnHeader;
import com.nexacro.xapi.data.ConstantColumnHeader;
import com.nexacro.xapi.data.DataSet;

import junit.framework.Assert;

/**
 * <pre>
 * Statements
 * </pre>
 *
 * @ClassName   : ListToDataSetConverterTest.java
 * @Description : 클래스 설명을 기술합니다.
 * @author Park SeongMin
 * @since 2015. 8. 4.
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 *     since          author              description
 *  ===========    =============    ===========================
 *  2015. 8. 4.     Park SeongMin     최초 생성
 * </pre>
 */

public class ListToDataSetConverterTest {

    private ListToDataSetConverter converter;
    
    @Before
    public void setUp() {
        converter = new ListToDataSetConverter();
    }
    
    @Test
    public void testSupportedType() {
        Class<?> source;
        Class<?> target;
        boolean canConvert;
        
        source = List.class;
        target = DataSet.class;
        canConvert = converter.canConvert(source, target);
        Assert.assertTrue(source + " to " + target + " must be converted", canConvert);
        
        // List sub class support.
        source = ArrayList.class;
        target = DataSet.class;
        canConvert = converter.canConvert(source, target);
        Assert.assertTrue(source + " to " + target + " must be converted", canConvert);
    }
    
    @Test
    public void testUnSupportedType() {
        
        Class<?> source;
        Class<?> target;
        boolean canConvert;
        
        source = Object.class;
        target = DataSet.class;
        canConvert = converter.canConvert(source, target);
        Assert.assertFalse(source + " to " + target + " can not convertible", canConvert);
        
        
        List list = new ArrayList();
        list.add(new Object[]{1, 2});
        list.add(new Object[]{3, 4});
        
        ConvertDefinition definition = new ConvertDefinition("ds");
        try {
            converter.convert(list, definition);
            Assert.fail("Object[] is unsupported type.");
        } catch (NexacroConvertException e) {
        }
        
    }
    
    @Test
    public void testConvertListBeanToDataSet() {
        
        List<DefaultBean> defaultBean = NexacroTestUtil.createDefaultBeans();
        
        ConvertDefinition definition = new ConvertDefinition("ds");
        
        Object ds = null;
        try {
            ds = converter.convert(defaultBean, definition);
        } catch (NexacroConvertException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
        
        if(!(ds instanceof DataSet)) {
            Assert.fail("converted object must be implemented DataSet");
        }
        
        NexacroTestUtil.compareDefaultDataSet((DataSet) ds);
        
    }
    
    @Test
    public void testConvertListMapToDataSet() {
        
        List<Map<String, Object>> defaultMap = NexacroTestUtil.createDefaultMaps();
        
        ConvertDefinition definition = new ConvertDefinition("ds");
        
        Object ds = null;
        try {
            ds = converter.convert(defaultMap, definition);
        } catch (NexacroConvertException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
        
        if(!(ds instanceof DataSet)) {
            Assert.fail("converted object must be implemented DataSet");
        }
        
        NexacroTestUtil.compareDefaultDataSet((DataSet) ds);
        
    }
    
    @Test
    public void testNullData() {
        
        List list = null;
        ConvertDefinition definition = new ConvertDefinition("ds");
        
        DataSet ds = null;
        try {
            ds = converter.convert(list, definition);
        } catch (NexacroConvertException e) {
            Assert.fail(e.getMessage());
        }
        
        Assert.assertNotNull("dataset should not be null", ds);
        Assert.assertEquals("ds", ds.getName());
        
    }
    
    @Test
    public void testStaticColumns() {
        
        StaticPropertyBean staticBean;
        double commissionPercent = 10.0d;
        
        List<StaticPropertyBean> staticBeanList = new ArrayList<StaticPropertyBean>();
        staticBean = new StaticPropertyBean();
        staticBean.setName("tom");
        staticBean.setCommissionPercent(commissionPercent);
        staticBeanList.add(staticBean);
        
        staticBean = new StaticPropertyBean();
        staticBean.setName("david");
        staticBeanList.add(staticBean);
        
        ConvertDefinition definition = new ConvertDefinition("ds");
        DataSet ds = null;
        try {
            ds = converter.convert(staticBeanList, definition);
        } catch (NexacroConvertException e) {
            Assert.fail(e.getMessage());
        }
        
        Assert.assertNotNull("converted list should not be null.", ds);
        
        int columnCount = ds.getColumnCount();
        Assert.assertEquals("two columns must be exist.", 2, ds.getColumnCount());
        
        ColumnHeader column = ds.getColumn("name");
        Assert.assertFalse(column.isConstant());
        
        column = ds.getColumn("commissionPercent");
        Assert.assertTrue(column.isConstant());
        ConstantColumnHeader constColumn = (ConstantColumnHeader) column;
        
        // check const column value
        Assert.assertEquals(commissionPercent, constColumn.getValue());
        
        Assert.assertEquals("tom", ds.getString(0, "name"));
        Assert.assertEquals("david", ds.getString(1, "name"));
        
    }
    
    @Test
    public void testUpperCase() {
        
    }
    
    @Test
    public void testLowerCase() {
        
    }
    
    // 
    @Test
    public void testNotSupportedRowType() {
    }
    
    @Test
    public void testNotSupportedSavedData() {
    }
    
    @Test
    public void testNotSupportedRemovedData() {
    }
    
    
    
    
    
    
    
    @Test
    public void testListConvert() {
        
        List<DefaultBean> beanList = new ArrayList<DefaultBean>();
        
//        Result result = new Result();
////        List<?> list = result.getList();
//        
//        
//        DefaultBean bean = new DefaultBean();
//        beanList.add(bean);
//        
//        Object[] array = beanList.toArray();
//        array[0].getClass();
//        
//        Class<? extends List> clazz= beanList.getClass();
//        
//        ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericSuperclass();
//        System.out.println(parameterizedType.getRawType());
//        
//        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
//        for(Type type: actualTypeArguments) {
//            System.out.println(type);
//        }
        
        DefaultBean bean = new DefaultBean();
        beanList.add(bean);
        
        System.out.println("key = " + beanList);
        Class<?> clazz = beanList.getClass();
        System.out.println("clazz = " + clazz);
        ParameterizedType superclass = (ParameterizedType) clazz.getGenericSuperclass();
        Type[] types = superclass.getActualTypeArguments();
        Class<?> actualdataType = null;
        if(types != null && types.length >0 && (types[0] instanceof Class<?>) ) {
            actualdataType = (Class<?>) (Class<?>) types[0];
        }
        System.out.println("actualdataType = " + actualdataType);
    }
    
    @Test
    public void testListType() {
        
        List<DefaultBean> beanList = new ArrayList<DefaultBean>();
        Type[] parameterizedTypes = getParameterizedTypes(beanList);
        
        Class<?> class1;
        try {
            class1 = getClass(parameterizedTypes[0]);
            System.out.println(class1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
//        List<DefaultBean> beanList = new ArrayList<DefaultBean>();
//        
//        Class<?> class1 = getClass(beanList.getClass());
//        System.out.println(class1);
//        System.out.println(getClass(class1));
    }
    
    private static final String TYPE_CLASS_NAME_PREFIX = "class ";
    private static final String TYPE_INTERFACE_NAME_PREFIX = "interface ";
    
    public static String getClassName(Type type) {
        if (type==null) {
            return "";
        }
        String className = type.toString();
        if (className.startsWith(TYPE_CLASS_NAME_PREFIX)) {
            className = className.substring(TYPE_CLASS_NAME_PREFIX.length());
        } else if (className.startsWith(TYPE_INTERFACE_NAME_PREFIX)) {
            className = className.substring(TYPE_INTERFACE_NAME_PREFIX.length());
        }
        return className;
    }
    
    /**
     * Returns the {@code Class} object associated with the given {@link Type}
     * depending on its fully qualified name. 
     * 
     * @param type the {@code Type} whose {@code Class} is needed.
     * @return the {@code Class} object for the class with the specified name.
     * 
     * @throws ClassNotFoundException if the class cannot be located.
     * 
     * @see {@link ReflectionUtil#getClassName(Type)}
     */
    public static Class<?> getClass(Type type) throws ClassNotFoundException {
        String className = getClassName(type);
        if (className==null || className.isEmpty()) {
            return null;
        }
        return Class.forName(className);
    }
    
    public static Type[] getParameterizedTypes(Object object) {
        Type superclassType = object.getClass().getGenericSuperclass();
        if (!ParameterizedType.class.isAssignableFrom(superclassType.getClass())) {
            return null;
        }
        
        return ((ParameterizedType)superclassType).getActualTypeArguments();
    }
    
//    public static Class<?> getClass(Type type) {
//        if (type instanceof Class) {
//          return (Class) type;
//        }
//        else if (type instanceof ParameterizedType) {
//          return getClass(((ParameterizedType) type).getRawType());
//        }
//        else if (type instanceof GenericArrayType) {
//          Type componentType = ((GenericArrayType) type).getGenericComponentType();
//          Class<?> componentClass = getClass(componentType);
//          if (componentClass != null ) {
//            return Array.newInstance(componentClass, 0).getClass();
//          }
//          else {
//            return null;
//          }
//        }
//        else {
//          return null;
//        }
//      }
    
    
    private static class Result {
        private List<?> list;
        public void setList(List<?> list) {
            this.list = list;
        }
        
        public List<?> getList() {
            return null;
        }
        
    }
    
    
}