package com.example.projectmanagement.repository;

import com.example.projectmanagement.entity.Project;
import com.example.projectmanagement.enums.ProjectCriteria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class ProjectRepositoryCustomImpl implements ProjectRepositoryCustom{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int deleteByCriteria(ProjectCriteria byWhich, String value) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<Project> delete = cb.createCriteriaDelete(Project.class);
        Root<Project> root = delete.from(Project.class);

        switch (byWhich) {
            case ID:
                delete.where(cb.equal(root.get("projectId"), Integer.parseInt(value)));
                break;
            case NAME:
                delete.where(cb.equal(root.get("projectName"), value));
                break;
            case TYPE_ID:
                delete.where(cb.equal(root.get("projectType").get("projectTypeId"), Integer.parseInt(value)));
                break;
            case SUBTYPE_ID:
                delete.where(cb.equal(root.get("projectSubType").get("projectSubTypeId"), Integer.parseInt(value)));
                break;
        }

        return entityManager.createQuery(delete).executeUpdate();
    }

    @Override
    public List<Project> findByCriteria(ProjectCriteria byWhich, String value) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Project> query = cb.createQuery(Project.class);
        Root<Project> root = query.from(Project.class);

        switch (byWhich) {
            case ID:
                query.where(cb.equal(root.get("projectId"), Integer.parseInt(value)));
                break;
            case NAME:
                query.where(cb.equal(root.get("projectName"), value));
                break;
            case TYPE_ID:
                query.where(cb.equal(root.get("projectType").get("projectTypeId"), Integer.parseInt(value)));
                break;
            case SUBTYPE_ID:
                query.where(cb.equal(root.get("projectSubType").get("projectSubTypeId"), Integer.parseInt(value)));
                break;
        }

        return entityManager.createQuery(query).getResultList();
    }
}
