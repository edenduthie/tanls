package tanls.dto;

import java.util.ArrayList;
import java.util.List;

import tanls.entity.Lawyer;

public class LawyersSearchResponse extends ResponseDTO {

	List<LawyerDTO> lawyers;
	
	public LawyersSearchResponse() {
		super();
	}
	
	public LawyersSearchResponse(List<Lawyer> lawyers) {
		super();
		this.lawyers = new ArrayList<LawyerDTO>();
		if( lawyers != null )
		{
	        for( Lawyer lawyer : lawyers ) 
	        {
	        	this.lawyers.add(new LawyerDTO(lawyer));
	        }
		}
	}

	public LawyersSearchResponse(String message, boolean success) {
		super(message, success);
		// TODO Auto-generated constructor stub
	}

	public LawyersSearchResponse(String message, boolean success,
			boolean redirectToLogin) {
		super(message, success, redirectToLogin);
		// TODO Auto-generated constructor stub
	}

	public List<LawyerDTO> getLawyers() {
		return lawyers;
	}

	public void setLawyers(List<LawyerDTO> lawyers) {
		this.lawyers = lawyers;
	}

}
