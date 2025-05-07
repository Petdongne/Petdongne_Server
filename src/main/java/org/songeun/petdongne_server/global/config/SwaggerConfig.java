package org.songeun.petdongne_server.global.config;

import io.swagger.v3.oas.models.info.Contact;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI springBootAPI() {
        Info info = new Info()
                .title("\uD83D\uDC36 펫동네")
                .description("펫동네 Server API 문서")
                .contact(new Contact().name("김송은").url("https://github.com/mungsil").email("thddmd2009@gmail.com"));

        Server server = new Server().url("/");

        return new OpenAPI()
                .servers(List.of(server))
                .info(info);
    }

}
