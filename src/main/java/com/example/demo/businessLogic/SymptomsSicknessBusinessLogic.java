package com.example.demo.businessLogic;

import com.example.demo.model.Sickness;
import com.example.demo.model.Symptoms;
import com.example.demo.model.SymptomsSickness;
import com.example.demo.repository.AbstractJpaDao;
import com.example.demo.utils.Constant;
import com.example.demo.utils.Utility;
import org.apache.log4j.Logger;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class SymptomsSicknessBusinessLogic extends AbstractJpaDao<SymptomsSickness> {
    Logger logger = Logger.getLogger(SymptomsBusinessLogic.class);
    Utility utility;

    public SymptomsSicknessBusinessLogic(Utility utility) {
        this.utility = utility;
    }


    public List<String> getPredictionList(String[] predictionArray) {
        List<String> predictionList = new ArrayList<>();
        List<SymptomsSickness> symptomsSicknesses;
        try {
            CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
            CriteriaQuery<SymptomsSickness> cr = cb.createQuery(SymptomsSickness.class);
            Root<SymptomsSickness> root = cr.from(SymptomsSickness.class);
            Join<SymptomsSickness, Symptoms> symptomsJoin = root.join("symptoms");
            Join<SymptomsSickness, Symptoms> sicknessJoin = root.join("sickness");
            Predicate symptomsName1 = cb.equal(symptomsJoin.get("symptomsName"),utility.capitalizeFirstLetter(predictionArray[0]));
            Predicate symptomsName2 = cb.equal(symptomsJoin.get("symptomsName"), utility.capitalizeFirstLetter(predictionArray[1]));
            Predicate symptomsName3 = cb.like(symptomsJoin.get("symptomsName"), "%" + predictionArray[2] + "%");
            Predicate symptomsName4 = cb.like(symptomsJoin.get("symptomsName"), "%" + predictionArray[3] + "%");
            Predicate symptomsName5 = cb.like(symptomsJoin.get("symptomsName"), "%" + predictionArray[4] + "%");
            Predicate symptomsName6 = cb.like(symptomsJoin.get("symptomsName"), "%" + predictionArray[5] + "%");
            Predicate predicateOr = cb.or(symptomsName1, symptomsName2);
            Predicate predicateAnd = cb.and(symptomsName3, symptomsName4);
            Predicate predicate = cb.and(symptomsName5, symptomsName6);
            Predicate combinedPredicate = cb.or(predicateOr, predicateAnd, predicate);
            Predicate symptomsStatus = cb.equal(symptomsJoin.get("status"), Constant.STATUS.ACTIVATED.value);
            Predicate SymptomsSicknessStatus = cb.equal(root.get("status"), Constant.STATUS.ACTIVATED.value);
            Predicate sicknessPredicate = cb.equal(sicknessJoin.get("status"), Constant.STATUS.ACTIVATED.value);
            cr.select(root).where(cb.and(combinedPredicate, symptomsStatus, SymptomsSicknessStatus, sicknessPredicate)).orderBy(cb.asc(root.get("id")));
            Query<SymptomsSickness> query = getCurrentSession().createQuery(cr);
            symptomsSicknesses = query.getResultList();
            for (SymptomsSickness symptomsSickness : symptomsSicknesses) {
                predictionList.add(symptomsSickness.getSickness().getSicknessName());
            }

        } catch (Exception e) {

        }
        return predictionList;
    }

    public List<SymptomsSickness> searchBySymptomAndSickness(Symptoms symptoms, Sickness sickness) {
        List<SymptomsSickness> symptomsSicknesses = new ArrayList<>();
        try {
            CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
            CriteriaQuery<SymptomsSickness> cr = cb.createQuery(SymptomsSickness.class);
            Root<SymptomsSickness> root = cr.from(SymptomsSickness.class);
            Predicate symptomRestriction = cb.equal(root.get("symptoms"), symptoms);
            Predicate sicknessRestriction = cb.equal(root.get("sickness"), sickness);
            Predicate status = cb.equal(root.get("status"), Constant.STATUS.ACTIVATED.value);
            cr.select(root).where(cb.and(symptomRestriction, sicknessRestriction, status)).orderBy(cb.asc(root.get("id")));
            Query<SymptomsSickness> query = getCurrentSession().createQuery(cr);
            symptomsSicknesses = query.getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return symptomsSicknesses;
    }


    public List<SymptomsSickness> getByColumnName(String columnName, Object value) {
        List<SymptomsSickness> symptomsSicknesses = new ArrayList<>();
        try {
            CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
            CriteriaQuery<SymptomsSickness> cr = cb.createQuery(SymptomsSickness.class);
            Root<SymptomsSickness> root = cr.from(SymptomsSickness.class);
            Predicate restriction = cb.equal(root.get(columnName), value);
            cr.select(root).where(restriction).orderBy(cb.asc(root.get("id")));
            Query<SymptomsSickness> query = getCurrentSession().createQuery(cr);
            symptomsSicknesses = query.getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return symptomsSicknesses;
    }
}
