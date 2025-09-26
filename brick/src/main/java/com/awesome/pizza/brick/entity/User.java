package com.awesome.pizza.brick.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entity representing a user who places an order in Awesome Pizza.
 * Includes name, pickup time range, and a comment/description.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Name of the user */
	private String name;

	/** Desired pickup time range (from) */
	private LocalDateTime pickupFrom;

	/** Desired pickup time range (to) */
	private LocalDateTime pickupTo;

	/** User's comment or description */
	private String userComment;
}