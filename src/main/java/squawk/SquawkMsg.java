package squawk;

public class SquawkMsg {
	String Msg;
	String MsgDT;
	String AuthorName;
	int MsgID;
	int Likes;
	
	public SquawkMsg(String msg, String msgDT, String AuthorName, int MsgID, int Likes) {
		super();
		this.Msg = msg;
		this.MsgDT = msgDT;
		this.AuthorName = AuthorName;
		this.MsgID = MsgID;
		this.Likes = Likes;
	}
	
	
	
}
