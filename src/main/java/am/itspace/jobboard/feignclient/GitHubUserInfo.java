package am.itspace.jobboard.feignclient;

import lombok.Data;

@Data
public class GitHubUserInfo {
    private String email;
    private String login;
}
