package io.github.apoteka.apotekaspring.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "lek_dobavljac")
public class LekDobavljac {
    @EmbeddedId
    private LekDobavljacId id;

    @MapsId("lekId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "lek_id", nullable = false)
    private Lek lek;

    @MapsId("dobavljacId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "dobavljac_id", nullable = false)
    private Dobavljac dobavljac;


}