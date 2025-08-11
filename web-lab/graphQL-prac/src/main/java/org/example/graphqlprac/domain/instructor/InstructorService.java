package org.example.graphqlprac.domain.instructor;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InstructorService {
    private final Logger log = LoggerFactory.getLogger(InstructorService.class);
    private final InstructorRepository instructorRepository;

   public List<Instructor> findAllByIds(Collection<String> ids) {
       return instructorRepository.findAllByIdIn(ids);
   }

   public Instructor findById(String id) {
       return instructorRepository.findById(id).orElseThrow(() -> new RuntimeException());
   }

   public List<Instructor> findAll() {
       return instructorRepository.findAll();
   }
}
