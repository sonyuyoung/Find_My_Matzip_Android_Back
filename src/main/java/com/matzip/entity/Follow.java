package com.matzip.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Table(name="follow",
        uniqueConstraints = {
        @UniqueConstraint(
                name="follow_uk",
                columnNames = {"to_user", "from_user"}
        )
})
@Entity
@Getter @Setter
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user")
    private Users toUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user")
    private Users fromUser;

    public Follow(Users toUser, Users fromUser){
        this.toUser = toUser;
        this.fromUser = fromUser;
    }
}