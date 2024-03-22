package com.phcworld.answer.controller.port;

import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.answer.domain.dto.FreeBoardAnswerRequest;

public interface FreeBoardAnswerService {
    FreeBoardAnswer register(FreeBoardAnswerRequest request);
//    FreeBoardAnswer getFreeBoardAnswer(Long answerId);
    FreeBoardAnswer update(FreeBoardAnswerRequest request);
    FreeBoardAnswer delete(Long answerId);
}
