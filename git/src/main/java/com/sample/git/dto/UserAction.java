package com.sample.git.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserAction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String command;
  private String repositoryName;
  private LocalDateTime actionDate;

  public UserAction(String command, String repositoryName, LocalDateTime actionDate) {
    this.command = command;
    this.repositoryName = repositoryName;
    this.actionDate = actionDate;
  }
}
