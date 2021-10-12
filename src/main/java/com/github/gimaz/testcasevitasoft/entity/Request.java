package com.github.gimaz.testcasevitasoft.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String title;

    @Column(length = 1000)
    private String text;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    @JsonBackReference
    private User user;

    private RequestStatus status;

    private LocalDateTime createdTime;

    private LocalDateTime lastUpdateTime;


    public enum RequestStatus{
        DRAFT{
            @Override
            public String getName() {
                return "Черновик";
            }
        },
        SENT{
            @Override
            public String getName() {
                return "Отправлено";
            }
        },
        ACCEPTED{
            @Override
            public String getName() {
                return "Принято";
            }
        },
        REJECTED{
            @Override
            public String getName() {
                return "Отклонено";
            }
        };

        public static RequestStatus getByName(String name) throws Exception {
            switch (name)
            {
                case "Черновик": return DRAFT;
                case "Отправлено": return SENT;
                case "Принято": return ACCEPTED;
                case "Отклонено": return REJECTED;
                default: throw new Exception("Not found RequestStatusWithName: " + name);
            }
        }
        public abstract String getName();
    }
}


