package com.example.demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentsRepository studentsRepository;
    @Autowired
    public StudentService(StudentsRepository studentsRepository) {
        this.studentsRepository = studentsRepository;
    }

    @GetMapping
    public List<Student> getStudents(){
        return studentsRepository.findAll();

    }

    public void addNewStudent(Student student) {

       Optional<Student> studentOptional = studentsRepository.findStudentByEmail(student.getEmail());
       if(studentOptional.isPresent()){
           throw new IllegalStateException("email taken already");
       }
       studentsRepository.save(student);
       System.out.println(student);
    }

    public void deleteStudent(Long id) {

    boolean exists =studentsRepository.existsById(id);
    if(!exists){
        throw new IllegalStateException("student with id "+id+" does not exist");
    }
    studentsRepository.deleteById(id);
    }
    @Transactional
    public void updateStudent(Long studentId, String name, String email) {
        Student student = studentsRepository.findById(studentId).orElseThrow(() -> new IllegalStateException("student with that id does not exist"));
        if(name != null){
            student.setName(name);
        }
        if(email != null){
            Optional<Student> studentOptional = studentsRepository.findStudentByEmail(email);
            if(studentOptional.isPresent()){
                throw new IllegalStateException("email is taken");
            }
                      student.setEmail(email);
        }

    }
}
