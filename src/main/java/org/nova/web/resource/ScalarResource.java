package org.nova.web.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/scalar")
@PermitAll
public class ScalarResource {

    private static final String SCALAR_HTML = """
            <!doctype html>
            <html lang="es">
              <head>
                <title>Nova Transform API Reference</title>
                <meta charset="utf-8" />
                <meta name="viewport" content="width=device-width, initial-scale=1" />
                <link rel="icon" type="image/svg+xml" href="https://scalar.com/favicon.svg" />
                <style>
                  body {
                    margin: 0;
                    padding: 0;
                    height: 100vh;
                    overflow: hidden;
                  }
                </style>
              </head>
              <body>
                <script
                  id="api-reference"
                  data-url="/openapi"
                  data-configuration='{"theme":"purple","layout":"modern","searchHotKey":"k","showSidebar":true}'>
                </script>
                <script src="https://cdn.jsdelivr.net/npm/@scalar/api-reference"></script>
              </body>
            </html>
            """;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getScalarUi() {
        return Response.ok(SCALAR_HTML).build();
    }
}
