package com.alessiodp.oreannouncer.jpa.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;

@Entity
@Table(name = "blocks_found")
public class BlocksFound {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Integer id;
	
	@Column(name = "player", nullable = false)
	private String player;
	
	@Column(name = "material_name", nullable = false)
	private String materialName;
	
	@Column(name = "timestamp", nullable = false)
	private BigInteger timestamp;
	
	@Column(name = "found")
	private Integer found = 0;
}
