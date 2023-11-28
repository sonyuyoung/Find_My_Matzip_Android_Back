package com.matzip.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // 프록시 객체를 직렬화에서 제외  대신에 레스토랑dto2만듦
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

    @JsonIgnore
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