package am.itspace.jobboard.security;

import am.itspace.jobboard.feignclient.GitHubFeignClient;
import am.itspace.jobboard.feignclient.GitHubUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FetchUserFromGitHub {

    private final GitHubFeignClient gitHubFeignClient;

    public String fetchEmailFromGitHub(String accessToken) {
        List<GitHubUserInfo> emailList = gitHubFeignClient.getEmails("Bearer " + accessToken);
        if (emailList != null && !emailList.isEmpty()) {
            GitHubUserInfo firstEmail = emailList.get(0);
            return firstEmail.getEmail();
        } else {
            return null;
        }
    }

    public String fetchUserNameFromGitHub(String accessToken) {
        GitHubUserInfo gitHubUser = gitHubFeignClient.getLogin("Bearer " + accessToken);
        return gitHubUser != null ? gitHubUser.getLogin() : null;
    }
}
