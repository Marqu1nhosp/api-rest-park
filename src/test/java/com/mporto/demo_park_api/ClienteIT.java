package com.mporto.demo_park_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mporto.demo_park_api.web.dto.ClienteCreateDto;
import com.mporto.demo_park_api.web.dto.ClienteResponseDto;
import com.mporto.demo_park_api.web.dto.PageableDto;
import com.mporto.demo_park_api.web.exception.ErrorMessage;

@SuppressWarnings("null")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/clientes/clientes-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clientes/clientes-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClienteIT {
        @Autowired
        WebTestClient testClient;

        @Test
        public void criarCliente_ComDadosValidos_RetornarClienteComStatus201() {
                ClienteResponseDto responseBody = testClient
                                .post()
                                .uri("/api/v1/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "toby@gmail.com",
                                                "123456"))
                                .bodyValue(new ClienteCreateDto("Tobias Ferreira", "16497958070"))
                                .exchange()
                                .expectStatus().isCreated()
                                .expectBody(ClienteResponseDto.class)
                                .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getNome()).isEqualTo("Tobias Ferreira");
                org.assertj.core.api.Assertions.assertThat(responseBody.getCpf()).isEqualTo("16497958070");

        }

        @Test
        public void criarCliente_ComCpfjaCadastrado_RetornarErrorMessageStatus409() {
                ErrorMessage responseBody = testClient
                                .post()
                                .uri("/api/v1/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "toby@gmail.com",
                                                "123456"))
                                .bodyValue(new ClienteCreateDto("Tobias Ferreira", "37448384040"))
                                .exchange()
                                .expectStatus().isEqualTo(409)
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);

        }

        @Test
        public void criarCliente_ComDadosInvalidos_RetornarErrorMessageStatus422() {
                ErrorMessage responseBody = testClient
                                .post()
                                .uri("/api/v1/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "toby@gmail.com",
                                                "123456"))
                                .bodyValue(new ClienteCreateDto("", ""))
                                .exchange()
                                .expectStatus().isEqualTo(422)
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

                responseBody = testClient
                                .post()
                                .uri("/api/v1/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "toby@gmail.com",
                                                "123456"))
                                .bodyValue(new ClienteCreateDto("Tob", "00000000000"))
                                .exchange()
                                .expectStatus().isEqualTo(422)
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

                responseBody = testClient
                                .post()
                                .uri("/api/v1/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "toby@gmail.com",
                                                "123456"))
                                .bodyValue(new ClienteCreateDto("Tob", "374.483.840-40"))
                                .exchange()
                                .expectStatus().isEqualTo(422)
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        }

        @Test
        public void criarCliente_ComUsuarioNaoPermitido_RetornarClienteComStatus403() {
                ErrorMessage responseBody = testClient
                                .post()
                                .uri("/api/v1/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com",
                                                "123456"))
                                .bodyValue(new ClienteCreateDto("Tobias Ferreira", "37448384040"))
                                .exchange()
                                .expectStatus().isForbidden()
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);

        }

        @Test
        public void buscarCliente_ComIdExistentePeloAdmin_RetornarClienteComStatus200() {
                ClienteResponseDto responseBody = testClient
                                .get()
                                .uri("/api/v1/clientes/10")
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com",
                                                "123456"))
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody(ClienteResponseDto.class)
                                .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(10);

        }

        @Test
        public void buscarCliente_ComIdInexistentePeloAdmin_RetornarErrorMessageComStatus404() {
                ErrorMessage responseBody = testClient
                                .get()
                                .uri("/api/v1/clientes/0")
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com",
                                                "123456"))
                                .exchange()
                                .expectStatus().isNotFound()
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
        }

        @Test
        public void buscarCliente_ComIdExistentePeloCliente_RetornarErrorMessageComStatus403() {
                ErrorMessage responseBody = testClient
                                .get()
                                .uri("/api/v1/clientes/0")
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@gmail.com",
                                                "123456"))
                                .exchange()
                                .expectStatus().isForbidden()
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
        }

        @Test
        public void buscarClientes_ComPaginacaoPeloAdmin_RetornarClientesComStatus200() {
                PageableDto responseBody = testClient
                                .get()
                                .uri("/api/v1/clientes")
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient,
                                                "ana@gmail.com", "123456"))
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody(PageableDto.class)
                                .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(2);
                org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(0);
                org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(1);

                responseBody = testClient
                                .get()
                                .uri("/api/v1/clientes?size=1&page=1")
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient,
                                                "ana@gmail.com", "123456"))
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody(PageableDto.class)
                                .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);
                org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(1);
                org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);
        }

        @Test
        public void buscarClientes_ComPaginacaoPeloCliente_RetornarErrorMessageComStatus403() {
                ErrorMessage responseBody = testClient
                                .get()
                                .uri("/api/v1/clientes")
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient,
                                                "bia@gmail.com", "123456"))
                                .exchange()
                                .expectStatus().isForbidden()
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
        }

        @Test
        public void buscarCliente_ComDadosDoTokenDeCliente_RetornarClienteComStatus200() {
                ClienteResponseDto responseBody = testClient
                                .get()
                                .uri("/api/v1/clientes/detalhes")
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient,
                                                "bia@gmail.com", "123456"))
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody(ClienteResponseDto.class)
                                .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getCpf()).isEqualTo("37448384040");
                org.assertj.core.api.Assertions.assertThat(responseBody.getNome()).isEqualTo("Bianca Silva");
                org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(20);

        }

        @Test
        public void buscarCliente_ComDadosDoTokenDeAdministrador_RetornarErrorMessageComStatus403() {
                ErrorMessage responseBody = testClient
                                .get()
                                .uri("/api/v1/clientes/detalhes")
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient,
                                                "ana@gmail.com", "123456"))
                                .exchange()
                                .expectStatus().isForbidden()
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);

        }

}
