package am.itspace.jobboard.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "githubClient", url = "https://api.github.com")
public interface GitHubFeignClient {

    @GetMapping("/user/emails")
    List<GitHubUserInfo> getEmails(@RequestHeader("Authorization") String authorization);

    @GetMapping("/user")
    GitHubUserInfo getLogin(@RequestHeader("Authorization") String authorization);
}
