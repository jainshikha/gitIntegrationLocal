package com.sample.git;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.sample.git.controller.GitController;
import com.sample.git.service.GitService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GitControllerTest {

  @InjectMocks
  private GitController gitController;

  @Mock
  private GitService gitService;

  private String repoUrl = "https://github.com/test/test-repo.git";
  private String expectedResponse = "Repository cloned successfully.";
  private String repoName = "test-repo";
  private List<String> expectedHistory = Arrays.asList("Commit 1", "Commit 2", "Commit 3");
  private String expectedDeleteResponse = "Repository deleted successfully.";

  @Before
  public void setup() {
    when(gitService.clone(repoUrl)).thenReturn(expectedResponse);
    when(gitService.showHistory(repoName)).thenReturn(expectedHistory);
    when(gitService.delete(repoName)).thenReturn(expectedDeleteResponse);

  }

  @Test
  public void testClone() {
    ResponseEntity<String> actualResponse = gitController.clone(repoUrl);
    assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
    assertEquals(expectedResponse, actualResponse.getBody());
  }
  @Test
  public void testShowHistory() {
    ResponseEntity<List<String>> actualResponse = gitController.showHistory(repoName);
    assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
    assertEquals(expectedHistory, actualResponse.getBody());
  }
  @Test
  public void testDelete() {
    ResponseEntity<String> actualResponse = gitController.delete(repoName);

    assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
    assertEquals(expectedDeleteResponse, actualResponse.getBody());
  }
}
