package com.github.fabasset.advice;

public class ApiError {

    private String status;

    private String message;
    
    public ApiError() {}
    
    public ApiError(String status, String message) {
    	this.status = status;
    	this.message = message;
    }

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ApiError [status=").append(status).append(", message=").append(message).append("]");
		return builder.toString();
	}
}