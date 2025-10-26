package com.buscadorpelut.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private Long codiUs;
    private String nomUs;
    private String cognom1;
    private String cognom2;
    private String emailUs;
    private String clauPas;
    private String rolUs;
}
