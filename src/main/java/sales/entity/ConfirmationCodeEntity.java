package sales.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.GenerationType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Table(name = "confirmation_codes")
@NoArgsConstructor
@Getter
@Setter
public class ConfirmationCodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "confirmation_code_sequence")
    @SequenceGenerator(name = "confirmation_code_sequence",
            sequenceName = "confirmation_code_sequence",
            allocationSize = 1)
    private Long id;
    private Integer code;
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;
    private LocalDateTime expiredAt;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    private UserEntity user;

    public ConfirmationCodeEntity(Integer code, LocalDateTime createdAt, LocalDateTime expiredAt, UserEntity user) {
        this.code = code;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
        this.user = user;
    }
}
