package com.example.grpcexample.entity;

import lombok.*;
import org.springframework.cache.annotation.EnableCaching;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long balance;
}
