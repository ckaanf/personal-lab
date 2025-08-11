package org.example.graphqlprac.domain.instructor.service;

import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoaderWithContext;
import org.dataloader.DataLoaderFactory;
import org.dataloader.DataLoaderRegistry;
import org.example.graphqlprac.domain.instructor.repository.InstructorRepository;
import org.example.graphqlprac.domain.instructor.model.Instructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InstructorDataLoader {
    private final InstructorRepository instructorRepository;

    // BatchLoaderWithContext로 비동기 batch 처리 구현
    public BatchLoaderWithContext<String, Instructor> batchLoader() {
        return (ids, env) -> CompletableFuture.supplyAsync(() -> {
            List<Instructor> instructors = instructorRepository.findAllByIdIn(ids);
            Map<String, Instructor> instructorMap = instructors.stream()
                    .collect(Collectors.toMap(Instructor::getId, inst -> inst));
            return ids.stream()
                    .map(instructorMap::get)
                    .collect(Collectors.toList());
        });
    }

    // DataLoaderRegistry에 등록하는 메서드 (WebGraphQlInterceptor에서 사용)
    public void register(DataLoaderRegistry registry) {
        registry.register("instructorDataLoader", DataLoaderFactory.newMappedDataLoader((Set<String> keys) -> {
            List<Instructor> instructors = instructorRepository.findAllByIdIn(keys);
            Map<String, Instructor> instructorMap = instructors.stream()
                    .collect(Collectors.toMap(Instructor::getId, inst -> inst));
            return CompletableFuture.completedFuture(instructorMap);
        }));
    }
}
