package org.songeun.petdongne_server.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geolatte.geom.json.GeolatteGeomModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ObjectMapperConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper(){
        ObjectMapper mapper=new ObjectMapper();
        mapper.registerModule(new GeolatteGeomModule());

        return mapper;
    }

}
