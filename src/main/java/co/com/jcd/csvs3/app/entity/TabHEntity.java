
package co.com.jcd.csvs3.app.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class TabHEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ordId;
    private Long idTabG;
    private Long codF;
    private String tipoIdP;
    private Long nIdP;
    private String nP;
    private String tDS;
    private String nDS;
    private LocalDateTime fSol;
    private LocalDateTime fPr;
    private String estP;
    private BigDecimal vB;

}
