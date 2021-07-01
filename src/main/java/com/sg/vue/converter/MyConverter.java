package com.sg.vue.converter;

import com.sg.vue.converter.annotion.EnableUserInfoTransform;
import com.sg.vue.converter.annotion.TransformMethod;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class MyConverter extends MappingJackson2HttpMessageConverter {

    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        beforeWriternal(object, type);
        super.writeInternal(object, type, outputMessage);
    }

    private void beforeWriternal(Object object, Type type) {
        beforeWriternal(object, type, false);
    }

    private void beforeWriternal(Object object, Type type, Boolean flag) {
        if (type != null && ParameterizedTypeImpl.class.getName().equals(type.getClass().getName())) {
            ParameterizedType tp = (ParameterizedType) type;
            Type[] types = tp.getActualTypeArguments();
            if (types.length > 0 && ResponseResult.class.getName().equals(tp.getRawType().getTypeName())) {
                String name = types[0].getClass().getName();
                if (ParameterizedTypeImpl.class.getName().equals(name)) {
                    ParameterizedType subType = (ParameterizedType) types[0];
                    Type[] subTypes = subType.getActualTypeArguments();
                    //ResponseResult<List<VO>>
                    if (subTypes.length > 0
                            && List.class.getName().equals(subType.getRawType().getTypeName())
                            && Class.class.getName().equals(subTypes[0].getClass().getName())) {
                        Class clazz = (Class) subTypes[0];
                        dataHandler(object, clazz, true);
                    }
                    //ResponseResult<VO>
                } else if (Class.class.getName().equals(name)) {
                    Class clazz = (Class) types[0];
                    dataHandler(object, clazz, false);
                }
            }
        } else if (flag) {
            if (Class.class.getName().equals(type.getClass().getName())) {
                Class clazz = (Class) type;
                dataHandler(object, clazz, true);
            } else if (Class.class.getName().equals(type.getClass().getName())) {
                Class clazz = (Class) type;
                dataHandler(object, clazz, false);
            }
        }
    }

    private void dataHandler(Object object, Class clazz, boolean isArray) {
        Annotation ant = clazz.getAnnotation(EnableUserInfoTransform.class);
        if (ant == null) {
            return;
        }
        List<Field> fds = new ArrayList<>();
        fds.addAll(Arrays.asList(clazz.getDeclaredFields()));
        Class tempClass = clazz.getSuperclass();
        while (tempClass != null) {
            fds.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = clazz.getSuperclass();
        }
        try {
            ResponseResult<Object> res = (ResponseResult) object;
            if (res != null && res.getData() != null && !fds.isEmpty()) {
                spiBug(fds, clazz, res.getData(), isArray);
            }
        } catch (Exception e) {
            spiBug(fds, clazz, object, isArray);
        }
    }

    private void spiBug(List<Field> fds, Class clazz, Object data, boolean isArray) {
        //取出get/set方法
        List<TransformMethod> methodList = new ArrayList<>();
        getMethods(fds, methodList, clazz);
        if (!methodList.isEmpty()) {
            //赋值
            dataObjectHandler(data, methodList, isArray);
        }

        //递归寻找VO中的VO和List<VO>
        fds.forEach(f -> {
            EnableUserInfoTransform enTr = f.getAnnotation(EnableUserInfoTransform.class);
            if (enTr == null) {
                return;
            }
            Class<?> subClazz = f.getType();
            if (List.class.getName().equals(subClazz.getTypeName())) {
                try {
                    PropertyDescriptor subDescriptor = new PropertyDescriptor(f.getName(), clazz);
                    if (data instanceof List) {
                        for (Object vo : (List) data) {
                            Object subObject = subDescriptor.getReadMethod().invoke(vo);
                            Type gType = f.getGenericType();
                            if (gType != null && gType instanceof ParameterizedType) {
                                ParameterizedType pt = (ParameterizedType) gType;
                                subClazz = (Class<?>) pt.getActualTypeArguments()[0];
                            }
                            beforeWriternal(subObject, subClazz, true);
                        }
                    } else {
                        Object subObject = subDescriptor.getReadMethod().invoke(data);
                        Type gType = f.getGenericType();
                        if (gType != null && gType instanceof ParameterizedType) {
                            ParameterizedType pt = (ParameterizedType) gType;
                            subClazz = (Class<?>) pt.getActualTypeArguments()[0];
                        }
                        beforeWriternal(subObject, subClazz, true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    PropertyDescriptor subDescriptor = new PropertyDescriptor(f.getName(), clazz);
                    Object subObject = subDescriptor.getReadMethod().invoke(data);
                    beforeWriternal(subObject, subClazz, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private void dataObjectHandler(Object data, List<TransformMethod> methodList, boolean isArray) {
    }

    private void getMethods(List<Field> fds, List<TransformMethod> methodList, Class clazz) {
    }

}
