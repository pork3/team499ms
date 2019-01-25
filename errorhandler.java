public enum Error_c{
	OutOfRage(0,"input date out of range."),
	ErrorDate(1,"input date is not correct."),
	CantOpenFile(2,"can't read input file.");

	private int code;
	private String error_message;

 	Error(int code, String error_message){
		this.code = code;
		this.error_message = error_message;
	}

	public String getMessage(){
		return error_message;
	}

	public int getCode(){
		return code;
	}
}