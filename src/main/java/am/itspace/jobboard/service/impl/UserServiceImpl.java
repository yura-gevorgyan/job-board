package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.ApplicantList;
import am.itspace.jobboard.entity.Company;
import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.JobApplies;
import am.itspace.jobboard.entity.Resume;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.Role;
import am.itspace.jobboard.exception.EmailIsPresentException;
import am.itspace.jobboard.exception.PasswordNotMuchException;
import am.itspace.jobboard.exception.UseOldPasswordException;
import am.itspace.jobboard.repository.ApplicantListRepository;
import am.itspace.jobboard.repository.CompanyRepository;
import am.itspace.jobboard.repository.JobAppliesRepository;
import am.itspace.jobboard.repository.JobRepository;
import am.itspace.jobboard.repository.ResumeRepository;
import am.itspace.jobboard.repository.UserRepository;
import am.itspace.jobboard.service.SendMailService;
import am.itspace.jobboard.service.UserService;
import am.itspace.jobboard.util.GenerateTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SendMailService sendMailService;
    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final ApplicantListRepository applicantListRepository;
    private final JobAppliesRepository jobAppliesRepository;
    private final ResumeRepository resumeRepository;

    @Override
    public User register(User user, String confirmPassword, Role role) {
        Optional<User> byEmail = userRepository.findByEmail(user.getEmail());

        if (byEmail.isPresent()) {
            throw new EmailIsPresentException();
        } else if (!user.getPassword().equals(confirmPassword)) {
            throw new PasswordNotMuchException();
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role);
        user.setToken(GenerateTokenUtil.generateToken());
        user.setActivated(false);
        user.setDeleted(false);
        user.setRegisterDate(new Date());

        User save = userRepository.save(user);
        sendMailService.sendEmailConfirmMail(save);

        return save;
    }

    @Override
    public User changePassword(String password, String confirmPassword, User user) {

        if (!password.equals(confirmPassword)) {
            throw new PasswordNotMuchException();
        } else if (passwordEncoder.matches(password, user.getPassword())) {
            throw new UseOldPasswordException();
        }
        user.setPassword(passwordEncoder.encode(password));
        save(user);
        sendMailService.send(user.getEmail(), "Changing Password", "Password successfully changed!");
        return user;
    }

    @Override
    public User confirmEmail(String confirmEmailCode) {
        Optional<User> optionalUser = findByToken(confirmEmailCode);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setActivated(true);
            save(user);
            sendMailService.send(user.getEmail(), "successMessage", "Email confirmed successfully!");
            return user;
        }
        return null;
    }

    @Override
    public User forgotPassword(String email) {
        User user = findByEmail(email);
        if (user != null) {
            user.setToken(GenerateTokenUtil.generateToken());
            save(user);
            sendMailService.sendEmailConfirmMail(user);
            return user;
        }
        return null;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public int getUserCount() {
        return userRepository.countBy();
    }

    @Override
    public int getTotalPages() {
        int pageSize = 20;
        long totalCount = userRepository.count();
        return (int) Math.ceil((double) totalCount / pageSize);
    }

    @Override
    public Page<User> getUsersFromNToM(int index) {
        int pageSize = 20;
        return userRepository.findAll(PageRequest.of(index - 1, pageSize).withSort(Sort.by("registerDate")));
    }

    @Override
    public int getTotalPagesOfSearch(String email, String role) {
        int pageSize = 20;
        long totalCount = getUserCountOfEmailRole(email, role);
        return (int) Math.ceil((double) totalCount / pageSize);
    }

    @Override
    public int getUserCountOfEmailRole(String email, String role) {
        Role roleEnum;
        if ((email == null || email.trim().isEmpty()) && (role == null || role.trim().isEmpty())) {
            return 0;
        }
        if (email == null || email.trim().isEmpty()) {
            try {
                roleEnum = Role.valueOf(role);
                return userRepository.countByRole(roleEnum);
            } catch (IllegalArgumentException e) {
                return 0;
            }
        }
        if (role == null || role.trim().isEmpty()) {
            return userRepository.countByEmailContaining(email);
        }
        try {
            roleEnum = Role.valueOf(role);
            return userRepository.countByEmailContainingAndRole(email, roleEnum);
        } catch (IllegalArgumentException ignored) {
            return 0;
        }
    }

    @Override
    public Page<User> getUsersFromNToMForSearch(int index, String email, String role) {
        int pageSize = 20;
        Role roleEnum;
        if ((email == null || email.trim().isEmpty()) && (role == null || role.trim().isEmpty())) {
            return null;
        }
        if (email == null || email.trim().isEmpty()) {
            try {
                roleEnum = Role.valueOf(role);
                return userRepository.findAllByRole(PageRequest.of(index - 1, pageSize).withSort(Sort.by("registerDate")), roleEnum);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        if (role == null || role.trim().isEmpty()) {
            return userRepository.findAllByEmailContaining(PageRequest.of(index - 1, pageSize).withSort(Sort.by("registerDate")), email);
        }
        return userRepository.findAllByEmailContainingAndRole(PageRequest.of(index - 1, pageSize).withSort(Sort.by("registerDate")), email, Role.valueOf(role));
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public Optional<User> findByToken(String token) {
        return userRepository.findByToken(token);
    }

    @Override
    public List<User> findUserByActivated(boolean isActive) {
        return userRepository.findUserByActivated(isActive);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public List<User> getLast4Users() {
        return userRepository.findTop4ByOrderByRegisterDateDesc();
    }

    @Override
    @Transactional
    public void blockById(int id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
            List<Job> allByUser = jobRepository.getAllByUser(user);
            for (Job job : allByUser) {
                job.setDeleted(true);
                jobRepository.save(job);
            }
            Optional<Company> byUser = companyRepository.findByUser(user);
            if (byUser.isPresent()) {
                Company company = byUser.get();
                company.setActive(false);
                companyRepository.save(company);
            }
            Optional<Resume> byUser1 = resumeRepository.findByUser(user);
            if (byUser1.isPresent()) {
                Resume resume = byUser1.get();
                resume.setActive(false);
                resumeRepository.save(resume);
            }
            List<JobApplies> allByToJobSeeker = jobAppliesRepository.findAllByToJobSeeker(user);
            for (JobApplies jobApplies : allByToJobSeeker) {
                jobApplies.setActive(false);
                jobAppliesRepository.save(jobApplies);
            }
            List<ApplicantList> allByToEmployer = applicantListRepository.findAllByToEmployer(user);
            for (ApplicantList applicantList : allByToEmployer) {
                applicantList.setActive(false);
                applicantListRepository.save(applicantList);
            }
            user.setDeleted(true);
            userRepository.save(user);
            sendMailService.sendEmailAccountBlocked(user);
        }
    }

    @Override
    @Transactional
    public void unlockById(int id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
            List<Job> allByUser = jobRepository.getAllByUser(user);
            for (Job job : allByUser) {
                job.setDeleted(false);
                jobRepository.save(job);
            }
            Optional<Company> byUser = companyRepository.findByUser(user);
            if (byUser.isPresent()) {
                Company company = byUser.get();
                company.setActive(true);
                companyRepository.save(company);
            }
            Optional<Resume> byUser1 = resumeRepository.findByUser(user);
            if (byUser1.isPresent()) {
                Resume resume = byUser1.get();
                resume.setActive(true);
                resumeRepository.save(resume);
            }
            List<JobApplies> allByToJobSeeker = jobAppliesRepository.findAllByToJobSeeker(user);
            for (JobApplies jobApplies : allByToJobSeeker) {
                jobApplies.setActive(true);
                jobAppliesRepository.save(jobApplies);
            }
            List<ApplicantList> allByToEmployer = applicantListRepository.findAllByToEmployer(user);
            for (ApplicantList applicantList : allByToEmployer) {
                applicantList.setActive(true);
                applicantListRepository.save(applicantList);
            }
            user.setDeleted(false);
            userRepository.save(user);
            sendMailService.sendEmailAccountUnlocked(user);
        }
    }
}
