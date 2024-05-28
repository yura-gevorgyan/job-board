package am.itspace.jobboard.feignClient;

import lombok.Data;

@Data
public class GitHubUserInfo {
    private String email;
    private String login;
}
