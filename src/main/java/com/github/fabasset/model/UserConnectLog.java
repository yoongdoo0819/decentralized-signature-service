package com.github.fabasset.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_connect_log")
public class UserConnectLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 12)
    private long logId;
    
    @Column(nullable = false, length = 100)
    private String id;
    
    @Column(nullable = false, length = 500)
    private Date logDate;
}