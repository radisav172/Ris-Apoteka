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
public class LekDobavljacId implements Serializable {
    private static final long serialVersionUID = -65819563663355233L;
    @NotNull
    @Column(name = "lek_id", nullable = false)
    private Integer lekId;

    @NotNull
    @Column(name = "dobavljac_id", nullable = false)
    private Integer dobavljacId;


}