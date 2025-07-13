package org.songeun.petdongne_server.global.file;

import java.io.BufferedReader;
import java.util.Collection;
import java.util.List;

public interface Parser<T> {

    public List<T> parse(BufferedReader reader);

}
