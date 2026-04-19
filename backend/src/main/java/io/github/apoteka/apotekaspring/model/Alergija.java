package io.github.apoteka.apotekaspring.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "alergija")
public class Alergija {
    @EmbeddedId
    private AlergijaId id;

    @MapsId("pacijentId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "pacijent_id", nullable = false)
    private Pacijent pacijent;

    @MapsId("lekId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "lek_id", nullable = false)
    private Lek lek;


}