package com.example.demo.businessLogic;

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
public class SymptomsBusinessLogic  extends AbstractJpaDao<Symptoms> {
    Logger logger = Logger.getLogger(SymptomsBusinessLogic.class);


    public List<Symptoms> getByColumnName(String columnName, Object value) {
        List<Symptoms> symptomsList = new ArrayList<>();
        try {
            CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
            CriteriaQuery<Symptoms> cr = cb.createQuery(Symptoms.class);
            Root<Symptoms> root = cr.from(Symptoms.class);
            Predicate restriction = cb.equal(root.get(columnName), value);
            cr.select(root).where(restriction).orderBy(cb.asc(root.get("id")));
            Query<Symptoms> query = getCurrentSession().createQuery(cr);
            symptomsList = query.getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return symptomsList;
    }

}
