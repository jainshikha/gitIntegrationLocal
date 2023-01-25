package com.sample.git;

import com.sample.git.exception.customException;
import com.sample.git.repo.UserActionRepository;
import com.sample.git.service.GitService;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GitServiceTest {
    private static final String WORKSPACE = "workspace/";
    private String repoUrl = "https://github.com/test/test-repo.git";
    private String repoName = "test-repo";
    private String workSpaceDir = WORKSPACE + repoName;
    private File localRepo = new File(workSpaceDir);

    @Mock
    private Logger log;

    @Mock
    private UserActionRepository userActionRepository;

    @Mock
    private Git git;
  @Mock
  private CloneCommand gitCloneCommand;

    @InjectMocks
    private GitService gitService;

    @Before
    public void setup() throws GitAPIException, IOException {
      when(Git.cloneRepository()).thenReturn(gitCloneCommand);
      doNothing().when(gitCloneCommand).call();
      when(gitCloneCommand.setBranch("main")).thenReturn(gitCloneCommand);
      when(gitCloneCommand.setURI(repoUrl)).thenReturn(gitCloneCommand);
      when(gitCloneCommand.setDirectory(localRepo)).thenReturn(gitCloneCommand);
    }

    @Test
    public void testClone() {
        String response = gitService.clone(repoUrl);
        assertEquals("Completed Cloning & action saved", response);
    }

    @Test(expected = customException.class)
    public void testCloneWithInvalidUrl() {
        gitService.clone("https://github.com/test/test-repo");
    }

    @Test(expected = customException.class)
    public void testCloneWithExistRepo() {
        localRepo.mkdir();
        gitService.clone(repoUrl);
    }
}
