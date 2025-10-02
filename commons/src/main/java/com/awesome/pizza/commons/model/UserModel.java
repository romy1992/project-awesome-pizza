package com.awesome.pizza.commons.model;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.*;

/** DTO for transferring user data via API. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserModel {
  private Long id;
  @NotNull private String name;
  @NotNull private LocalDateTime pickupFrom;
  @NotNull private LocalDateTime pickupTo;
  private String userComment;
}
