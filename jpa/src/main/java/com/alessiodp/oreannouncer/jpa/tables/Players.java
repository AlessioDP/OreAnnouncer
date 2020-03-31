package com.alessiodp.oreannouncer.jpa.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "players")
public class Players {
	@Id
	@Column(name = "uuid", unique = true, nullable = false)
	private String uuid;
	
	@Column(name = "alerts", nullable = false)
	private Boolean alerts;
}
