package com.example.hobiday_backend.domain.perform.repository;

import com.example.hobiday_backend.domain.perform.entity.Perform;
import com.example.hobiday_backend.domain.perform.exception.PerformErrorCode;
import com.example.hobiday_backend.domain.perform.exception.PerformException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.hobiday_backend.domain.perform.entity.QPerform.perform;

@Repository
public class PerformCustomRepositoryImpl implements PerformCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public PerformCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<Perform> findAllBySelectAreaAndGenre(String keyword, List<String> genres, List<String> areas) {
        if(keyword.isEmpty() && genres.isEmpty() && areas.isEmpty()){
            throw new PerformException(PerformErrorCode.PERFORM_NOT_ALLOWED);
        }else if(keyword.isEmpty()){
            throw new PerformException(PerformErrorCode.PERFORM_NOT_ALLOWED);
        }
        else if (genres.isEmpty() && areas.isEmpty()){
            return jpaQueryFactory
                    .selectFrom(perform)
                    .where(perform.prfnm.contains(keyword))
                    .fetch();
        }else if (genres.isEmpty()){
            return jpaQueryFactory
                    .selectFrom(perform)
                    .where(perform.prfnm.contains(keyword),
                            perform.area.in(areas))
                    .fetch();
        }else if(areas.isEmpty()){
            return jpaQueryFactory
                    .selectFrom(perform)
                    .where(perform.prfnm.contains(keyword),
                            perform.genrenm.in(genres))
                    .fetch();
        }
        return jpaQueryFactory
                .selectFrom(perform)
                .where(perform.prfnm.contains(keyword),
                        perform.genrenm.in(genres),
                        perform.area.in(areas))
                .fetch();

    }

    @Override
    public List<Perform> findTenBySelectGenre(List<String> genres) {
        List<Perform> performs = jpaQueryFactory
                .selectFrom(perform)
                .where(perform.genrenm.in(genres))
                .orderBy(perform.prfpdfrom.asc())
                .limit(10)
                .fetch();

        int index = 0;
        while(performs.size() < 10){
            performs.addAll(jpaQueryFactory
                    .selectFrom(perform)
                    .where(perform.genrenm.notIn(genres))
                    .orderBy(perform.prfpdfrom.asc())
                    .offset(index++)
                    .limit(1)
                    .fetch());
        }

        return performs;
    }
}
