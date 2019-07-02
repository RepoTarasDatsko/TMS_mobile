package com.papyrus.mobile.mobile_tms;

import org.ksoap2.serialization.SoapObject;

import org.ksoap2.serialization.PropertyInfo;

public class SoapObjectCustom extends SoapObject {
    public SoapObjectCustom(String namespace, String name) {
        super(namespace, name);
    }

    @Override
    public SoapObject addProperty(String name, Object value) {
        PropertyInfo propertyInfo = new PropertyInfo();
        propertyInfo.name = name;
        propertyInfo.type = value == null ? PropertyInfo.OBJECT_CLASS
                : value.getClass();
        propertyInfo.setValue(value);

        propertyInfo.setNamespace(this.namespace);

        return addProperty(propertyInfo);
    }
}
