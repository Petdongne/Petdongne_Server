package org.songeun.petdongne_server.global.file.validation;

import java.util.Set;

public interface ComparableCsvHeader {

    public boolean compare(Set<String> headers);

    public boolean isSupported(Class<?> clazz);
    
}
