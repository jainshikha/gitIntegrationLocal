package com.sample.git.controller;

import com.sample.git.service.GitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GitController {

  @Autowired
  private GitService gitService;

  /**
   * @param repoUrl
   * @return
   */
  @PostMapping("/clone")
  public ResponseEntity<String> clone(@RequestBody String repoUrl) {
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(gitService.clone(repoUrl));
  }

  /**
   * @param repoName
   * @return
   */
  @GetMapping("/history/{repoName}")
  public ResponseEntity<List<String>> showHistory(@PathVariable String repoName) {
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(
        gitService.showHistory(repoName));
  }

  /**
   * @param repoName
   * @return
   */
  @DeleteMapping("/delete/{repoName}")
  public ResponseEntity<String> delete(@PathVariable String repoName) {
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(gitService.delete(repoName));
  }

}
