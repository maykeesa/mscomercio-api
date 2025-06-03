package br.com.ms.categoria.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

public class CategoriaDto {

    public enum Request{;

        @Data
        public static class Categoria{
            @NotBlank(message = "O valor não pode ser nulo ou vazio.")
            private String nome;
        }
    }

    public enum Response{;

        @Data
        public static class Categoria{
            private UUID id;
            private String nome;
            private LocalDateTime dataCriacao;
        }
    }
}
