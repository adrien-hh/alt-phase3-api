package org.alt.altphase3api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info =
        @Info(
            title = "Internal Tools Management API",
            version = "1.0",
            description =
                "REST API for managing internal SaaS tools used across departments. "
                    + "Supports filtering, pagination, usage metrics, and tool lifecycle management.",
            contact = @Contact(name = "API Team")))
public class OpenApiConfig {}
