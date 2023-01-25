package com.sample.git.service;

import com.sample.git.dto.UserAction;
import com.sample.git.exception.customException;
import com.sample.git.repo.UserActionRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class GitService {
  private static final String WORKSPACE = "workspace/";
  @Autowired
  private UserActionRepository userActionRepository;

  /**
   * @param repoUrl
   * @return
   */
  public String clone(String repoUrl) {
    // Find the start index of the repository name by finding the last '/' character in the URL and adding 1
    int startIndex = repoUrl.lastIndexOf("/") + 1;
    // Find the end index of the repository name by finding the '.git' extension
    int endIndex = repoUrl.lastIndexOf(".git");

    // If the '.git' extension is not found in the URL, throw a custom exception
    if (endIndex == -1) {
      throw new customException(HttpStatus.BAD_REQUEST, "Error: Invalid URL, Missing extension");
    }

    // Extract the repository name from the URL using the start and end indices
    String repoName = repoUrl.substring(startIndex, endIndex);

    // Create a string representing the local directory for the repository
    String workSpaceDir = String.format(WORKSPACE + repoName);
    // Create a new File object for the local directory
    File localRepo = new File(workSpaceDir);
    // Check if the directory already exists, if it does throw a custom exception
    if (localRepo.exists())
      throw new customException(HttpStatus.CONFLICT, "Error: Repository already exists at " + workSpaceDir);

    // Print out a message indicating that the repository is being cloned and where it is being cloned to
    log.info("Cloning {}" , repoUrl , " into {}" , repoName);
    try {
      // Clone the repository using the Git.cloneRepository() method
      // set the branch to "main", the URI to the repository URL, and the directory to the local repository directory
      Git.cloneRepository()
        .setBranch("main")
        .setURI(repoUrl)
        .setDirectory(localRepo)
        .call();
    } catch (GitAPIException e) {
      // If an exception is thrown, wrap it in a runtime exception and throw it
      throw new RuntimeException(e);
    }
    // Print out a message indicating that the cloning is completed
    log.info("Completed Cloning");
    // Save the action performed, in this case "clone" and the location of the cloned repository
    userActionRepository.save(new UserAction("clone", WORKSPACE, LocalDateTime.now()));
    // Return a message indicating that the cloning is completed and the action is saved
    return "Completed Cloning & action saved";
  }
  /**
   * @param repoName
   * @return
   */
  @SneakyThrows
  public List<String> showHistory(String repoName) {
    //Open the git repository in the workspace directory
    Git git = Git.open(new File(WORKSPACE + repoName));
    //Retrieve the commit logs of the repository
    Iterable<RevCommit> logs = git.log().call();
    //Create a new list to store the commit logs
    List<String> history = new ArrayList<>();
    //iterate over the commit logs
    for (RevCommit rev : logs) {
      //print out the commit logs
      log.info("Commit: {}" , rev);
      //add the commit logs to the list
      history.add(String.valueOf(rev));
    }
    //return the list of commit logs
    return history;
  }

  /**
   * @param repoName
   * @return
   */
  public String delete(String repoName) {
    // Create a string representing the local directory for the repository
    String workSpaceDir = String.format(WORKSPACE + repoName);
    // Create a new File object for the local directory
    File localRepo = new File(workSpaceDir);
    // Check if the directory exists
    if (localRepo.exists()) {
      try {
        // Delete the directory using the FileUtils.deleteDirectory method
        FileUtils.deleteDirectory(new File(WORKSPACE + repoName));
      } catch (IOException e) {
        // If an exception is thrown, wrap it in a runtime exception and throw it
        throw new RuntimeException(e);
      }
      //delete the action performed by the repository by its name
      userActionRepository.deleteByRepositoryName(repoName);
      // Return a message indicating that the repository has been deleted
      return "repo deleted from local";
    }
    // If the directory does not exist, throw a custom exception indicating that the repository does not exist at that location
    throw new customException(HttpStatus.CONFLICT, "Error: Repository does not exists at " + workSpaceDir);
  }
}
