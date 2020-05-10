package com.example.demo.businessLogic;

import com.example.demo.model.BusinessUser;
import com.example.demo.model.Sickness;
import com.example.demo.model.Symptoms;
import com.example.demo.repository.AbstractJpaDao;
import com.example.demo.repository.UserLogic;
import org.apache.log4j.Logger;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Component
public class SicknessBusinessLogic extends AbstractJpaDao<Sickness> {
    Logger logger = Logger.getLogger(SicknessBusinessLogic.class);

    public List<Sickness> getByColumnName(String columnName, Object value) {
        List<Sickness> sicknessList = new ArrayList<>();
        try {
            CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
            CriteriaQuery<Sickness> cr = cb.createQuery(Sickness.class);
            Root<Sickness> root = cr.from(Sickness.class);
            Predicate restriction = cb.equal(root.get(columnName), value);
            cr.select(root).where(restriction).orderBy(cb.asc(root.get("id")));
            Query<Sickness> query = getCurrentSession().createQuery(cr);
            sicknessList = query.getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return sicknessList;
    }
}
