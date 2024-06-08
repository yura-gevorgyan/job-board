package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.*;
import am.itspace.jobboard.entity.enums.Role;
import am.itspace.jobboard.exception.EmailIsPresentException;
import am.itspace.jobboard.exception.NotFoundException;
import am.itspace.jobboard.exception.PasswordNotMuchException;
import am.itspace.jobboard.exception.UseOldPasswordException;
import am.itspace.jobboard.repository.*;
import am.itspace.jobboard.service.*;
import am.itspace.jobboard.util.GenerateTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final ChatroomRepository chatroomRepository;
    private final ResumeWishlistService resumeWishlistService;
    private final CompanyWishlistService companyWishlistService;
    private final JobWishlistService jobWishlistService;
    private final CompanyPictureService companyPicture;

    private static final int PAGE_SIZE = 20;

    @Override
    public User
    register(User user, String confirmPassword, Role role) {
        Optional<User> byEmail = userRepository.findByEmail(user.getEmail());

        if (byEmail.isPresent() && !byEmail.get().isDeleted()) {
            throw new EmailIsPresentException();
        } else if (!user.getPassword().equals(confirmPassword)) {
            throw new PasswordNotMuchException();
        } else if (byEmail.isPresent() && byEmail.get().isDeleted()) {

            byEmail.get().setName(user.getName());
            byEmail.get().setSurname(user.getSurname());
            byEmail.get().setPassword(passwordEncoder.encode(user.getPassword()));
            byEmail.get().setRole(role);
            byEmail.get().setToken(GenerateTokenUtil.generateToken());
            byEmail.get().setActivated(false);
            byEmail.get().setDeleted(false);
            byEmail.get().setRegisterDate(new Date());

            User save = userRepository.save(byEmail.get());
            sendMailService.sendEmailConfirmMail(save);

            return save;
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
    public void changePassword(String password, String confirmPassword, User user) {

        if (!password.equals(confirmPassword)) {
            throw new PasswordNotMuchException();
        } else if (passwordEncoder.matches(password, user.getPassword())) {
            throw new UseOldPasswordException();
        }
        user.setPassword(passwordEncoder.encode(password));
        save(user);
        sendMailService.send(user.getEmail(), "Changing Password", "Password successfully changed!");
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
    public Page<User> findAllUsers(int index) {
        return userRepository.findAll(PageRequest.of(index - 1, PAGE_SIZE).withSort(Sort.by("registerDate")));
    }

    @Override
    public Page<User> findAllUsers(Specification<User> specification, int index) {
        return userRepository.findAll(specification, PageRequest.of(index - 1, PAGE_SIZE).withSort(Sort.by("registerDate")));
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
    public User findByIdAndIsActiveTrue(int id) {
        return userRepository.findByIdAndActivatedTrueAndIsDeletedFalse(id).orElse(null);
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
    public List<User> findUserByPasswordAndIsActivatedFalse(String password) {
        return userRepository.findUserByPasswordAndActivatedFalse(password);
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

    @Override
    public List<User> findUserFriendsHavingChatWith(int currentUserId) {
        List<User> users;
        List<Chatroom> allBySenderId = chatroomRepository.findAllBySenderId(currentUserId);
        users = allBySenderId.stream().map(Chatroom::getRecipient).collect(Collectors.toList());
        return users;
    }

    @Override
    public User findById(int id) {
        return userRepository.findByIdAndIsDeletedFalse(id).orElse(null);
    }

    @Override
    public void updateOAuth2User(User user) {

        Optional<User> byEmail = userRepository.findByEmail(user.getEmail());
        byEmail.ifPresentOrElse(existingUser -> {

            existingUser.setName(user.getName());
            existingUser.setSurname(user.getSurname());
            existingUser.setRole(user.getRole());
            existingUser.setActivated(true);
            userRepository.save(existingUser);

        }, () -> {
            throw new NotFoundException();
        });
    }

    @Override
    public void deleteProfileCode(User user) {
        user.setToken(GenerateTokenUtil.generateToken());
        userRepository.save(user);
        sendMailService.sendEmailConfirmMailForDelete(user);
    }

    @Override
    @Transactional
    public void deleteProfile(User user) {
        switch (user.getRole().getName()) {
            case "Job Seeker" -> {
                Optional<Resume> byUser = resumeRepository.findByUser(user);

                if (byUser.isPresent()) {
                    List<ApplicantList> allByResume = applicantListRepository.findAllByResume(byUser.get());

                    allByResume.parallelStream().
                            forEach(applicantList -> {
                                applicantList.setActive(false);
                                applicantListRepository.save(applicantList);
                            });
                    resumeWishlistService.deleteByResume(byUser.get());
                    byUser.get().setActive(false);
                    resumeRepository.save(byUser.get());
                }

                List<JobApplies> allByToJobSeeker = jobAppliesRepository.findAllByToJobSeeker(user);
                allByToJobSeeker.parallelStream().forEach(jobApplies -> {
                    jobApplies.setActive(false);
                    jobAppliesRepository.save(jobApplies);
                });

            }
            case "Employee" -> {
                List<ApplicantList> allByToEmployer = applicantListRepository.findAllByToEmployer(user);

                allByToEmployer.parallelStream().
                        forEach(applicantList -> {
                            applicantList.setActive(false);
                            applicantListRepository.save(applicantList);
                        });


                for (Job job : jobRepository.findByUserId(user.getId())) {

                    for (JobApplies jobApplies : jobAppliesRepository.findAllByJob(job)) {
                        jobApplies.setActive(false);
                        jobAppliesRepository.save(jobApplies);
                    }
                    jobWishlistService.deleteByJob(job);
                    job.setDeleted(true);
                    jobRepository.save(job);
                }
            }
            case "Company Owner" -> {
                Optional<Company> byUser = companyRepository.findByUser(user);

                for (ApplicantList applicantList : applicantListRepository.findAllByToEmployer(user)) {
                    applicantList.setActive(false);
                    applicantListRepository.save(applicantList);
                }

                for (Job job : jobRepository.findByUserId(user.getId())) {

                    for (JobApplies jobApplies : jobAppliesRepository.findAllByJob(job)) {
                        jobApplies.setActive(false);
                        jobAppliesRepository.save(jobApplies);
                    }
                    jobWishlistService.deleteByJob(job);
                    job.setDeleted(true);
                    jobRepository.save(job);
                }

                if (byUser.isPresent()) {
                    companyWishlistService.deleteByCompany(byUser.get());
                    companyPicture.deleteByCompanyId(byUser.get().getId());
                    byUser.get().setActive(false);
                    companyRepository.save(byUser.get());
                }
            }
        }

        resumeWishlistService.deleteByUserId(user.getId());
        companyWishlistService.deleteByUserId(user.getId());
        jobWishlistService.deleteByUserId(user.getId());

        user.setDeleted(true);
        userRepository.save(user);
    }

    @Override
    public User confirmEmailForDelete(String confirmEmailCode) {
        Optional<User> optionalUser = findByToken(confirmEmailCode);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setActivated(true);
            save(user);
            sendMailService.send(user.getEmail(), "messageForDeleting", "Email confirmed For Deleting your Profile !");
            return user;
        }
        return null;
    }
}
