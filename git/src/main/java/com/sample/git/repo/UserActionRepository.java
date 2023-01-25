package com.sample.git.repo;

import com.sample.git.dto.UserAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActionRepository extends JpaRepository<UserAction, Long> {
  //Delete action performed by the repository by its name
  void deleteByRepositoryName(String repoName);
}
