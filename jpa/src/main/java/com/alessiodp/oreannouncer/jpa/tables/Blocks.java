package com.alessiodp.oreannouncer.jpa.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "blocks")
public class Blocks implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "player", nullable = false)
	private String player;
	
	@Id
	@Column(name = "material_name", nullable = false)
	private String materialName;
	
	@Column(name = "destroyed")
	private Integer destroyed = 0;
}
