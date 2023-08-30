package com.bosonit.conecta4.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Invitacion {

    private Integer idInvitacion;
    private String nombreJugador1;
    private String ipCliente1;

}
