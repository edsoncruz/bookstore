package com.bookstore.entity.base;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;


/**
 * Common attributes for all the entities.
 * 
 * @author Edson Cruz
 *
 */
@Data
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	protected Long id;
}
