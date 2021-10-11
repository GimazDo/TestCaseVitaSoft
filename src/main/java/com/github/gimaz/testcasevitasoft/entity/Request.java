package com.github.gimaz.testcasevitasoft.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    private RequestStatus status;

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


        public abstract String getName();
    }
}


