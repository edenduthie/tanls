package tanls.dto;

import java.util.List;

import tanls.entity.Question;

public class QuestionListDTO extends ResponseDTO
{
    private List<QuestionDTO> questions;

	public QuestionListDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QuestionListDTO(String message, boolean success,
			boolean redirectToLogin) {
		super(message, success, redirectToLogin);
		// TODO Auto-generated constructor stub
	}

	public QuestionListDTO(String message, boolean success) {
		super(message, success);
		// TODO Auto-generated constructor stub
	}

	public List<QuestionDTO> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionDTO> questions) {
		this.questions = questions;
	}
}
