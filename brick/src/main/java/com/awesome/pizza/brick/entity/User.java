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
	@Column(name = "NAME", nullable = false)
	private String name;

	/** Desired pickup time range (from) */
	@Column(name = "PICKUP_FROM", nullable = false)
	private LocalDateTime pickupFrom;

	/** Desired pickup time range (to) */
	@Column(name = "PICKUP_TO", nullable = false)
	private LocalDateTime pickupTo;

	/** User's comment or description */
	@Column(name = "USER_COMMENT", length = 1000)
	private String userComment;
}