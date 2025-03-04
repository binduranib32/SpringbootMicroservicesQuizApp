package com.ib.quiz_service.service;

import com.ib.quiz_service.dao.QuizDao;
import com.ib.quiz_service.feign.QuizInterface;
import com.ib.quiz_service.model.QuestionWrapper;
import com.ib.quiz_service.model.Quiz;
import com.ib.quiz_service.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {


    @Autowired
    QuizDao quizDao;

    @Autowired
    QuizInterface quizInterface;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {

         // call the generate url from QuestionController service - RestTemplate http://localhost:9090/question/generate
          List<Integer> questions = quizInterface.getQuestionsForQuiz(category, numQ).getBody();
          Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questions);
        quizDao.save(quiz);

        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Quiz quiz = quizDao.findById(id).get();
        List<Integer> questionIds = quiz.getQuestionIds();

        ResponseEntity<List<QuestionWrapper>> questions = quizInterface.getQuestionsFromId(questionIds);
        return questions;
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
      ResponseEntity<Integer> score = quizInterface.getScore(responses);
        return score;
    }

}
