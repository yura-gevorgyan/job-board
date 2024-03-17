package am.itspace.jobboard.service;

import am.itspace.jobboard.entity.Resume;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ResumeService {

    int getResumeCount();

    List<Resume> getLast6Resumes();

    int getTotalPages();

    Page<Resume> getResumesFromNToM(int index);

    int getTotalPagesOfSearch(int categoryId, String userEmail);

    int getResumeCountOfCategoryUserEmail(int categoryId, String userEmail);

    Page<Resume> getResumesFromNToMForSearch(int searchIndex, int categoryId, String userEmail);

    void deleteById(int id);
}
