package com.poscoict.posledger.assets.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(length=100)
    private String id;
    
    @Column(nullable = true, length = 200)
    private String name;
    
    @Column(nullable = false, length = 500)
    private String password;
    
    private Date createDate;
    
    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="id")
	private List<UserConnectLog> connectLogs;
}