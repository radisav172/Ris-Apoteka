package io.github.apoteka.apotekaspring.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class AlergijaId implements Serializable {
    private static final long serialVersionUID = 250416433062208950L;
    @NotNull
    @Column(name = "pacijent_id", nullable = false)
    private Integer pacijentId;

    @NotNull
    @Column(name = "lek_id", nullable = false)
    private Integer lekId;


}