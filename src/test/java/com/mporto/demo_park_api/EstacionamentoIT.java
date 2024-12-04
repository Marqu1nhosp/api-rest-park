package com.mporto.demo_park_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mporto.demo_park_api.web.dto.EstacionamentoCreateDto;
import com.mporto.demo_park_api.web.dto.PageableDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/estacionamentos/estacionamentos-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/estacionamentos/estacionamentos-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

public class EstacionamentoIT {

        @Autowired
        WebTestClient testClient;

        @Test
        public void CriarCheckin_ComDadosValidos_RetornarCreatedAndLocation() {
                EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                                .placa("PJH-1G01").marca("GM").modelo("Onix")
                                .cor("BRANCO").clienteCpf("09191773016")
                                .build();

                testClient.post().uri("/api/v1/estacionamentos/check-in")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com",
                                                "123456"))
                                .bodyValue(createDto)
                                .exchange()
                                .expectStatus().isCreated()
                                .expectHeader().exists(HttpHeaders.LOCATION)
                                .expectBody()
                                .jsonPath("placa").isEqualTo("PJH-1G01")
                                .jsonPath("marca").isEqualTo("GM")
                                .jsonPath("modelo").isEqualTo("Onix")
                                .jsonPath("cor").isEqualTo("BRANCO")
                                .jsonPath("clienteCpf").isEqualTo("09191773016")
                                .jsonPath("recibo").exists()
                                .jsonPath("dataEntrada").exists()
                                .jsonPath("vagaCodigo").exists();

        }

        @Test
        public void CriarCheckin_ComRoleCliente_RetornarErrorStatus403() {
                EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                                .placa("PJH-1G01").marca("GM").modelo("Onix")
                                .cor("BRANCO").clienteCpf("09191773016")
                                .build();

                testClient.post().uri("/api/v1/estacionamentos/check-in")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@gmail.com",
                                                "123456"))
                                .bodyValue(createDto)
                                .exchange()
                                .expectStatus().isForbidden()
                                .expectBody()
                                .jsonPath("status").isEqualTo("403")
                                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in")
                                .jsonPath("method").isEqualTo("POST");

        }

        @Test
        public void CriarCheckin_ComDadosInvalidos_RetornarErrorStatus422() {
                EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                                .placa("").marca("").modelo("")
                                .cor("").clienteCpf("")
                                .build();

                testClient.post().uri("/api/v1/estacionamentos/check-in")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@gmail.com",
                                                "123456"))
                                .bodyValue(createDto)
                                .exchange()
                                .expectStatus().isEqualTo(422)
                                .expectBody()
                                .jsonPath("status").isEqualTo("422")
                                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in")
                                .jsonPath("method").isEqualTo("POST");

        }

        @Test
        public void CriarCheckin_ComCpfInexistente_RetornarErrorStatus404() {
                EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                                .placa("PJH-1G01").marca("GM").modelo("Onix")
                                .cor("BRANCO").clienteCpf("25239627053")
                                .build();

                testClient.post().uri("/api/v1/estacionamentos/check-in")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com",
                                                "123456"))
                                .bodyValue(createDto)
                                .exchange()
                                .expectStatus().isNotFound()
                                .expectBody()
                                .jsonPath("status").isEqualTo("404")
                                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in")
                                .jsonPath("method").isEqualTo("POST");

        }

        @Sql(scripts = "/sql/estacionamentos/estacionamento-insert-vagas-ocupadas.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(scripts = "/sql/estacionamentos/estacionamento-delete-vagas-ocupadas.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        @Test
        public void CriarCheckin_ComVagasOcupadas_RetornarErrorStatus404() {
                EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                                .placa("PJH-1G01").marca("GM").modelo("Onix")
                                .cor("BRANCO").clienteCpf("09191773016")
                                .build();

                testClient.post().uri("/api/v1/estacionamentos/check-in")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com",
                                                "123456"))
                                .bodyValue(createDto)
                                .exchange()
                                .expectStatus().isNotFound()
                                .expectBody()
                                .jsonPath("status").isEqualTo("404")
                                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in")
                                .jsonPath("method").isEqualTo("POST");

        }

        @Test
        public void BuscarCheckin_ComPerfilAdmin_RetornarDadosStatus200() {
                testClient.get().uri("/api/v1/estacionamentos/check-in/{recibo}", "20230313-101300")
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com",
                                                "123456"))
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody()
                                .jsonPath("placa").isEqualTo("FIT-1020")
                                .jsonPath("marca").isEqualTo("FIAT")
                                .jsonPath("modelo").isEqualTo("PALIO")
                                .jsonPath("cor").isEqualTo("VERDE")
                                .jsonPath("clienteCpf").isEqualTo("98401203015")
                                .jsonPath("recibo").isEqualTo("20230313-101300")
                                .jsonPath("dataEntrada").isEqualTo("2023-03-13 10:15:00")
                                .jsonPath("vagaCodigo").isEqualTo("A-01");

        }

        @Test
        public void BuscarCheckin_ComPerfilCliente_RetornarDadosStatus200() {
                testClient.get().uri("/api/v1/estacionamentos/check-in/{recibo}", "20230313-101300")
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@gmail.com",
                                                "123456"))
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody()
                                .jsonPath("placa").isEqualTo("FIT-1020")
                                .jsonPath("marca").isEqualTo("FIAT")
                                .jsonPath("modelo").isEqualTo("PALIO")
                                .jsonPath("cor").isEqualTo("VERDE")
                                .jsonPath("clienteCpf").isEqualTo("98401203015")
                                .jsonPath("recibo").isEqualTo("20230313-101300")
                                .jsonPath("dataEntrada").isEqualTo("2023-03-13 10:15:00")
                                .jsonPath("vagaCodigo").isEqualTo("A-01");

        }

        @Test
        public void BuscarCheckin_ComReciboInexistente_RetornarErrorStatus404() {
                testClient.get().uri("/api/v1/estacionamentos/check-in/{recibo}", "20230313-999999")
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@gmail.com",
                                                "123456"))
                                .exchange()
                                .expectStatus().isNotFound()
                                .expectBody()
                                .jsonPath("status").isEqualTo("404")
                                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in/20230313-999999")
                                .jsonPath("method").isEqualTo("GET");

        }

        @Test
        public void criarCheckout_ComReciboExistente_RetornarSucesso() {
                testClient.put().uri("/api/v1/estacionamentos/check-out/{recibo}", "20230313-101300")
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com",
                                                "123456"))
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody()
                                .jsonPath("placa").isEqualTo("FIT-1020")
                                .jsonPath("marca").isEqualTo("FIAT")
                                .jsonPath("modelo").isEqualTo("PALIO")
                                .jsonPath("cor").isEqualTo("VERDE")
                                .jsonPath("dataEntrada").isEqualTo("2023-03-13 10:15:00")
                                .jsonPath("clienteCpf").isEqualTo("98401203015")
                                .jsonPath("vagaCodigo").isEqualTo("A-01")
                                .jsonPath("recibo").isEqualTo("20230313-101300")
                                .jsonPath("dataSaida").exists()
                                .jsonPath("valor").exists()
                                .jsonPath("desconto").exists();

        }

        @Test
        public void criarCheckout_ComReciboInexistente_RetornaErrorStatus404() {
                testClient.put().uri("/api/v1/estacionamentos/check-out/{recibo}", "20230313-000000")
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com",
                                                "123456"))
                                .exchange()
                                .expectStatus().isNotFound()
                                .expectBody()
                                .jsonPath("status").isEqualTo("404")
                                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-out/20230313-000000")
                                .jsonPath("method").isEqualTo("PUT");

        }

        @Test
        public void criarCheckout_ComRoleCliente_RetornaErrorStatus403() {
                testClient.put().uri("/api/v1/estacionamentos/check-out/{recibo}", "20230313-101300")
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@gmail.com",
                                                "123456"))
                                .exchange()
                                .expectStatus().isForbidden()
                                .expectBody()
                                .jsonPath("status").isEqualTo("403")
                                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-out/20230313-101300")
                                .jsonPath("method").isEqualTo("PUT");

        }

        @SuppressWarnings("null")
        @Test
        public void buscarEstacioanamentos_PorClienteCpf_RetornarSucesso() {
                PageableDto responseBody = testClient.get()
                                .uri("/api/v1/estacionamentos/cpf/{cpf}?size=1&page=0", "98401203015")
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com",
                                                "123456"))
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody(PageableDto.class)
                                .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);
                org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);
                org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(0);
                org.assertj.core.api.Assertions.assertThat(responseBody.getSize()).isEqualTo(1);

                responseBody = testClient.get()
                                .uri("/api/v1/estacionamentos/cpf/{cpf}?size=1&page=1", "98401203015")
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com",
                                                "123456"))
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody(PageableDto.class)
                                .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);
                org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);
                org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(1);
                org.assertj.core.api.Assertions.assertThat(responseBody.getSize()).isEqualTo(1);

        }

        @Test
        public void buscarEstacioanamentos_PorClienteCpfComPerfilCliente_RetornaErrorStatus403() {
                testClient.get().uri("/api/v1/estacionamentos/cpf/{cpf}",
                                "98401203015")
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient,
                                                "bia@gmail.com",
                                                "123456"))
                                .exchange()
                                .expectStatus().isForbidden()
                                .expectBody()
                                .jsonPath("status").isEqualTo("403")
                                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/cpf/98401203015")
                                .jsonPath("method").isEqualTo("GET");

        }

        @SuppressWarnings("null")
        @Test
        public void buscarEstacioanamentos_DoClienteLogado_RetornarSucesso() {
                PageableDto responseBody = testClient.get()
                                .uri("/api/v1/estacionamentos?size=1&page=0")
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "marcos@gmail.com",
                                                "123456"))
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody(PageableDto.class)
                                .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);
                org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(1);
                org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(0);
                org.assertj.core.api.Assertions.assertThat(responseBody.getSize()).isEqualTo(1);

                responseBody = testClient.get()
                                .uri("/api/v1/estacionamentos?size=1&page=1", "98401203015")
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "marcos@gmail.com",
                                                "123456"))
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody(PageableDto.class)
                                .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(0);
                org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(1);
                org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(1);
                org.assertj.core.api.Assertions.assertThat(responseBody.getSize()).isEqualTo(1);

        }

        @Test
        public void buscarEstacioanamentos_DoClienteLogadoComPerfilAdmin_RetornaErrorStatus403() {
                testClient.get().uri("/api/v1/estacionamentos")
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient,
                                                "ana@gmail.com",
                                                "123456"))
                                .exchange()
                                .expectStatus().isForbidden()
                                .expectBody()
                                .jsonPath("status").isEqualTo("403")
                                .jsonPath("path").isEqualTo("/api/v1/estacionamentos")
                                .jsonPath("method").isEqualTo("GET");

        }
}
