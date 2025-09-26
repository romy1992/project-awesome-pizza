package com.awesome.pizza.commons.model;

import java.time.LocalDateTime;
import lombok.*;

/** DTO for transferring user data via API. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserModel {
  private Long id;
  private String name;
  private LocalDateTime pickupFrom;
  private LocalDateTime pickupTo;
  private String userComment;
}
