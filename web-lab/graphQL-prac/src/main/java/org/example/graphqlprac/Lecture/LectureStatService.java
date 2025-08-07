package org.example.graphqlprac.Lecture;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LectureStatService {
    private final MongoTemplate mongoTemplate;

    public LectureStatService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<LectureStats> getEnrollmentStats() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("enrolled").is(true)),
                Aggregation.group("title").count().as("enrolledCount"),
                Aggregation.project("enrolledCount").and("title").previousOperation(),
                Aggregation.sort(Sort.Direction.DESC, "enrolledCount")
        );

        AggregationResults<LectureStats> results = mongoTemplate.aggregate(
                aggregation,
                "lectures", // 컬렉션 이름
                LectureStats.class
        );
        return results.getMappedResults();
    }
}
